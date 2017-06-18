package com.engage.expenses.controller;

import com.engage.expenses.api.Path;
import com.engage.expenses.repository.ExpensesRepository;
import com.engage.shared.JsonMapper;

import static com.engage.expenses.Main.APPLICATION_JSON;
import static spark.Spark.get;

public class ExpensesController {

    public ExpensesController(JsonMapper jsonMapper, ExpensesRepository repository) {
        ExpensesRepository repository1 = repository;

        get(Path.EXPENSES, APPLICATION_JSON, (req, res) -> repository.findAll(), jsonMapper::toJson);

    }


}
