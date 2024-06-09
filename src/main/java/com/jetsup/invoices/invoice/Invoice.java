package com.jetsup.invoices.invoice;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table
public class Invoice {
    @Setter
    @Getter
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
    @Setter
    @Getter
    private String invoiceId;//e.g., AB1234
    @Setter
    @Getter
    private String invoiceStatus;//draft, pending, paid
    @Setter
    @Getter
    private String clientName;
    @Setter
    @Getter
    private String clientEmail;
    @Setter
    @Getter
    private String clientStreetAddress;
    @Setter
    @Getter
    private String clientCity;
    @Setter
    @Getter
    private String clientZipCode;
    @Setter
    @Getter
    private String clientCountry;
    @Setter
    @Getter
    private String dueDate; //timestamp
    @Setter
    @Getter
    private String notes; // additional notes about the invoice
    @Setter
    @Getter
    private String createdAt; // timestamp
    @Transient
    @Setter
    @Getter
    private float total;

    public Invoice() {
    }

    public Invoice(long id, String invoiceId, String invoiceStatus, String clientName, String clientEmail, String clientStreetAddress, String clientCity, String clientZipCode, String clientCountry, String dueDate, double total, String notes) {
        this.id = id;
        this.invoiceId = invoiceId;
        this.invoiceStatus = invoiceStatus;
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.clientStreetAddress = clientStreetAddress;
        this.clientCity = clientCity;
        this.clientZipCode = clientZipCode;
        this.clientCountry = clientCountry;
        this.createdAt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        this.dueDate = dueDate;
        this.notes = notes;
    }

    public Invoice(String invoiceId, String invoiceStatus, String clientName, String clientEmail, String clientStreetAddress, String clientCity, String clientZipCode, String clientCountry, String dueDate, double total, String notes) {
        this.invoiceId = invoiceId;
        this.invoiceStatus = invoiceStatus;
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.clientStreetAddress = clientStreetAddress;
        this.clientCity = clientCity;
        this.clientZipCode = clientZipCode;
        this.clientCountry = clientCountry;
        this.createdAt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        this.dueDate = dueDate;
        this.notes = notes;
    }

    public Invoice(String invoiceStatus, String clientName, String clientEmail, String clientStreetAddress, String clientCity, String clientZipCode, String clientCountry, String dueDate, String notes) {
        this.invoiceStatus = invoiceStatus;
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.clientStreetAddress = clientStreetAddress;
        this.clientCity = clientCity;
        this.clientZipCode = clientZipCode;
        this.clientCountry = clientCountry;
        this.createdAt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        this.dueDate = dueDate;
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "{" +
                "invoice_id='" + this.invoiceId + "'" +
                ", status='" + this.invoiceStatus + "'" +
                ", client_name='" + this.clientName + "'" +
                ", client_email='" + this.clientEmail + "'" +
                ", total='" + this.total + "'" +
                ", created_at='" + this.createdAt + "'" +
                ", due_date='" + this.dueDate + "'" +
                ", notes='" + this.notes + "'" +
                '}';
    }
}
