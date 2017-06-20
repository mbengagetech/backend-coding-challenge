package com.engage.expenses.model;

import com.engage.expenses.resource.CurrencyResource;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Validation;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "expenses")
@EqualsAndHashCode(of = "id")
public class Expense {

    static final String DATE_EMPTY = "Date can't be empty";
    static final String DATE_IN_FUTURE = "Date can't be in the future";
    static final String AMOUNT_EMPTY = "Amount can't be empty";
    static final String AMOUNT_ZERO_OR_NEGATIVE = "Amount can't be zero or negative";
    static final String REASON_EMPTY = "Reason can't be empty";
    static final String CURRENCY_EMPTY = "Currency can't be empty";
    public static final Currency DEFAULT_CURRENCY = Currency.GBP;

    @Id
    @SequenceGenerator(allocationSize = 1, sequenceName = "expenses_id_seq", name = "expenses_id_seq")
    @GeneratedValue(generator = "expenses_id_seq", strategy = GenerationType.SEQUENCE)
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d/M/yyyy")
    private LocalDate date;
    @Getter
    private BigDecimal amount;
    private String reason;
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Enumerated(EnumType.STRING)
    private Currency currency;

    public Expense(LocalDate date, BigDecimal amount, String reason, Currency currency) {
        this.date = date;
        this.amount = amount;
        this.reason = reason;
        this.currency = currency;
    }

    public BigDecimal getVat() {
        return Vat.vatFromTotal(amount);
    }

    public Validation<Seq<String>, Expense> validate() {
        return validateDate(date)
                .combine(validateAmount(amount))
                .combine(validateReason(reason))
                .combine(validateCurrency(currency)).ap(Expense::new);
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

    private Validation<String, Currency> validateCurrency(Currency currency) {
        if (currency == null) {
            return Validation.invalid(CURRENCY_EMPTY);
        } else {
            return Validation.valid(currency);
        }
    }

    public Either<Seq<String>, Expense> convertToDefaultCurrency(CurrencyResource currencyResource) {
        if (DEFAULT_CURRENCY.equals(currency)) {
            return Either.right(this);
        } else {
            Either<String, BigDecimal> result = currencyResource.convertCurrency(date, amount, currency, DEFAULT_CURRENCY);
            if (result.isRight()) {
                amount = result.get();
                currency = DEFAULT_CURRENCY;
                return Either.right(this);
            } else {
                return Either.left(List.of(result.getLeft()));
            }
        }
    }
}
