package com.example.expense_tracker_backend.util;


import com.example.expense_tracker_backend.dto.ExpenseDTO;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Component
public class PdfGenerator {


    public ByteArrayInputStream createPdf(List<ExpenseDTO> expenses, int year, int month, BigDecimal total) throws Exception {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();


        try {
            PdfWriter.getInstance(document, out);
            document.open();


            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph title = new Paragraph("Expense Report - " + year + "-" + String.format("%02d", month), titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);


            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{2, 3, 2, 2, 4});


// Header
            table.addCell("Date");
            table.addCell("Title");
            table.addCell("Category");
            table.addCell("Amount");
            table.addCell("Description");


            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");


            for (ExpenseDTO e : expenses) {
                table.addCell(e.getDate() != null ? e.getDate().format(fmt) : "");
                table.addCell(e.getTitle() == null ? "" : e.getTitle());
                table.addCell(e.getCategory() == null ? "" : e.getCategory());
                table.addCell(e.getAmount() == null ? "0.00" : e.getAmount().toString());
                table.addCell(e.getDescription() == null ? "" : e.getDescription());
            }


            document.add(table);


            Paragraph totalPara = new Paragraph("\nTotal: " + (total == null ? "0.00" : total.toString()), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12));
            totalPara.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalPara);


            document.close();


            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception ex) {
            throw ex;
        }
    }
}