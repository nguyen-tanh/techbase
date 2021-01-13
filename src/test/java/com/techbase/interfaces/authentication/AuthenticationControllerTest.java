package com.techbase.interfaces.authentication;

import com.fasterxml.jackson.databind.JsonNode;
import com.techbase.MyApplicationTests;
import com.techbase.domain.Employee;
import com.techbase.domain.EmployeeRepository;
import com.techbase.domain.Role;
import com.techbase.interfaces.handle.command.LoginCommand;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author nguyentanh
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthenticationControllerTest extends MyApplicationTests {

    @Autowired
    WebApplicationContext context;

    protected MockMvc mvc;

    @Autowired
    private EmployeeRepository employeeRepository;


    @BeforeAll
    void setUpEach() {
        MockitoAnnotations.openMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        Objects.requireNonNull(context.getServletContext()).setAttribute(
                WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);

        Employee employee = new Employee.Builder("admin", "$2a$04$0w0cXyYe777HV5.DEypKrOAI/QJ7WT7WPZ7a8AY6oBcwoBzm9tUUy", "Tran Van A")
                .role(Role.Name.DIRECTOR.create())
                .build();
        employeeRepository.save(employee);

    }

    @Test
    @Description("test user name is wrong")
    void authenticate0() throws Exception {

        LoginCommand loginCommand = new LoginCommand();
        loginCommand.setUserName("a");
        loginCommand.setPassword("admin");

        String requestJson = mapToJson(loginCommand);

        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders.post("/api/auth")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        JsonNode jsonNode = getJsonNode(mvcResult.getResponse().getContentAsString());

        String statusMessage = jsonNode.get("statusMessage").asText();
        assertEquals("300", statusMessage);

        String messageError = jsonNode.get("errorMessages").elements().next().get("message").asText();
        assertEquals("Login is fail", messageError);

        JsonNode data = jsonNode.get("data");
        assertNull(data);
    }

    @Test
    @Description("test password is wrong")
    void authenticate1() throws Exception {

        LoginCommand loginCommand = new LoginCommand();
        loginCommand.setUserName("admin");
        loginCommand.setPassword("a");

        String requestJson = mapToJson(loginCommand);

        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders.post("/api/auth")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        JsonNode jsonNode = getJsonNode(mvcResult.getResponse().getContentAsString());

        String statusMessage = jsonNode.get("statusMessage").asText();
        assertEquals("300", statusMessage);

        String messageError = jsonNode.get("errorMessages").elements().next().get("message").asText();
        assertEquals("Login is fail", messageError);

        JsonNode data = jsonNode.get("data");
        assertNull(data);
    }

    @Test
    @Description("test login is success")
    void authenticate() throws Exception {

        LoginCommand loginCommand = new LoginCommand();
        loginCommand.setUserName("admin");
        loginCommand.setPassword("admin");

        String requestJson = mapToJson(loginCommand);

        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders.post("/api/auth")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        JsonNode jsonNode = getJsonNode(mvcResult.getResponse().getContentAsString());

        String statusMessage = jsonNode.get("statusMessage").asText();
        assertEquals("000", statusMessage);

        JsonNode data = jsonNode.get("data");
        assertNotNull(data);
    }
}