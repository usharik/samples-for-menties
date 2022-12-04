package com.example.service;

import com.example.persist.Invoice;
import com.example.persist.InvoiceRepository;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.*;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public List<Invoice> findAll() {
        return invoiceRepository.findAll();
    }

    public void importFromExcel(InputStream is) throws IOException {
        XSSFWorkbook workBook = new XSSFWorkbook(is);
        FormulaEvaluator evaluator = workBook.getCreationHelper().createFormulaEvaluator();

        XSSFSheet sheet = workBook.getSheetAt(0);
        List<Invoice> invoicesList = new ArrayList<>();

        for (XSSFRow row : new ExcelRowIterable(sheet, 1)) {
            Invoice invoice = new Invoice();

            invoice.setInvoiceNumber(Optional.ofNullable(row.getCell(0))
                    .map(XSSFCell::getStringCellValue)
                    .orElseThrow(() -> new IllegalArgumentException("No invoice number at row " + row.getRowNum())));

            invoice.setSupplier(Optional.ofNullable(row.getCell(1))
                    .map(XSSFCell::getStringCellValue)
                    .orElseThrow(() -> new IllegalArgumentException("No supplier at row " + row.getRowNum())));

            invoice.setDate(Optional.ofNullable(row.getCell(2))
                    .map(XSSFCell::getDateCellValue)
                    .map(date -> date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                    .orElseThrow(() -> new IllegalArgumentException("No date at row " + row.getRowNum())));

            invoice.setDescription(Optional.ofNullable(row.getCell(3))
                    .map(XSSFCell::getStringCellValue)
                    .orElse(null));

            invoice.setQty(Optional.ofNullable(row.getCell(4))
                    .map(XSSFCell::getNumericCellValue)
                    .map(val -> (int) Math.ceil(val))
                    .orElseThrow(() -> new IllegalArgumentException("No qty at row " + row.getRowNum())));

            invoice.setTotal(Optional.ofNullable(row.getCell(5))
                    .map(XSSFCell::getNumericCellValue)
                    .map(BigDecimal::new)
                    .orElse(null));
            invoicesList.add(invoice);
        }
        invoiceRepository.saveAllAndFlush(invoicesList);
    }

    private static class ExcelRowIterable implements Iterable<XSSFRow> {

        private int rowIndex;

        private final XSSFSheet sheet;

        private ExcelRowIterable(XSSFSheet sheet, int rowToStart) {
            this.sheet = sheet;
            this.rowIndex = rowToStart;
        }

        @Override
        public Iterator<XSSFRow> iterator() {

            return new Iterator<>() {
                @Override
                public boolean hasNext() {
                    return Optional.ofNullable(sheet.getRow(rowIndex))
                            .map(row -> row.getCell(0))
                            .filter(cell -> cell.getCellType() != CellType.BLANK)
                            .isPresent();
                }

                @Override
                public XSSFRow next() {
                    return sheet.getRow(rowIndex++);
                }
            };
        }
    }
}
