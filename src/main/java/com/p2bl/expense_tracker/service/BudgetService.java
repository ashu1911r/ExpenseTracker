package com.p2bl.expense_tracker.service;

import com.p2bl.expense_tracker.model.Budget;
import com.p2bl.expense_tracker.repository.BudgetRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BudgetService {
    private final BudgetRepository repository;

    public BudgetService(BudgetRepository repository) {
        this.repository = repository;
    }

    public List<Budget> findAll() {
        return repository.findAll();
    }

    public Budget findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Budget not found: " + id));
    }

    public Budget findForMonth(int year, int month) {
        return repository.findByBudgetYearAndBudgetMonth(year, month).orElse(null);
    }

    public Budget save(Budget budget) {
        return repository.save(budget);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public String monthName(int month) {
        return java.time.Month.of(month).toString().charAt(0) +
                java.time.Month.of(month).toString().substring(1).toLowerCase();
    }

    public BigDecimal remaining(Budget budget, BigDecimal spent) {
        if (budget == null) return BigDecimal.ZERO.subtract(spent);
        return budget.getAmount().subtract(spent);
    }
}
