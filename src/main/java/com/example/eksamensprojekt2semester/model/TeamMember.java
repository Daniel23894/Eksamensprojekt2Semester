package com.example.eksamensprojekt2semester.model;

import java.math.BigDecimal;

public class TeamMember {
    private int memberId;
    private String name;
    private String email;
    private Role role;
    private BigDecimal hoursPerDay; /** Amount of hours a person can work per day **/
    private String password;

    /** No-args constructor **/
    public TeamMember() {
    }

    /** Constructor with all fields except id, it will be auto-generated **/
    public TeamMember(String name, String email, Role role, BigDecimal hoursPerDay, String password) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.hoursPerDay = hoursPerDay;
        this.password = password;

    }

    /** Getter methods **/
    public int getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public BigDecimal getHoursPerDay() {
        return hoursPerDay;
    }

    public String getPassword() {
        return password;
    }

    /** Setter methods with validation **/
    public void setMemberId(int memberId) {
        if (memberId < 0) {
            throw new IllegalArgumentException("Member ID cannot be negative.");
        }
        this.memberId = memberId;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if (name.length() < 3) {
            throw new IllegalArgumentException("Name must be at least 3 characters long.");
        }
        this.name = name;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Email must be valid.");
        }
        this.email = email;
    }

    public void setRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be empty.");
        }
        this.role = role;
    }

    public void setHoursPerDay(BigDecimal hoursPerDay) {
        if (hoursPerDay == null || hoursPerDay.compareTo(BigDecimal.ZERO) < 0 || hoursPerDay.compareTo(BigDecimal.valueOf(24)) > 0) {
            throw new IllegalArgumentException("Hours per day must be between 0 and 24.");
        }
        this.hoursPerDay = hoursPerDay;
    }

    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long.");
        }
        // Optional: check for spaces inside password (you can remove if you want)
        if (password.contains(" ")) {
            throw new IllegalArgumentException("Password cannot contain spaces.");
        }
        this.password = password;
    }


    @Override
    public String toString() {
        return "TeamMember{" +
                "memberId=" + memberId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", hoursPerDay=" + hoursPerDay +
                '}';
    }
}
