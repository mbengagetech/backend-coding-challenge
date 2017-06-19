package com.engage.expenses.repository;

import com.engage.expenses.model.Expense;
import com.engage.shared.Hibernate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.engage.shared.Hibernate.runWithSession;
import static org.assertj.core.api.Assertions.assertThat;

public class ExpensesRepositoryTest {

    private ExpensesRepository repository;

    @Before
    public void setUp() {
        repository = new ExpensesRepository();
    }

    @After
    public void tearDown() {
        Hibernate.runNativeQuery("truncate expenses");
    }

    @Test
    public void shouldFindAllExpenses() {

        Expense expense = new Expense(LocalDate.now().minusDays(1), BigDecimal.valueOf(10.1), "Reason 1");
        Expense anotherExpense = new Expense(LocalDate.now(), BigDecimal.valueOf(20.5), "Reason 2");

        runWithSession(session -> {
            session.persist(expense);
            session.persist(anotherExpense);
        });

        List<Expense> expected = repository.findAll();

        assertThat(expected).containsExactlyInAnyOrder(expense, anotherExpense);
    }
}