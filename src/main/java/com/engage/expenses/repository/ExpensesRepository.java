package com.engage.expenses.repository;

import com.engage.expenses.model.Expense;

import java.util.List;

import static com.engage.shared.Hibernate.fetchWithSession;
import static com.engage.shared.Hibernate.runWithSession;

public class ExpensesRepository {

    public List<Expense> findAll() {
        return null;

    public void saveExpense(Expense expense) {
        runWithSession(session -> session.save(expense));
    }
}
