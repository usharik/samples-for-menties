package com.example.service;

import com.example.persist.Invoice;
import com.example.persist.InvoiceRepository;
import com.example.service.excel.ColumnMapper;
import com.example.service.excel.ExcelImportFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    private final ExcelImportFactory excelImportFactory;

    private final ColumnMapper columnMapper = new ColumnMapper.ColumnMapperBuilder()
            .withStringColumn("invoiceNumber", 0, true)
            .withStringColumn("supplier", 1, true)
            .withDateColumn("date", 2, true)
            .withStringColumn("description", 3, false)
            .withIntegerColumn("qty", 4, true)
            .withBigDecimalColumn("total", 5, true)
            .build();

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository, ExcelImportFactory excelImportFactory) {
        this.invoiceRepository = invoiceRepository;
        this.excelImportFactory = excelImportFactory;
    }

    public List<Invoice> findAll() {
        return invoiceRepository.findAll();
    }

    public void importFromExcel(InputStream is) throws IOException {
        List<Invoice> invoicesList = new ArrayList<>();

        for (Map<String, Object> row: excelImportFactory.createExcelRowIterable(is, 0, 1, columnMapper)) {
            Invoice invoice = new Invoice();
            invoice.setInvoiceNumber((String) row.get("invoiceNumber"));
            invoice.setSupplier((String) row.get("supplier"));
            invoice.setDate((LocalDate) row.get("date"));
            invoice.setDescription((String) row.get("description"));
            invoice.setQty((Integer) row.get("qty"));
            invoice.setTotal((BigDecimal) row.get("total"));
            invoicesList.add(invoice);
        }
        invoiceRepository.saveAllAndFlush(invoicesList);
    }
}
