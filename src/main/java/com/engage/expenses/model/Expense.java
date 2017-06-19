package com.engage.expenses.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "expenses")
@EqualsAndHashCode(of = "id")
public class Expense {

    static final String DATE_EMPTY = "Date can't be empty";
    static final String DATE_IN_FUTURE = "Date can't be in the future";
    static final String AMOUNT_EMPTY = "Amount can't be empty";
    static final String AMOUNT_ZERO_OR_NEGATIVE = "Amount can't be zero or negative";
    static final String REASON_EMPTY = "Reason can't be empty";

    @Id
    @SequenceGenerator(allocationSize = 1, sequenceName = "expenses_id_seq", name = "expenses_id_seq")
    @GeneratedValue(generator = "expenses_id_seq", strategy = GenerationType.SEQUENCE)
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d/M/yyyy")
    private LocalDate date;
    private BigDecimal amount;
    private String reason;

    public Expense(LocalDate date, BigDecimal amount, String reason) {
        this.date = date;
        this.amount = amount;
        this.reason = reason;
    }

    public Validation<Seq<String>, Expense> validate() {
        return validateDate(date)
                .combine(validateAmount(amount))
                .combine(validateReason(reason)).ap(Expense::new);
    }

    private Validation<String, LocalDate> validateDate(LocalDate date) {
        if (date == null) {
            return Validation.invalid(DATE_EMPTY);
        } else if (date.isAfter(LocalDate.now())) {
            return Validation.invalid(DATE_IN_FUTURE);
        } else {
            return Validation.valid(date);
        }
    }

    private Validation<String, BigDecimal> validateAmount(BigDecimal amount) {
        if (amount == null) {
            return Validation.invalid(AMOUNT_EMPTY);
        } else if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return Validation.invalid(AMOUNT_ZERO_OR_NEGATIVE);
        } else {
            return Validation.valid(amount);
        }
    }

    private Validation<String, String> validateReason(String reason) {
        if (StringUtils.isEmpty(reason)) {
            return Validation.invalid(REASON_EMPTY);
        } else {
            return Validation.valid(reason);
        }
    }
}
