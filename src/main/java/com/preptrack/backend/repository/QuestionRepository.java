package com.preptrack.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.preptrack.backend.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByCategory(String category);
}
