package com.p2bl.expense_tracker.service;

import com.p2bl.expense_tracker.model.Expense;
import com.p2bl.expense_tracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    private final ExpenseRepository repository;

    public ExpenseService(ExpenseRepository repository) {
        this.repository = repository;
    }

    public List<Expense> findAll() {
        return repository.findAllByOrderByExpenseDateDescIdDesc();
    }

    public List<Expense> findForMonth(int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        return repository.findByExpenseDateBetweenOrderByExpenseDateDescIdDesc(start, start.withDayOfMonth(start.lengthOfMonth()));
    }

    public Expense findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Expense not found: " + id));
    }

    public Expense save(Expense expense) {
        return repository.save(expense);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public BigDecimal total(List<Expense> expenses) {
        return expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<String, BigDecimal> totalsByCategory(List<Expense> expenses) {
        return expenses.stream().collect(Collectors.groupingBy(
                Expense::getCategory,
                Collectors.mapping(Expense::getAmount,
                        Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
        ));
    }
}
