package ru.javawebinar.topjava.repository

import org.hibernate.Session
import org.springframework.stereotype.Component
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Component
class JpaUtil {

    @PersistenceContext
    private lateinit var em: EntityManager

    fun clear2ndLevelHibernateCache() {
        val session: Session = em.delegate as Session
        session.sessionFactory.cache.evictAllRegions()
    }
}