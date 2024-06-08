package com.jetsup.invoices.invoice;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table
public class Invoice {
    @Id
    @SequenceGenerator(
            name = "invoice_sequence",
            sequenceName = "invoice_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "invoice_sequence"
    )
    private long id; // primary key
    private String invoice_id;//e.g., AB1234
    private int client_id; // foreign key
    private String status;//draft, pending, paid
    private String created_at; // timestamp
    private String due_date; //timestamp
    private double total; // total amount of the invoice
    private String notes; // additional notes about the invoice

    public Invoice() {
    }

    public Invoice(long id, String invoice_id, int client_id, String status, /*String created_at,*/ String due_date, double total, String notes) {
        this.id = id;
        this.invoice_id = invoice_id;
        this.client_id = client_id;
        this.status = status;
//        this.created_at = created_at;
        this.created_at = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        this.due_date = due_date;
        this.total = total;
        this.notes = notes;
    }

    // used when there is a draft present
    public Invoice(String invoice_id, int client_id, String status, /*String created_at,*/ String due_date, double total, String notes) {
        this.invoice_id = invoice_id;
        this.client_id = client_id;
        this.status = status;
//        this.created_at = created_at;
        this.created_at = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        this.due_date = due_date;
        this.total = total;
        this.notes = notes;
    }

    // new fresh invoice, no draft present
    public Invoice(int client_id, String status, /*String created_at,*/ String due_date, double total, String notes) {
        this.client_id = client_id;
        this.status = status;
//        this.created_at = created_at;
        this.created_at = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        this.due_date = due_date;
        this.total = total;
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Invoices{" +
                "invoice_id='" + this.invoice_id + '\'' +
                ", client_id=" + this.client_id +
                ", status='" + this.status + '\'' +
                ", created_at='" + this.created_at + '\'' +
                ", due_date='" + this.due_date + '\'' +
                ", total=" + this.total +
                ", notes='" + this.notes + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(String invoice_id) {
        this.invoice_id = invoice_id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
