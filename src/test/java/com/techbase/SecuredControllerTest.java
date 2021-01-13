package com.techbase;

import com.fasterxml.jackson.databind.JsonNode;
import com.techbase.application.AuthenticationService;
import com.techbase.application.EmployeeReportService;
import com.techbase.domain.Role;
import com.techbase.interfaces.handle.dto.EmployeeReportDTO;
import com.techbase.interfaces.handle.response.APIResponse;
import com.techbase.interfaces.handle.response.PagerResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

/**
 * @author nguyentanh
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SecuredControllerTest extends MyApplicationTests {

    @Autowired
    WebApplicationContext context;

    protected MockMvc mvc;

    @MockBean
    private EmployeeReportService employeeReportService;

    @Autowired
    private AuthenticationService authenticationService;


    @BeforeAll
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        Objects.requireNonNull(context.getServletContext()).setAttribute(
                WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);
    }

    @Test
    @Description("No token")
    void authenticate0() throws Exception {
        PagerResponse<EmployeeReportDTO> response = new PagerResponse<>(new ArrayList<>(), 0, 0, 0);
        when(employeeReportService.reportEmployees(any())).thenReturn(response);

        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders.get("/api/user/employee")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(result -> assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus()))
                .andReturn();

        JsonNode jsonNode = getJsonNode(mvcResult.getResponse().getContentAsString());

        String statusMessage = jsonNode.get("statusMessage").asText();
        assertEquals(APIResponse.StatusMessage.HEADER_ERROR.getStatus(), statusMessage);

        String messageError = jsonNode.get("errorMessages").elements().next().get("message").asText();
        assertEquals("Cannot find token", messageError);
    }

    @Test
    @Description("Token is invalid")
    void authenticate1() throws Exception {
        PagerResponse<EmployeeReportDTO> response = new PagerResponse<>(new ArrayList<>(), 0, 0, 0);
        when(employeeReportService.reportEmployees(any())).thenReturn(response);

        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders.get("/api/user/employee")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer xyz")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(result -> assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus()))
                .andReturn();

        JsonNode jsonNode = getJsonNode(mvcResult.getResponse().getContentAsString());

        String statusMessage = jsonNode.get("statusMessage").asText();
        assertEquals(APIResponse.StatusMessage.AUTHENTICATION_ERROR.getStatus(), statusMessage);

        String messageError = jsonNode.get("errorMessages").elements().next().get("message").asText();
        assertEquals("Cannot verify authentication", messageError);
    }

    @Test
    @Description("Token is expired")
    void authenticate2() throws Exception {
        PagerResponse<EmployeeReportDTO> response = new PagerResponse<>(new ArrayList<>(), 0, 0, 0);
        when(employeeReportService.reportEmployees(any())).thenReturn(response);

        String token = authenticationService.createToken("admin", Role.Name.DIRECTOR.create(), Duration.ofSeconds(-1));
        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders.get("/api/user/employee")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(result -> assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus()))
                .andReturn();

        JsonNode jsonNode = getJsonNode(mvcResult.getResponse().getContentAsString());

        String statusMessage = jsonNode.get("statusMessage").asText();
        assertEquals(APIResponse.StatusMessage.AUTHENTICATION_ERROR.getStatus(), statusMessage);

        String messageError = jsonNode.get("errorMessages").elements().next().get("message").asText();
        assertEquals("Cannot verify authentication", messageError);
    }

    @Test
    @Description("Token has ROLE wrong")
    void authenticate3() throws Exception {
        PagerResponse<EmployeeReportDTO> response = new PagerResponse<>(new ArrayList<>(), 0, 0, 0);
        when(employeeReportService.reportEmployees(any())).thenReturn(response);

        String token = authenticationService.createToken("admin", Role.Name.MEMBER.create(), Duration.ofSeconds(1));
        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders.get("/api/user/employee")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(result -> assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus()))
                .andReturn();

        JsonNode jsonNode = getJsonNode(mvcResult.getResponse().getContentAsString());

        String statusMessage = jsonNode.get("statusMessage").asText();
        assertEquals(APIResponse.StatusMessage.AUTHENTICATION_ERROR.getStatus(), statusMessage);

        String messageError = jsonNode.get("errorMessages").elements().next().get("message").asText();
        assertEquals("Access denied!", messageError);
    }

    @Test
    @Description("Token is success")
    void authenticate4() throws Exception {
        PagerResponse<EmployeeReportDTO> response = new PagerResponse<>(new ArrayList<>(), 0, 0, 0);
        when(employeeReportService.reportEmployees(any())).thenReturn(response);

        String token = authenticationService.createToken("admin", Role.Name.DIRECTOR.create(), Duration.ofSeconds(1));
        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders.get("/api/user/employee")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(result -> assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus()))
                .andReturn();

        JsonNode jsonNode = getJsonNode(mvcResult.getResponse().getContentAsString());

        String statusMessage = jsonNode.get("statusMessage").asText();
        assertEquals(APIResponse.StatusMessage.SUCCESS.getStatus(), statusMessage);

        JsonNode data = jsonNode.get("data");
        assertNotNull(data);
    }
}
