package com.preptrack.backend.controller;

import com.preptrack.backend.dto.LoginRequest;
import com.preptrack.backend.dto.StudentResponse;
import com.preptrack.backend.entity.Student;
import com.preptrack.backend.repository.StudentRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "http://localhost:5173")
public class StudentController {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentController(
            StudentRepository studentRepository,
            PasswordEncoder passwordEncoder) {

        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }


    // ==============================
    // REGISTER
    // ==============================

    @PostMapping
    public ResponseEntity<StudentResponse> registerStudent(
            @RequestBody Student student) {

        Optional<Student> existingStudent =
                studentRepository.findByEmail(student.getEmail());

        if (existingStudent.isPresent()) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build();
        }

        /*
         * Convert plain password into BCrypt password
         * before saving into MySQL.
         */

        String encodedPassword =
                passwordEncoder.encode(student.getPassword());

        student.setPassword(encodedPassword);

        /*
         * New registrations are students
         * unless a role has explicitly been supplied.
         */

        if (student.getRole() == null ||
                student.getRole().isBlank()) {

            student.setRole("STUDENT");
        }

        Student savedStudent =
                studentRepository.save(student);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(StudentResponse.fromStudent(savedStudent));
    }


    // ==============================
    // GET ALL STUDENTS
    // ==============================

    @GetMapping
    public List<StudentResponse> getAllStudents() {

        return studentRepository
                .findAll()
                .stream()
                .map(StudentResponse::fromStudent)
                .toList();
    }


    // ==============================
    // LOGIN
    // ==============================

    @PostMapping("/login")
    public ResponseEntity<StudentResponse> login(
            @RequestBody LoginRequest loginRequest) {

        Optional<Student> optionalStudent =
                studentRepository.findByEmail(
                        loginRequest.getEmail()
                );

        if (optionalStudent.isEmpty()) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        Student student = optionalStudent.get();

        String storedPassword = student.getPassword();

        boolean passwordCorrect = false;

        /*
         * New users have BCrypt passwords.
         */

        if (storedPassword != null &&
                storedPassword.startsWith("$2")) {

            passwordCorrect =
                    passwordEncoder.matches(
                            loginRequest.getPassword(),
                            storedPassword
                    );

        } else {

            /*
             * Existing old users may still have
             * plain-text passwords.
             *
             * Verify them once and immediately
             * convert them to BCrypt.
             */

            if (storedPassword != null &&
                    storedPassword.equals(
                            loginRequest.getPassword()
                    )) {

                passwordCorrect = true;

                student.setPassword(
                        passwordEncoder.encode(
                                loginRequest.getPassword()
                        )
                );

                studentRepository.save(student);
            }
        }


        if (!passwordCorrect) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }


        /*
         * Never return the Student entity directly.
         * StudentResponse does not contain password.
         */

        return ResponseEntity.ok(
                StudentResponse.fromStudent(student)
        );
    }


    // ==============================
    // GET STUDENT BY EMAIL
    // ==============================

    @GetMapping("/email/{email}")
    public ResponseEntity<StudentResponse> getStudentByEmail(
            @PathVariable String email) {

        Optional<Student> student =
                studentRepository.findByEmail(email);

        if (student.isEmpty()) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        return ResponseEntity.ok(
                StudentResponse.fromStudent(
                        student.get()
                )
        );
    }


    // ==============================
    // DELETE STUDENT
    // ==============================

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(
            @PathVariable Long id) {

        Optional<Student> student =
                studentRepository.findById(id);

        if (student.isEmpty()) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Student not found");
        }

        /*
         * Protect the Admin account from deletion.
         */

        if ("ADMIN".equals(student.get().getRole())) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Admin account cannot be deleted");
        }

        studentRepository.deleteById(id);

        return ResponseEntity.ok(
                "Student deleted successfully"
        );
    }


    // ==============================
    // UPDATE STUDENT PROFILE
    // ==============================

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable Long id,
            @RequestBody Student updatedStudent) {

        Optional<Student> existingStudent =
                studentRepository.findById(id);

        if (existingStudent.isEmpty()) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        Student student =
                existingStudent.get();

        student.setName(
                updatedStudent.getName()
        );

        student.setEmail(
                updatedStudent.getEmail()
        );

        student.setCollege(
                updatedStudent.getCollege()
        );

        student.setBranch(
                updatedStudent.getBranch()
        );

        /*
         * Password and role are intentionally
         * NOT changed here.
         */

        Student savedStudent =
                studentRepository.save(student);

        return ResponseEntity.ok(
                StudentResponse.fromStudent(savedStudent)
        );
    }
}