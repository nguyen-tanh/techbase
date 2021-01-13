package com.techbase.domain;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author nguyentanh
 */
@Entity
@Table(name = "ROLE")
@FieldNameConstants
@Accessors(fluent = true)
@Getter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "NAME", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private Name name;

    private Role() {
    }

    private Role(Name name) {
        this.name = name;
    }

    public enum Name {
        DIRECTOR,
        MANAGER,
        MEMBER;

        public Role create() {
            return new Role(this);
        }
    }
}
