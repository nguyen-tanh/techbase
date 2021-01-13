package com.techbase.domain;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Description;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author nguyentanh
 */
class DepartmentTest {

    @Test
    @Description("Add MEMBER as MANAGER")
    void addManager0() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Department department = new Department("Phong 1");
            Employee employee = new Employee.Builder("a","a", "a")
                    .role(Role.Name.MEMBER.create())
                    .build();
            department.addManager(employee);
        });

        assertEquals("Employee must MANAGER", exception.getMessage());
    }

    @Test
    @Description("Add a MANAGER")
    void addManager() {
        Department department = new Department("Phong 1");
        Employee employee = new Employee.Builder("a","a", "a")
                .role(Role.Name.MANAGER.create())
                .build();
        department.addManager(employee);
    }
}