"use strict";

/******************************************************************************************

Expenses controller

******************************************************************************************/

var app = angular.module("expenses.controller", []);

app.controller("ctrlExpenses", ["$rootScope", "$scope", "config", "restalchemy", function ExpensesCtrl($rootScope, $scope, $config, $restalchemy) {
    // Update the headings
    $rootScope.mainTitle = "Expenses";
    $rootScope.mainHeading = "Expenses";

    // Update the tab sections
    $rootScope.selectTabSection("expenses", 0);

    var restExpenses = $restalchemy.init({ root: $config.apiroot }).at("expenses");

    $scope.dateOptions = {
        changeMonth: true,
        changeYear: true,
        dateFormat: "dd/mm/yy"
    };

    var loadExpenses = function() {
        // Retrieve a list of expenses via REST
        restExpenses.get().then(function(expenses) {
            $scope.expenses = expenses;
        });
    };

    var parseAmountWithCurrency = function() {
        if (typeof $scope.newExpense.amount === undefined) {
            return {};
        } else {
            var parsed = $scope.newExpense.amount.trim().match(/\S+/g) || [];
            return {
                amount: parseFloat(parsed[0]),
                currency: parsed[1] || "GBP"
            };
        }
    };

    $scope.saveExpense = function() {
        if ($scope.expensesform.$valid) {
            // Post the expense via REST
            restExpenses.post(_.merge($scope.newExpense, parseAmountWithCurrency())).then(function() {
                // Reload new expenses list
                loadExpenses();
                $scope.newExpense = {};
            });
        }
    };

    $scope.clearExpense = function() {
        $scope.newExpense = {};
    };

    // Initialise scope variables
    loadExpenses();
    $scope.clearExpense();
}]);
