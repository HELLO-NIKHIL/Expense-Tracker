package org.example;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/** App-level operations. Controllers call this instead of talking to the database directly. */
@Service
@Transactional
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final BudgetRepository budgetRepository;

    public ExpenseService(ExpenseRepository expenseRepository, BudgetRepository budgetRepository) {
        this.expenseRepository = expenseRepository;
        this.budgetRepository = budgetRepository;
    }

    @Transactional(readOnly = true)
    public List<Expense> findRecent() {
        return expenseRepository.findAllByOrderByDateDescIdDesc();
    }

    public Expense add(Expense expense) {
        return expenseRepository.save(expense);
    }

    public void delete(long id) {
        expenseRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public BigDecimal totalBetween(LocalDate start, LocalDate end) {
        return expenseRepository.findByDateBetween(start, end).stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional(readOnly = true)
    public List<CategorySummary> categorySummaries(LocalDate start, LocalDate end, LocalDate monthStart) {
        Map<String, BigDecimal> spendingByCategory = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (Expense expense : expenseRepository.findByDateBetween(start, end)) {
            spendingByCategory.merge(expense.getCategory(), expense.getAmount(), BigDecimal::add);
        }

        Map<String, Budget> budgetsByCategory = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (Budget budget : budgetRepository.findAllByMonthStartOrderByCategoryAsc(monthStart)) {
            budgetsByCategory.put(budget.getCategory(), budget);
        }

        Set<String> categories = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        categories.addAll(spendingByCategory.keySet());
        categories.addAll(budgetsByCategory.keySet());

        return categories.stream()
                .map(category -> {
                    BigDecimal spent = spendingByCategory.getOrDefault(category, BigDecimal.ZERO);
                    Budget budget = budgetsByCategory.get(category);
                    BigDecimal budgetAmount = budget == null ? null : budget.getAmount();
                    return new CategorySummary(category, spent, budgetAmount);
                })
                .toList();
    }

    public Budget saveMonthlyBudget(Budget submittedBudget, LocalDate monthStart) {
        submittedBudget.setMonthStart(monthStart);
        for (Budget existingBudget : budgetRepository.findAllByMonthStartOrderByCategoryAsc(monthStart)) {
            if (existingBudget.getCategory().equalsIgnoreCase(submittedBudget.getCategory())) {
                existingBudget.setAmount(submittedBudget.getAmount());
                return budgetRepository.save(existingBudget);
            }
        }
        return budgetRepository.save(submittedBudget);
    }
}
