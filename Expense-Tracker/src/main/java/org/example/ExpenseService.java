package org.example;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/** App-level operations. Controllers call this instead of talking to the database directly. */
@Service
@Transactional
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
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
}
