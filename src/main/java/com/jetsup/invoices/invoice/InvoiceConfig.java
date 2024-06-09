package com.jetsup.invoices.invoice;

import com.jetsup.invoices.product.Product;
import com.jetsup.invoices.product.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class InvoiceConfig {
    @Bean
    CommandLineRunner commandLineRunner(InvoiceRepository repository, ProductRepository productRepository) {
        return args -> {
            Invoice invoice = new Invoice("AB1234", "PAID", "George Washington", "washington@gmail.com", "1234 Cherry Tree Lane", "Mount Vernon", "22121", "USA", "2024-09-10", 200, "I sold something to you, I guess.");
            Invoice invoice2 = new Invoice("AB1434", "PENDING", "John Adams", "adams@yahoo.com", "1234 Quincy Street", "Quincy", "02169", "USA", "2024-11-10", 2300, "I sold something to you, I guess too.");
            repository.saveAll(List.of(invoice, invoice2));

            long invoiceId = invoice.getId();
            long invoice2Id = invoice2.getId();

            Product product1 = new Product("Apple", 5, 1.50f, invoiceId);
            Product product2 = new Product("Banana", 10, 0.50f, invoice2Id);
            Product product3 = new Product("Orange", 3, 1.00f, invoice2Id);
            Product product4 = new Product("Grapes", 2, 2.00f, invoice2Id);

            productRepository.saveAll(List.of(product1, product2, product3, product4));
        };
    }
}
