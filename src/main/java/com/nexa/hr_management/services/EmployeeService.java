package com.nexa.hr_management.services;

import com.nexa.hr_management.dto.DepartmentEmployeeCount;
import com.nexa.hr_management.model.Employee;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface EmployeeService {
    List<Employee> getAllEmployees();

    List<Employee> searchEmployees(String searchTerm);

    boolean deleteEmployee(int employeeId);

    Optional<Employee> getEmployeeById(int employeeId); // Added this method

    long getEmployeeCount();

    Map<String, Long> getAllDepartmentCounts();

    boolean existsByEmail(String email);

    Optional<Employee> findByEmail(String email);

    Employee createEmployee(Employee employee);

    List<DepartmentEmployeeCount> getEmployeeCountByDepartment(); // This method should return a List<DepartmentEmployeeCount>

    long getDistinctDepartmentCount();

    void updateEmployee(Employee employee); // Add this

    void save(Employee employee);
}