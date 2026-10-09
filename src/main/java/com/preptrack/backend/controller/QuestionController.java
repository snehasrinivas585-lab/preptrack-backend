package com.preptrack.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.preptrack.backend.entity.Question;
import com.preptrack.backend.repository.QuestionRepository;

@RestController
@RequestMapping("/questions")
@CrossOrigin(origins = "http://localhost:5173")
public class QuestionController {

    private final QuestionRepository questionRepository;

    public QuestionController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @GetMapping
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @GetMapping("/{id}")
    public Question getQuestionById(@PathVariable Long id) {

        Optional<Question> question =
                questionRepository.findById(id);

        return question.orElse(null);
    }

    @GetMapping("/category/{category}")
    public List<Question> getQuestionsByCategory(
            @PathVariable String category) {

        return questionRepository.findByCategory(category);
    }

    @PostMapping
    public Question addQuestion(@RequestBody Question question) {

        return questionRepository.save(question);
    }

    @PutMapping("/{id}")
    public Question updateQuestion(
            @PathVariable Long id,
            @RequestBody Question updatedQuestion) {

        Optional<Question> existingQuestion =
                questionRepository.findById(id);

        if (existingQuestion.isPresent()) {

            Question question = existingQuestion.get();

            question.setQuestionText(
                    updatedQuestion.getQuestionText());

            question.setOptionA(
                    updatedQuestion.getOptionA());

            question.setOptionB(
                    updatedQuestion.getOptionB());

            question.setOptionC(
                    updatedQuestion.getOptionC());

            question.setOptionD(
                    updatedQuestion.getOptionD());

            question.setCorrectAnswer(
                    updatedQuestion.getCorrectAnswer());

            question.setCategory(
                    updatedQuestion.getCategory());

            return questionRepository.save(question);
        }

        return null;
    }

    @DeleteMapping("/{id}")
    public String deleteQuestion(@PathVariable Long id) {

        if (questionRepository.existsById(id)) {

            questionRepository.deleteById(id);

            return "Question deleted successfully";
        }

        return "Question not found";
    }
}

