package com.jetsup.invoices.invoice;

import com.jetsup.invoices.product.Product;
import com.jetsup.invoices.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final ProductRepository productRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository, ProductRepository productRepository) {
        this.invoiceRepository = invoiceRepository;
        this.productRepository = productRepository;
    }

    public List<Invoice> getInvoices() {
        List<Invoice> invoices = invoiceRepository.findAll();

        // iterate them and get the total of each invoice by getting the quantity and amount from the products
        for (Invoice invoice : invoices) {
            List<Product> products = productRepository.findProductsByInvoiceId(invoice.getId());
            float accumulatedTotal = 0;
            for (Product product : products) {
                accumulatedTotal += product.getQuantity() * product.getUnitPrice();
            }
            invoice.setTotal(accumulatedTotal);
        }

        return invoices;
    }

    private String generateInvoiceId() {
        StringBuilder invoiceId = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            invoiceId.append((char) (Math.random() * 26 + 'A'));
        }
        for (int i = 0; i < 4; i++) {
            invoiceId.append((int) (Math.random() * 10));
        }
        return invoiceId.toString();
    }

    public void createNewInvoice(Invoice invoice) {
        String invoiceId = generateInvoiceId();
        Optional<Invoice> dbInvoice = invoiceRepository.findInvoiceByInvoiceId(invoiceId);

        while (dbInvoice.isPresent()) {
            invoiceId = generateInvoiceId();
            dbInvoice = invoiceRepository.findInvoiceByInvoiceId(invoiceId);
        }
        // created_at
        String createdAt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        invoice.setCreatedAt(createdAt);
        invoice.setInvoiceId(invoiceId);
        System.out.println(invoiceId + " -> " + invoice);
        invoiceRepository.save(invoice);
    }

    public void deleteInvoice(long invoiceId) {
        boolean invoiceExists = invoiceRepository.existsById(invoiceId);

        if (!invoiceExists) {
            throw new IllegalStateException("Invoice with id " + invoiceId + " does not exist.");
        }

        invoiceRepository.deleteById(invoiceId);
    }

    @Transactional
    public void updateInvoice(Long invoiceId, String status, String clientName, String clientEmail, String clientStreetAddress, String clientCity, String clientZipCode, String clientCountry, String dueDate, String notes) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalStateException("Invoice with id " + invoiceId + " does not exist."));

        if (status != null && !status.isEmpty() && !status.equals(invoice.getInvoiceStatus())) {
            invoice.setInvoiceStatus(status);
        }

        if (clientName != null && !clientName.isEmpty() && !clientName.equals(invoice.getClientName())) {
            invoice.setClientName(clientName);
        }

        if (clientEmail != null && !clientEmail.isEmpty() && !clientEmail.equals(invoice.getClientEmail())) {
            invoice.setClientEmail(clientEmail);
        }

        if (clientStreetAddress != null && !clientStreetAddress.isEmpty() && !clientStreetAddress.equals(invoice.getClientStreetAddress())) {
            invoice.setClientStreetAddress(clientStreetAddress);
        }

        if (clientCity != null && !clientCity.isEmpty() && !clientCity.equals(invoice.getClientCity())) {
            invoice.setClientCity(clientCity);
        }

        if (clientZipCode != null && !clientZipCode.isEmpty() && !clientZipCode.equals(invoice.getClientZipCode())) {
            invoice.setClientZipCode(clientZipCode);
        }

        if (clientCountry != null && !clientCountry.isEmpty() && !clientCountry.equals(invoice.getClientCountry())) {
            invoice.setClientCountry(clientCountry);
        }

        if (dueDate != null && !dueDate.isEmpty() && !dueDate.equals(invoice.getDueDate())) {
            invoice.setDueDate(dueDate);
        }

        if (notes != null && !notes.isEmpty() && !notes.equals(invoice.getNotes())) {
            invoice.setNotes(notes);
        }

        invoiceRepository.save(invoice);
    }

    public Map<String, Object> getInvoice(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalStateException("Invoice with id " + invoiceId + " does not exist."));

        List<Product> products = productRepository.findProductsByInvoiceId(invoiceId);

        // in each product, get the quantity and unit price and calculate the total
        float accumulatedTotal = 0;
        for (Product product : products) {
            accumulatedTotal += product.getQuantity() * product.getUnitPrice();
        }

        return Map.of("invoice", invoice, "products", products, "total", accumulatedTotal);

//        return List.of(invoice.toString(), products.toString());
    }
}
