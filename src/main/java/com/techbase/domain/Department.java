package com.techbase.domain;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import org.apache.commons.lang3.Validate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author nguyentanh
 */
@Entity
@Table(name = "DEPARTMENT")
@FieldNameConstants
@Accessors(fluent = true)
@Getter
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "MANAGER_ID")
    private Employee employee;

    private Department() {
    }

    public Department(String name) {
        this.name = name;
    }

    public void addManager(Employee employee) {
        Validate.isTrue(employee.role().name() == Role.Name.MANAGER, "Employee must MANAGER");
        this.employee = employee;
    }

}
