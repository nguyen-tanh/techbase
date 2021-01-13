package com.techbase.domain;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * @author nguyentanh
 */
@Entity
@Table(name = "TEAM")
@FieldNameConstants
@Accessors(fluent = true)
@Getter
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "EMP_TEAM",
            joinColumns = {@JoinColumn(name = "TEAM_ID")},
            inverseJoinColumns = {@JoinColumn(name = "EMPLOYEE_ID")})
    private final Set<Employee> employees = new HashSet<>();

    @ManyToOne(cascade = CascadeType.ALL)
    private Department department;

    private Team() {
    }

    public Team(String name, Department department) {
        this.name = name;
        this.department = department;
    }

    public void addEmployee(Employee employee) {
        this.employees.add(employee);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Team team = (Team) o;

        return new EqualsBuilder().append(name, team.name).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(name).toHashCode();
    }
}
