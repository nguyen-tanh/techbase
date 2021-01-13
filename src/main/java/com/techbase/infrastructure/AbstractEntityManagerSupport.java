package com.techbase.infrastructure;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;

/**
 * @author nguyentanh
 */
public abstract class AbstractEntityManagerSupport {

    private EntityManager entityManager;

    @PersistenceContext
    public void setSessionFactory(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    protected EntityManager entityManager() {
        return entityManager;
    }

    protected CriteriaBuilder criteriaBuilder() {
        return entityManager.getCriteriaBuilder();
    }

}
