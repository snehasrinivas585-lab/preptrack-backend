package com.preptrack.backend.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.preptrack.backend.entity.TestAttempt;
import com.preptrack.backend.repository.TestAttemptRepository;

@RestController
@RequestMapping("/attempts")
@CrossOrigin(origins = "http://localhost:5173")
public class TestAttemptController {

    private final TestAttemptRepository testAttemptRepository;

    public TestAttemptController(
            TestAttemptRepository testAttemptRepository) {

        this.testAttemptRepository = testAttemptRepository;
    }

    @PostMapping
    public TestAttempt saveAttempt(
            @RequestBody TestAttempt attempt) {

        attempt.setAttemptedAt(LocalDateTime.now());

        return testAttemptRepository.save(attempt);
    }

    @GetMapping
    public List<TestAttempt> getAllAttempts() {

        return testAttemptRepository.findAll();
    }

    @GetMapping("/student/{email}")
    public List<TestAttempt> getStudentAttempts(
            @PathVariable String email) {

        return testAttemptRepository
                .findByStudentEmail(email);
    }
}
