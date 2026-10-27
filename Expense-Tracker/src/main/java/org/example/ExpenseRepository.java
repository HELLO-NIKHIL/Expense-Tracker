package org.example;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/** Spring Data creates the database implementation for this interface at startup. */
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findAllByOrderByDateDescIdDesc();
    List<Expense> findByDateBetween(LocalDate start, LocalDate end);
}
