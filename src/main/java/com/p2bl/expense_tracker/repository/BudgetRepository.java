package com.p2bl.expense_tracker.repository;

import com.p2bl.expense_tracker.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findByBudgetYearAndBudgetMonth(Integer budgetYear, Integer budgetMonth);
}
