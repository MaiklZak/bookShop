package com.example.MyBookShopApp.data.reposirories;

import com.example.MyBookShopApp.data.model.TestEntity;
import org.springframework.data.repository.CrudRepository;

public interface TestEntityCrudRepository extends CrudRepository<TestEntity, Long> {
}
