package com.example.demo.controller;

import com.example.demo.enums.ProductType;
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
    BigDecimal getTotalAmountPerCustomer(@RequestHeader int token) {
        return reportService.getTotalAmountPerCustomer(token);
    }

    @GetMapping("/topToppings")
    List<ProductType> mostUsedToppings() {
        return reportService.mostUsedToppings();
    }
}