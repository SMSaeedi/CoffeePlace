package com.example.demo.controller;

import com.example.demo.service.ReportService;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

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
    List<String> mostUsedToppings() {
        return reportService.mostUsedToppings();
    }
}