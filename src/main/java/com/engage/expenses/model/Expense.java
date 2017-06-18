package com.engage.expenses.model;

import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
public class Expense {

    private Long id;
    private LocalDate date;
    private BigDecimal amount;
    private String reason;

}
