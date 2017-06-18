package com.engage.expenses;

import com.engage.expenses.model.Expense;
import com.engage.shared.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static spark.Spark.*;

public class Main {

    public static final String APPLICATION_JSON = "application/json";

    public static void main(String[] args) throws JsonProcessingException {

        JsonMapper jsonMapper = new JsonMapper();

        staticFileLocation("static");
        port(8080);

        List<Expense> expenses = newArrayList(new Expense(1L, LocalDate.now(), BigDecimal.valueOf(10.1), "Why not?"));

        get("/app/expenses", APPLICATION_JSON, (req, res) -> expenses, jsonMapper::toJson);

        after((req, res) -> {
            res.type("application/json");
        });
    }

}
