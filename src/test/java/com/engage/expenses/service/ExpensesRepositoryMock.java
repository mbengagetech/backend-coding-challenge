package com.engage.expenses.service;

import com.engage.expenses.model.Expense;
import com.engage.expenses.repository.ExpensesRepository;

import java.util.List;

public class ExpensesRepositoryMock implements ExpensesRepository {

    private Expense savedExpense;

    @Override
    public List<Expense> findAll() {
        return null;
    }

    @Override
    public void saveExpense(Expense expense) {
        savedExpense = expense;
    }

    public boolean hasSaved(Expense expense) {
        return expense.equals(savedExpense);
    }

    public boolean hasBeenCalled() {
        return savedExpense != null;
    }
}
