package com.engage.expenses.repository;

import com.engage.expenses.model.Expense;

import java.util.List;

public interface ExpensesRepository {

    List<Expense> findAll();

    void saveExpense(Expense expense);
}
