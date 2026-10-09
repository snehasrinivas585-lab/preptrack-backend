package com.preptrack.backend.dto;

import com.preptrack.backend.entity.Student;

public class StudentResponse {

    private Long id;
    private String name;
    private String email;
    private String college;
    private String branch;
    private String role;

    public StudentResponse() {
    }

    public StudentResponse(
            Long id,
            String name,
            String email,
            String college,
            String branch,
            String role) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.college = college;
        this.branch = branch;
        this.role = role;
    }

    public static StudentResponse fromStudent(Student student) {

        return new StudentResponse(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getCollege(),
                student.getBranch(),
                student.getRole()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
