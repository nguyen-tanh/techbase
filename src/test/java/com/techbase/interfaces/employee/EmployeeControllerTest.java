package com.techbase.interfaces.employee;

import com.fasterxml.jackson.databind.JsonNode;
import com.techbase.MyApplicationTests;
import com.techbase.domain.Department;
import com.techbase.domain.Employee;
import com.techbase.domain.EmployeeRepository;
import com.techbase.domain.Role;
import com.techbase.domain.Team;
import com.techbase.domain.TeamRepository;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author nguyentanh
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmployeeControllerTest extends MyApplicationTests {

    @Autowired
    WebApplicationContext context;

    protected MockMvc mvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TeamRepository teamRepository;

    @BeforeAll
    void setUpEach() {
        MockitoAnnotations.openMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        Objects.requireNonNull(context.getServletContext()).setAttribute(
                WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);

        Role director = Role.Name.DIRECTOR.create();
        Role manager = Role.Name.MANAGER.create();
        Role member = Role.Name.MEMBER.create();

        Employee A = new Employee.Builder("admin", "$2a$04$0w0cXyYe777HV5.DEypKrOAI/QJ7WT7WPZ7a8AY6oBcwoBzm9tUUy", "Tran Van A")
                .role(director)
                .build();

        Employee B = new Employee.Builder("adminB", "$2a$04$0w0cXyYe777HV5.DEypKrOAI/QJ7WT7WPZ7a8AY6oBcwoBzm9tUUy", "Tran Van B")
                .role(manager)
                .build();

        Employee C = new Employee.Builder("adminC", "$2a$04$0w0cXyYe777HV5.DEypKrOAI/QJ7WT7WPZ7a8AY6oBcwoBzm9tUUy", "Tran Van C")
                .role(member)
                .build();

        Employee D = new Employee.Builder("adminD", "$2a$04$0w0cXyYe777HV5.DEypKrOAI/QJ7WT7WPZ7a8AY6oBcwoBzm9tUUy", "Tran Van D")
                .role(member)
                .build();

        Employee BB = new Employee.Builder("adminBB", "$2a$04$0w0cXyYe777HV5.DEypKrOAI/QJ7WT7WPZ7a8AY6oBcwoBzm9tUUy", "Tran Van BB")
                .role(manager)
                .build();

        employeeRepository.save(A);

        Department department1 = new Department("Phong 1");
        department1.addManager(B);

        Department department2 = new Department("Phong 2");
        department2.addManager(BB);

        Team team1 = new Team("Team 1", department1);
        team1.addEmployee(C);

        Team team2 = new Team("Team 2", department2);
        team2.addEmployee(D);

        teamRepository.saveAll(Arrays.asList(team1, team2));

    }

    @Test
    @Description("Page 0")
    void getEmployees0() throws Exception {

        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders.get("/api/user/employee")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        JsonNode jsonNode = getJsonNode(mvcResult.getResponse().getContentAsString());

        String statusMessage = jsonNode.get("statusMessage").asText();
        assertEquals("000", statusMessage);

        List<JsonNode> jsonNodes = new ArrayList<>();
        final Iterator<JsonNode> elements = jsonNode.get("data").get("content").elements();
        elements.forEachRemaining(jsonNodes::add);
        List<String> names = jsonNodes.stream().map(jsonNode1 -> jsonNode1.get("name").asText())
                .collect(Collectors.toList());
        assertEquals(Arrays.asList("Tran Van A", "Tran Van B", "Tran Van BB", "Tran Van C", "Tran Van D"), names);

        long totalElements = jsonNode.get("data").get("totalElements").asLong();
        assertEquals(5, totalElements);

        long page = jsonNode.get("data").get("page").asLong();
        assertEquals(1, page);

        long size = jsonNode.get("data").get("size").asLong();
        assertEquals(1500, size);
    }

    @Test
    @Description("Page 1")
    void getEmployees1() throws Exception {

        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders.get("/api/user/employee?page=1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        JsonNode jsonNode = getJsonNode(mvcResult.getResponse().getContentAsString());

        String statusMessage = jsonNode.get("statusMessage").asText();
        assertEquals("000", statusMessage);

        List<JsonNode> jsonNodes = new ArrayList<>();
        final Iterator<JsonNode> elements = jsonNode.get("data").get("content").elements();
        elements.forEachRemaining(jsonNodes::add);
        List<String> names = jsonNodes.stream().map(jsonNode1 -> jsonNode1.get("name").asText())
                .collect(Collectors.toList());
        assertEquals(Arrays.asList("Tran Van A", "Tran Van B", "Tran Van BB", "Tran Van C", "Tran Van D"), names);

        long totalElements = jsonNode.get("data").get("totalElements").asLong();
        assertEquals(5, totalElements);

        long page = jsonNode.get("data").get("page").asLong();
        assertEquals(1, page);

        long size = jsonNode.get("data").get("size").asLong();
        assertEquals(1500, size);
    }

    @Test
    @Description("Page2")
    void getEmployees2() throws Exception {

        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders.get("/api/user/employee?page=2")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        JsonNode jsonNode = getJsonNode(mvcResult.getResponse().getContentAsString());

        String statusMessage = jsonNode.get("statusMessage").asText();
        assertEquals("000", statusMessage);

        List<JsonNode> jsonNodes = new ArrayList<>();
        final Iterator<JsonNode> elements = jsonNode.get("data").get("content").elements();
        elements.forEachRemaining(jsonNodes::add);
        List<String> names = jsonNodes.stream().map(jsonNode1 -> jsonNode1.get("name").asText())
                .collect(Collectors.toList());
        assertEquals(names.size(), 0);

        long totalElements = jsonNode.get("data").get("totalElements").asLong();
        assertEquals(5, totalElements);

        long page = jsonNode.get("data").get("page").asLong();
        assertEquals(2, page);

        long size = jsonNode.get("data").get("size").asLong();
        assertEquals(1500, size);
    }
}