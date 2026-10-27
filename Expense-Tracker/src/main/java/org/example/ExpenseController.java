package org.example;

import jakarta.validation.Valid;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.io.IOException;

/** Handles browser requests and chooses which HTML page to return. */
@Controller
public class ExpenseController {
    private final ExpenseService expenseService;
    private final ExcelExportService excelExportService;

    public ExpenseController(ExpenseService expenseService, ExcelExportService excelExportService) {
        this.expenseService = expenseService;
        this.excelExportService = excelExportService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        fillDashboard(model);
        return "index"; // Thymeleaf resolves this to templates/index.html.
    }

    @PostMapping("/expenses")
    public String addExpense(@Valid @ModelAttribute("expenseForm") Expense expense,
                             BindingResult bindingResult, Model model,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            fillDashboard(model);
            // Keep the values and validation errors the user just submitted.
            model.addAttribute("expenseForm", expense);
            return "index";
        }
        expenseService.add(expense);
        redirectAttributes.addFlashAttribute("message", "Expense saved.");
        return "redirect:/";
    }

    @PostMapping("/expenses/{id}/delete")
    public String deleteExpense(@PathVariable long id, RedirectAttributes redirectAttributes) {
        expenseService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Expense deleted.");
        return "redirect:/";
    }

    @GetMapping("/expenses/export")
    public ResponseEntity<byte[]> exportExpenses() throws IOException {
        byte[] workbook = excelExportService.createWorkbook(expenseService.findRecent());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename("expenses.xlsx").build().toString())
                .body(workbook);
    }

    private void fillDashboard(Model model) {
        YearMonth currentMonth = YearMonth.now();
        model.addAttribute("expenses", expenseService.findRecent());
        model.addAttribute("monthTotal", expenseService.totalBetween(currentMonth.atDay(1), currentMonth.atEndOfMonth()));
        model.addAttribute("grandTotal", expenseService.totalBetween(LocalDate.of(1, 1, 1), LocalDate.of(9999, 12, 31)));
        model.addAttribute("monthLabel", currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")));
        if (!model.containsAttribute("expenseForm")) {
            Expense form = new Expense();
            form.setDate(LocalDate.now());
            model.addAttribute("expenseForm", form);
        }
    }
}
