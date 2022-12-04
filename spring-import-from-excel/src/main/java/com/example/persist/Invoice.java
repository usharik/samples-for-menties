package com.example.persist;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String invoiceNumber;

    @Column
    private String supplier;

    @Column
    private LocalDate date;

    @Column
    private String description;

    @Column
    private Integer qty;

    @Column
    private BigDecimal total;

    public Invoice() {
    }

    public Invoice(String supplier, LocalDate date, String description, Integer qty, BigDecimal total) {
        this.supplier = supplier;
        this.date = date;
        this.description = description;
        this.qty = qty;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal value) {
        this.total = value;
    }
}
