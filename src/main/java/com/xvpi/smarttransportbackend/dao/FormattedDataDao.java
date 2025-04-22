package com.xvpi.smarttransportbackend.dao;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class FormattedDataDao {
    @PersistenceContext
    private EntityManager entityManager;
    public int countDistinctPlateNoByDate(String date) {
        String jpql = "SELECT COUNT(DISTINCT f.plateNo) FROM FormattedData f WHERE f.formattedTime LIKE :datePattern";
        return ((Number) entityManager.createQuery(jpql)
                .setParameter("datePattern", date + "%")
                .getSingleResult()).intValue();
    }

}
