package com.nexa.hr_management.services;

import com.nexa.hr_management.dto.DepartmentEmployeeCount;
import com.nexa.hr_management.exception.EmployeeAlreadyExistsException;
import com.nexa.hr_management.model.Employee;
import com.nexa.hr_management.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public long getEmployeeCount() {
        long count = employeeRepository.count();
        System.out.println("DEBUG: Employee count retrieved: " + count);  // Debugging line
        return count;
    }

    @Override
    public List<Employee> getAllEmployees() {
        System.out.println("DEBUG: Fetching all employees");  // Debugging line
        return employeeRepository.findAll();
    }

    @Override
    public List<Employee> searchEmployees(String searchTerm) {
        System.out.println("DEBUG: Searching employees with search term: " + searchTerm);  // Debugging line
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllEmployees();
        }
        return employeeRepository.searchEmployees(searchTerm.trim().toLowerCase());
    }

    @Override
    public boolean deleteEmployee(int employeeId) {
        try {
            System.out.println("DEBUG: Attempting to delete employee with ID: " + employeeId);  // Debugging line
            if (employeeRepository.existsById(employeeId)) {
                employeeRepository.deleteById(employeeId);
                System.out.println("DEBUG: Employee with ID " + employeeId + " deleted successfully.");  // Debugging line
                return true;
            }
            System.out.println("DEBUG: Employee with ID " + employeeId + " not found.");  // Debugging line
            return false;
        } catch (Exception e) {
            System.out.println("DEBUG: Error deleting employee with ID " + employeeId);  // Debugging line
            throw new RuntimeException("Failed to delete employee with ID: " + employeeId, e);
        }
    }

    @Override
    public Optional<Employee> getEmployeeById(int employeeId) {
        System.out.println("DEBUG: Fetching employee with ID: " + employeeId);  // Debugging line
        return employeeRepository.findById(employeeId);
    }

    @Override
    public Map<String, Long> getAllDepartmentCounts() {
        System.out.println("DEBUG: Fetching department counts");  // Debugging line
        // Get all employees
        List<Employee> employees = employeeRepository.findAll();

        // Group by department and count
        Map<String, Long> departmentCounts = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.counting()
                ));

        System.out.println("DEBUG: Department counts: " + departmentCounts);  // Debugging line
        return departmentCounts;
    }

    @Override
    @Transactional
    public Employee createEmployee(Employee employee) {
        System.out.println("DEBUG: Creating employee with email: " + employee.getEmail());  // Debugging line

        // 1. Check if employee already exists by email
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            System.out.println("DEBUG: Employee with email " + employee.getEmail() + " already exists.");  // Debugging line
            throw new EmployeeAlreadyExistsException("An employee with email " + employee.getEmail() + " already exists.");
        }



        // 3. Save employee to DB
        Employee savedEmployee = employeeRepository.save(employee);
        System.out.println("DEBUG: Employee created successfully: " + savedEmployee.getName());  // Debugging line
        return savedEmployee;
    }

    @Override
    public boolean existsByEmail(String email) {
        System.out.println("DEBUG: Checking if employee with email exists: " + email);  // Debugging line
        return employeeRepository.existsByEmail(email);
    }

    @Override
    public Optional<Employee> findByEmail(String email) {
        System.out.println("DEBUG: Fetching employee with email: " + email);  // Debugging line
        return employeeRepository.findByEmail(email);
    }

    @Override
    public List<DepartmentEmployeeCount> getEmployeeCountByDepartment() {
        System.out.println("DEBUG: Fetching employee count by department");  // Debugging line
        return employeeRepository.countEmployeesByDepartment();
    }

    @Override
    public long getDistinctDepartmentCount() {
        System.out.println("DEBUG: Fetching count of distinct departments");  // Debugging line
        return employeeRepository.countDistinctDepartments();
    }

    @Override
    public void updateEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    @Override
    public void save(Employee employee) {
        employeeRepository.save(employee); // This method is used to save the employee
    }

}
