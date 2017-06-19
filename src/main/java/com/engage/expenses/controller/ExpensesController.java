package com.engage.expenses.controller;

import com.engage.expenses.api.Path;
import com.engage.expenses.model.Expense;
import com.engage.expenses.repository.ExpensesRepository;
import com.engage.shared.JsonMapper;
import com.engage.shared.exception.ValidationException;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

import static com.engage.expenses.Main.APPLICATION_JSON;
import static spark.Spark.get;
import static spark.Spark.post;

public class ExpensesController {

    public ExpensesController(JsonMapper jsonMapper, ExpensesRepository repository) {

        get(Path.EXPENSES, APPLICATION_JSON, (req, res) -> repository.findAll(), jsonMapper::toJson);

        post(Path.EXPENSES, APPLICATION_JSON, (req, res) -> {
            Expense expenseDto = jsonMapper.fromJson(req.body(), Expense.class);
            Validation<Seq<String>, Expense> validatedExpense = expenseDto.validate();

            Validation<Seq<String>, Expense> result = validatedExpense.map(expense -> {
                repository.saveExpense(expense);
                return expense;
            });

            if (result.isValid()) {
                return result.get();
            } else {
                throw new ValidationException(result.getError());
            }
        }, jsonMapper::toJson);
    }


}
