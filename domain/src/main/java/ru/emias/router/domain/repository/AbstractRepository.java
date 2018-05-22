package ru.emias.router.domain.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;

public abstract class AbstractRepository {

    @PersistenceContext
    protected EntityManager em;

    protected Session getHibernateSession() {
        return (Session) em.getDelegate();
    }

}
