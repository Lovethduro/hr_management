package com.nexa.hr_management.controllers;

import com.nexa.hr_management.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@PreAuthorize("hasRole('ADMIN')")
public class ReportApiController {

    private final ReportService reportService;

    @Autowired
    public ReportApiController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/employee")
    public ResponseEntity<Map<String, Object>> getEmployeeReportData() {
        Map<String, Object> reportData = reportService.generateEmployeeReport();
        return ResponseEntity.ok(reportData);
    }

    // Add more report endpoints as needed
}