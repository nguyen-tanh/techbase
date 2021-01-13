package com.techbase.domain;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
@Table(name = "EMPLOYEE")
@FieldNameConstants
@Accessors(fluent = true)
@Getter
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "EMP_TEAM",
            joinColumns = {@JoinColumn(name = "EMPLOYEE_ID")},
            inverseJoinColumns = {@JoinColumn(name = "TEAM_ID")})
    private final Set<Team> teams = new HashSet<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ROLE_ID")
    private Role role;

    @Column(name = "USER_NAME", nullable = false, unique = true)
    private String userName;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    public boolean verifyPassword(PasswordEncoder passwordEncoder, String rawPassword) {
        return passwordEncoder.matches(rawPassword, this.password);
    }

    private Employee() {
    }

    public static class Builder {
        private final String userName;
        private final String password;
        private final String name;
        private Role role;
        private final Set<Team> teams = new HashSet<>();

        public Builder(String userName, String password, String name) {
            this.userName = userName;
            this.password = password;
            this.name = name;
        }

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public Builder team(Team team) {
            this.teams.add(team);
            return this;
        }

        public Employee build() {
            Employee employee = new Employee();
            employee.userName = this.userName;
            employee.password = this.password;
            employee.role = this.role;
            employee.name = name;
            employee.teams.addAll(teams);

            return employee;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Employee employee = (Employee) o;

        return new EqualsBuilder().append(userName, employee.userName).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(userName).toHashCode();
    }
}
