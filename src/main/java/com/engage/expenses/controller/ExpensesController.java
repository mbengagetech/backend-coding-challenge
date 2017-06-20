package com.engage.expenses.controller;

import com.engage.expenses.api.Path;
import com.engage.expenses.service.ExpensesService;
import com.engage.expenses.model.Expense;
import com.engage.expenses.repository.HibernateExpensesRepository;
import com.engage.shared.JsonMapper;

import static com.engage.expenses.Main.APPLICATION_JSON;
import static spark.Spark.get;
import static spark.Spark.post;

public class ExpensesController {

    public ExpensesController(JsonMapper jsonMapper, HibernateExpensesRepository repository, ExpensesService service) {

        get(Path.EXPENSES, APPLICATION_JSON, (req, res) -> repository.findAll(), jsonMapper::toJson);

        post(Path.EXPENSES, APPLICATION_JSON, (req, res) -> {
            Expense expenseDto = jsonMapper.fromJson(req.body(), Expense.class);
            return service.addExpense(expenseDto);
        }, jsonMapper::toJson);
    }


}
