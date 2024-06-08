package com.jetsup.invoices.invoice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class InvoiceConfig {
    @Bean
    CommandLineRunner commandLineRunner(InvoiceRepository repository) {
        return args -> {
            Invoice invoice = new Invoice("AB1234", 1, "PAID", "2024-09-10", 200, "I sold something to you, I guess.");
            Invoice invoice2 = new Invoice("AB1434", 2, "PENDING", "2024-11-10", 2300, "I sold something to you, I guess too.");

            repository.saveAll(List.of(invoice, invoice2));
        };
    }
}
