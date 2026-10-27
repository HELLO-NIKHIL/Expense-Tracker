package org.example;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/** Builds an actual Excel .xlsx workbook from the expenses stored in PostgreSQL. */
@Service
public class ExcelExportService {
    private static final String[] HEADERS = {"Date", "Name", "Category", "Amount", "Note"};

    public byte[] createWorkbook(List<Expense> expenses) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Expenses");
            Row header = sheet.createRow(0);
            CellStyle headerStyle = workbook.createCellStyle();
            Font boldFont = workbook.createFont();
            boldFont.setBold(true);
            headerStyle.setFont(boldFont);

            for (int column = 0; column < HEADERS.length; column++) {
                Cell cell = header.createCell(column);
                cell.setCellValue(HEADERS[column]);
                cell.setCellStyle(headerStyle);
            }

            int rowNumber = 1;
            for (Expense expense : expenses) {
                Row row = sheet.createRow(rowNumber++);
                row.createCell(0).setCellValue(expense.getDate().toString());
                row.createCell(1).setCellValue(expense.getName());
                row.createCell(2).setCellValue(expense.getCategory());
                row.createCell(3).setCellValue(expense.getAmount().doubleValue());
                row.createCell(4).setCellValue(expense.getNote() == null ? "" : expense.getNote());
            }

            for (int column = 0; column < HEADERS.length; column++) sheet.autoSizeColumn(column);
            sheet.createFreezePane(0, 1);
            workbook.write(output);
            return output.toByteArray();
        }
    }
}
