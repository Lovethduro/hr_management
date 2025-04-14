package com.nexa.hr_management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Or AUTO, depending on your DB setup
    @Column(name = "emp_id")
    private Long emp_id;

    @Column(nullable = false)
    @NotNull(message = "First name is required")
    private String firstName;

    @Column(nullable = false)
    @NotNull(message = "Last name is required")
    private String lastName;

    @Column(nullable = false)
    @NotNull(message = "Email is required")
    private String email;

    @Column(nullable = false)
    @NotNull(message = "Position is required")
    private String position;

    @Column(nullable = false)
    @NotNull(message = "Department is required")
    private String department;

    @Column(nullable = false)
    @NotNull(message = "Postal address is required")
    private String postalAddress;

    @Column(nullable = false)
    @NotNull(message = "Qualification is required")
    private String qualification;

    @Column(nullable = false)
    @NotNull(message = "Role is required")
    private String role;

    @Column(nullable = false)
    @NotNull(message = "Salary is required")
    private Double salary;

    @Column(nullable = false)
    @NotNull(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phone; // Adding phone field with validation

    // Convenience method to get full name
    @Transient
    public String getName() {
        return firstName + " " + lastName;
    }

    // Add this method to your Employee model class
    public void setAddress(String address) {
        this.postalAddress = address;
    }

    // And this getter for form binding
    public String getAddress() {
        return this.postalAddress;
    }
}
