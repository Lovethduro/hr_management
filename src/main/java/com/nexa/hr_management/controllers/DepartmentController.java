package com.nexa.hr_management.controllers;

import com.nexa.hr_management.dto.DepartmentEmployeeCount;
import com.nexa.hr_management.repository.EmployeeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/departments")  // Base path for all department-related endpoints
public class DepartmentController {

    private final EmployeeRepository employeeRepository;

    public DepartmentController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping
    public String viewDepartments(Model model) {
        try {
            // Get department statistics
            long departmentCount = employeeRepository.countDistinctDepartments();
            List<DepartmentEmployeeCount> employeesByDepartment = employeeRepository.countEmployeesByDepartment();

            // Add data to model
            model.addAttribute("departmentCount", departmentCount);
            model.addAttribute("employeesByDepartment", employeesByDepartment);

            // Log for debugging
            System.out.println("Department Count: " + departmentCount);
            employeesByDepartment.forEach(dept ->
                    System.out.println(dept.getDepartmentName() + ": " + dept.getEmployeeCount()));

            return "pages/admin"; // Template path
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error loading department data");
            return "error";
        }
    }
}