package org.example;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/** Database access for monthly category budgets. */
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findAllByMonthStartOrderByCategoryAsc(LocalDate monthStart);
}
