package com.preptrack.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.preptrack.backend.entity.Resource;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

    List<Resource> findByCategory(String category);
}