package com.engage.expenses.integration;

import com.engage.expenses.Main;
import com.engage.expenses.api.Path;
import com.engage.expenses.controller.ExpensesController;
import com.engage.expenses.model.Expense;
import com.engage.expenses.repository.ExpensesRepository;
import com.engage.shared.JsonMapper;
import com.google.common.collect.ImmutableMap;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.engage.expenses.Main.PORT;
import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static spark.Spark.port;

public class ExpensesIntegrationTest {

    @BeforeClass
    public static void setUp() {
        JsonMapper jsonMapper = new JsonMapper();
        port(PORT);

        new Main(
                new ExpensesController(
                        jsonMapper,
                        new ExpensesRepositoryStub()
                )
        );
    }

    @AfterClass
    public static void tearDown() {
        Spark.stop();
    }

    @Test
    public void shouldGetExpenses() {
        try {
            HttpResponse<JsonNode> res = Unirest.get("http://localhost:" + PORT + Path.EXPENSES).asJson();

            assertThat(res.getStatus()).isEqualTo(200);
            assertThat(res.getBody().isArray()).isTrue();
            JSONArray expensesJson = res.getBody().getArray();
            assertThat(expensesJson.length()).isEqualTo(1);

            JSONObject expected = new JSONObject(ImmutableMap.builder()
                    .put("id", 1)
                    .put("date", "2017-06-18")
                    .put("amount", 10.1)
                    .put("reason", "Why not?").build());

            JSONObject actual = expensesJson.getJSONObject(0);

            assertThat(actual.similar(expected)).isTrue();

        } catch (UnirestException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    private static class ExpensesRepositoryStub extends ExpensesRepository {
        @Override
        public List<Expense> findAll() {
            return newArrayList(new Expense(1L, LocalDate.now(), BigDecimal.valueOf(10.1), "Why not?"));
        }
    }
}
