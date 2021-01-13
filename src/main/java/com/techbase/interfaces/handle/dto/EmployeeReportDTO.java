package com.techbase.interfaces.handle.dto;

import lombok.Getter;

/**
 * @author nguyentanh
 */
@Getter
public class EmployeeReportDTO {

    private final String name;
    private final String role;
    private final String teams;
    private final String departments;

    public EmployeeReportDTO(String name, String role, String teams, String departments) {
        this.name = name;
        this.role = role;
        this.teams = teams;
        this.departments = departments;
    }
}
