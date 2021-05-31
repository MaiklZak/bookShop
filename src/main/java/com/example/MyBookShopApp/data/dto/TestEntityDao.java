package com.example.MyBookShopApp.data.dto;

import com.example.MyBookShopApp.data.repositories.AbstractHibernateDao;
import com.example.MyBookShopApp.data.model.TestEntity;
import org.springframework.stereotype.Repository;

@Repository
public class TestEntityDao extends AbstractHibernateDao<TestEntity> {

    public TestEntityDao() {
        super();
        setClazz(TestEntity.class);
    }
}
