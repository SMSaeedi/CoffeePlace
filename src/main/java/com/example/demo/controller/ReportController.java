package com.example.demo.controller;

import com.example.demo.service.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/totalAmount")
    BigDecimal getTotalAmountPerCustomer(@RequestHeader int customerId) {
        return reportService.getTotalAmountPerCustomer(customerId);
    }

    @GetMapping("/topToppings")
    Map<String, Integer> mostUsedToppings() {
        return reportService.mostUsedToppings();
    }
}