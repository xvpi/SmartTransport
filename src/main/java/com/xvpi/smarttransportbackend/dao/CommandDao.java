package com.xvpi.smarttransportbackend.dao;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

@Repository
public class CommandDao {
    @PersistenceContext
    private EntityManager entityManager;


    public int countAllCommands(LocalDateTime getTime) {
        LocalDateTime startOfDay = getTime.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        String jpql = "SELECT COUNT(c) FROM AICommand c WHERE c.generateTime >= :start AND c.generateTime < :end";
        Long result = (Long) entityManager.createQuery(jpql)
                .setParameter("start", startOfDay)
                .setParameter("end", endOfDay)
                .getSingleResult();
        return result.intValue();
    }

    public int countUnfinishedCommands(LocalDateTime getTime) {
        LocalDateTime startOfDay = getTime.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        String jpql = "SELECT COUNT(c) FROM AICommand c WHERE c.generateTime >= :start AND c.generateTime < :end AND " +
                "c.status = false";
        Long result = (Long) entityManager.createQuery(jpql)
                .setParameter("start", startOfDay)
                .setParameter("end", endOfDay)
                .getSingleResult();
        return result.intValue();
    }

}
