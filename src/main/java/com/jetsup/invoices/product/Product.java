package com.jetsup.invoices.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
public class Product {
    @Setter
    @Getter
    @Id
    @SequenceGenerator(
            name = "product_sequence",
            sequenceName = "product_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_sequence"
    )
    private long id; // primary key
    @Setter
    @Getter
    private String name;
    @Setter
    @Getter
    private int quantity;
    @Setter
    @Getter
    private float unitPrice;
    @Setter
    @Getter
    private long invoiceId;

    public Product() {
    }

    public Product(long id, String name, int quantity, float unitPrice, long invoiceId) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.invoiceId = invoiceId;
    }

    public Product(String name, int quantity, float unitPrice, long invoiceId) {
        this.name = name;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.invoiceId = invoiceId;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantity='" + quantity + '\'' +
                ", unitPrice='" + unitPrice + '\'' +
                ", invoiceId=" + invoiceId +
                '}';
    }
}
