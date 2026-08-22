package com.p2bl.expense_tracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "monthly_budget",
       uniqueConstraints = @UniqueConstraint(columnNames = {"budget_year", "budget_month"}))
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(2000)
    @Column(name = "budget_year", nullable = false)
    private Integer budgetYear;

    @NotNull
    @Min(1)
    @Max(12)
    @Column(name = "budget_month", nullable = false)
    private Integer budgetMonth;

    @NotNull(message = "Budget amount is required")
    @DecimalMin(value = "0.01", message = "Budget must be greater than 0")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    public Budget() {}

    public Budget(Integer budgetYear, Integer budgetMonth, BigDecimal amount) {
        this.budgetYear = budgetYear;
        this.budgetMonth = budgetMonth;
        this.amount = amount;
    }

    public Long getId() { return id; }
    public Integer getBudgetYear() { return budgetYear; }
    public Integer getBudgetMonth() { return budgetMonth; }
    public BigDecimal getAmount() { return amount; }
    public void setId(Long id) { this.id = id; }
    public void setBudgetYear(Integer budgetYear) { this.budgetYear = budgetYear; }
    public void setBudgetMonth(Integer budgetMonth) { this.budgetMonth = budgetMonth; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
