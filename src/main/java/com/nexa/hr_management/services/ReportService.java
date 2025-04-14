package com.nexa.hr_management.services;

import com.nexa.hr_management.model.Employee;
import com.nexa.hr_management.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public ReportService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Map<String, Object> generateEmployeeReport() {
        Map<String, Object> reportData = new HashMap<>();
        List<Employee> employees = employeeRepository.findAll();

        // Calculate basic totals
        reportData.put("totalEmployees", employees.size());

        // Group by department
        Map<String, Long> departmentCounts = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.counting()
                ));
        reportData.put("departmentDistribution", departmentCounts);

        // Group by role
        Map<String, Long> roleCounts = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getRole,
                        Collectors.counting()
                ));
        reportData.put("roleDistribution", roleCounts);

        // Calculate department salary stats
        Map<String, DoubleSummaryStatistics> departmentSalaryStats = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.summarizingDouble(Employee::getSalary)
                ));

        Map<String, Map<String, Double>> departmentSalaries = new HashMap<>();
        departmentSalaryStats.forEach((dept, stats) -> {
            Map<String, Double> salaryData = new HashMap<>();
            salaryData.put("average", stats.getAverage());
            salaryData.put("min", stats.getMin());
            salaryData.put("max", stats.getMax());
            departmentSalaries.put(dept, salaryData);
        });

        reportData.put("departmentSalaries", departmentSalaries);

        // Add timestamp
        reportData.put("generatedAt", new Date());

        return reportData;
    }
}