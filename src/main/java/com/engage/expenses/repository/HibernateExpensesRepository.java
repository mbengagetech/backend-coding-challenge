package com.engage.expenses.repository;

import com.engage.expenses.model.Expense;

import java.util.List;

import static com.engage.shared.Hibernate.fetchWithSession;
import static com.engage.shared.Hibernate.runWithSession;

public class HibernateExpensesRepository implements ExpensesRepository {

    @Override
    public List<Expense> findAll() {
        return fetchWithSession(session -> (List<Expense>) session.createQuery("from Expense").getResultList());
    }

    @Override
    public void saveExpense(Expense expense) {
        runWithSession(session -> session.save(expense));
    }
}
