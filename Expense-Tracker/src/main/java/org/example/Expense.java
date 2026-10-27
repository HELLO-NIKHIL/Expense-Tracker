package org.example;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

/** One expense. JPA maps this class to a row in PostgreSQL's expenses table. */
@Entity
@Table(name = "expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Please enter a name.")
    @Size(max = 100, message = "Name must be 100 characters or fewer.")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Please enter a category.")
    @Size(max = 50, message = "Category must be 50 characters or fewer.")
    @Column(nullable = false, length = 50)
    private String category;

    @NotNull(message = "Please enter an amount.")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero.")
    @Digits(integer = 10, fraction = 2, message = "Use up to 10 whole digits and 2 decimal places.")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @NotNull(message = "Please choose a date.")
    @Column(nullable = false)
    private LocalDate date;

    @Size(max = 250, message = "Note must be 250 characters or fewer.")
    @Column(length = 250)
    private String note;

    // JPA needs a no-argument constructor to turn database rows into Java objects.
    public Expense() { }

    public Expense(String name, String category, BigDecimal amount, LocalDate date, String note) {
        this.name = name;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.note = note;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name == null ? null : name.trim(); }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category == null ? null : category.trim(); }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note == null ? "" : note.trim(); }
}
