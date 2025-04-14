package com.nexa.hr_management.controllers;

import com.nexa.hr_management.model.Employee;
import com.nexa.hr_management.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@Controller
public class EmployeeController {

    private static final Logger logger = Logger.getLogger(EmployeeController.class.getName());
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/displayEmployee")
    public String displayEmployees(Model model,
                                   @RequestParam(required = false) String search) {
        try {
            logger.info("Displaying employees with search term: " + (search != null ? search : "none"));
            List<Employee> employees = (search != null && !search.trim().isEmpty())
                    ? employeeService.searchEmployees(search)
                    : employeeService.getAllEmployees();

            model.addAttribute("employees", employees);
        } catch (Exception e) {
            logger.severe("Error retrieving employees: " + e.getMessage());
            model.addAttribute("errorMessage", "Error retrieving employees: " + e.getMessage());
        }
        return "pages/displayEmployee";
    }


    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteEmployee(@RequestParam("emp_id") int empId) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Call the service layer to delete the employee
            boolean isDeleted = employeeService.deleteEmployee(empId);

            if (isDeleted) {
                // Success response
                response.put("success", true);
                response.put("message", "Employee deleted successfully");
                return ResponseEntity.ok(response);
            } else {
                // If employee not found
                response.put("success", false);
                response.put("message", "Employee not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (IllegalArgumentException e) {
            // If there is an invalid argument or the employee doesn't exist
            response.put("success", false);
            response.put("message", "Invalid employee ID");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            // General error handling
            response.put("success", false);
            response.put("message", "Error deleting employee: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @PostMapping("/createEmployee")
    public String createEmployee(@ModelAttribute Employee employee,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        // Validate form data
        if (result.hasErrors()) {
            return "pages/createEmployee";
        }

        // Check if email already exists
        if (employeeService.existsByEmail(employee.getEmail())) {
            result.rejectValue("email", "error.employee", "Email already exists");
            return "pages/createEmployee";
        }

        try {
            // Create the employee
            Employee savedEmployee = employeeService.createEmployee(employee);

            // Add success message
            redirectAttributes.addFlashAttribute("successMessage",
                    "Employee " + savedEmployee.getName() + " created successfully!");

            // Redirect to employee list or dashboard
            return "redirect:/displayEmployee";
        } catch (Exception e) {
            // Add error message
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Failed to create employee: " + e.getMessage());
            return "pages/createEmployee";
        }
    }

    @GetMapping("/editEmployee/{id}")
    public String showEditEmployeeForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Employee> optionalEmployee = employeeService.getEmployeeById(Math.toIntExact(id));

        if (optionalEmployee.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Employee not found.");
            return "redirect:/displayEmployee";
        }

        model.addAttribute("employee", optionalEmployee.get());
        return "pages/editEmployees"; // Make sure you have this HTML under templates/pages/
    }
    @PostMapping("/updateEmployee")
    public String updateEmployee(@ModelAttribute("employee") Employee employee) {
        // Update the employee using the service layer
        employeeService.save(employee);

        // After saving the updated employee, redirect to the employee list or another page
        return "redirect:/displayEmployee";  // Redirect to the employee list page
    }


}
