package com.nexa.hr_management.dto;

public class DepartmentEmployeeCount {
    private String departmentName;
    private long employeeCount;

    public DepartmentEmployeeCount(String departmentName, long employeeCount) {
        this.departmentName = departmentName;
        this.employeeCount = employeeCount;
    }

    // Getters AND setters
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public long getEmployeeCount() { return employeeCount; }
    public void setEmployeeCount(long employeeCount) { this.employeeCount = employeeCount; }
}
