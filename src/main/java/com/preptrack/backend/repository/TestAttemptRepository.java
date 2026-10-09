package com.preptrack.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.preptrack.backend.entity.TestAttempt;

public interface TestAttemptRepository
        extends JpaRepository<TestAttempt, Long> {

    List<TestAttempt> findByStudentEmail(String studentEmail);
}