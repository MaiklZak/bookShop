package com.example.MyBookShopApp.data.repositories;

import com.example.MyBookShopApp.data.model.Tag;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    @EntityGraph(value = "tag.books", type = EntityGraph.EntityGraphType.FETCH)
    List<Tag> findAll();
}
