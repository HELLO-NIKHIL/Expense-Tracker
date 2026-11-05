package org.example;

import java.math.BigDecimal;

/** Display data for one category's spending and optional monthly budget. */
public class CategorySummary {
    private final String category;
    private final BigDecimal spent;
    private final BigDecimal budgetAmount;

    public CategorySummary(String category, BigDecimal spent, BigDecimal budgetAmount) {
        this.category = category;
        this.spent = spent;
        this.budgetAmount = budgetAmount;
    }

    public String getCategory() { return category; }
    public BigDecimal getSpent() { return spent; }
    public BigDecimal getBudgetAmount() { return budgetAmount; }
    public boolean hasBudget() { return budgetAmount != null; }
    public boolean isOverBudget() { return hasBudget() && spent.compareTo(budgetAmount) > 0; }
    public BigDecimal getRemaining() { return hasBudget() ? budgetAmount.subtract(spent) : null; }
}
