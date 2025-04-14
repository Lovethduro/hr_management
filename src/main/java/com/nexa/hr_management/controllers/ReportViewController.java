package com.nexa.hr_management.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/reports")
@PreAuthorize("hasRole('ADMIN')")
public class ReportViewController {

    @GetMapping
    public String showReportsPage() {
        return "pages/reports";
    }

    @GetMapping("/employee")
    public String showEmployeeReport() {
        return "pages/employee-report";
    }

    // More report view endpoints as needed
}