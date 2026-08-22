package com.p2bl.expense_tracker.controller;

import com.p2bl.expense_tracker.model.Budget;
import com.p2bl.expense_tracker.model.Expense;
import com.p2bl.expense_tracker.service.BudgetService;
import com.p2bl.expense_tracker.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@Controller
public class ExpenseController {
    private final ExpenseService expenseService;
    private final BudgetService budgetService;

    public ExpenseController(ExpenseService expenseService, BudgetService budgetService) {
        this.expenseService = expenseService;
        this.budgetService = budgetService;
    }

    @GetMapping("/")
    public String dashboard(@RequestParam(required = false) Integer year,
                            @RequestParam(required = false) Integer month,
                            Model model) {
        LocalDate today = LocalDate.now();
        int selectedYear = year != null ? year : today.getYear();
        int selectedMonth = month != null ? month : today.getMonthValue();

        List<Expense> expenses = expenseService.findForMonth(selectedYear, selectedMonth);
        BigDecimal spent = expenseService.total(expenses);
        Budget budget = budgetService.findForMonth(selectedYear, selectedMonth);
        BigDecimal budgetAmount = budget != null ? budget.getAmount() : BigDecimal.ZERO;
        BigDecimal remaining = budgetAmount.subtract(spent);
        Map<String, BigDecimal> categoryTotals = expenseService.totalsByCategory(expenses);

        model.addAttribute("expenses", expenses);
        model.addAttribute("spent", spent);
        model.addAttribute("budget", budget);
        model.addAttribute("budgetAmount", budgetAmount);
        model.addAttribute("remaining", remaining);
        model.addAttribute("categoryTotals", categoryTotals);
        model.addAttribute("selectedYear", selectedYear);
        model.addAttribute("selectedMonth", selectedMonth);
        model.addAttribute("monthName", Month.of(selectedMonth).toString());
        model.addAttribute("yearMonth", YearMonth.of(selectedYear, selectedMonth));
        return "dashboard";
    }

    @GetMapping("/expenses")
    public String expenses(Model model) {
        model.addAttribute("expenses", expenseService.findAll());
        return "expenses";
    }

    @GetMapping("/expenses/new")
    public String newExpense(Model model) {
        Expense expense = new Expense();
        expense.setExpenseDate(LocalDate.now());
        model.addAttribute("expense", expense);
        model.addAttribute("pageTitle", "Add Expense");
        return "expense-form";
    }

    @GetMapping("/expenses/edit/{id}")
    public String editExpense(@PathVariable Long id, Model model) {
        model.addAttribute("expense", expenseService.findById(id));
        model.addAttribute("pageTitle", "Edit Expense");
        return "expense-form";
    }

    @PostMapping("/expenses/save")
    public String saveExpense(@Valid @ModelAttribute("expense") Expense expense,
                              BindingResult result) {
        if (result.hasErrors()) {
            return "expense-form";
        }
        expenseService.save(expense);
        return "redirect:/expenses";
    }

    @PostMapping("/expenses/delete/{id}")
    public String deleteExpense(@PathVariable Long id) {
        expenseService.delete(id);
        return "redirect:/expenses";
    }

    @GetMapping("/budgets")
    public String budgets(Model model) {
        List<Budget> budgets = budgetService.findAll();
        budgets.sort((a, b) -> {
            int yearCompare = b.getBudgetYear().compareTo(a.getBudgetYear());
            return yearCompare != 0 ? yearCompare : b.getBudgetMonth().compareTo(a.getBudgetMonth());
        });
        model.addAttribute("budgets", budgets);
        return "budgets";
    }

    @GetMapping("/budgets/new")
    public String newBudget(Model model) {
        LocalDate today = LocalDate.now();
        model.addAttribute("budget", new Budget(today.getYear(), today.getMonthValue(), null));
        model.addAttribute("pageTitle", "Set Monthly Budget");
        return "budget-form";
    }

    @GetMapping("/budgets/edit/{id}")
    public String editBudget(@PathVariable Long id, Model model) {
        model.addAttribute("budget", budgetService.findById(id));
        model.addAttribute("pageTitle", "Edit Monthly Budget");
        return "budget-form";
    }

    @PostMapping("/budgets/save")
    public String saveBudget(@Valid @ModelAttribute("budget") Budget budget,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "budget-form";
        }
        Budget existing = budgetService.findForMonth(budget.getBudgetYear(), budget.getBudgetMonth());
        if (existing != null && (budget.getId() == null || !existing.getId().equals(budget.getId()))) {
            existing.setAmount(budget.getAmount());
            budgetService.save(existing);
        } else {
            budgetService.save(budget);
        }
        return "redirect:/budgets";
    }

    @PostMapping("/budgets/delete/{id}")
    public String deleteBudget(@PathVariable Long id) {
        budgetService.delete(id);
        return "redirect:/budgets";
    }
}
