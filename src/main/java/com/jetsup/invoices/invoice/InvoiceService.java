package com.jetsup.invoices.invoice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public List<Invoice> getInvoices() {
        return invoiceRepository.findAll();
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
        invoice.setCreated_at(createdAt);
        invoice.setInvoice_id(invoiceId);
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
    public void updateInvoice(Long invoiceId, String status, String dueDate, Double total, String notes) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalStateException("Invoice with id " + invoiceId + " does not exist."));

        if (status != null && !status.isEmpty() && !status.equals(invoice.getStatus())) {
            invoice.setStatus(status);
        }

        if (dueDate != null && !dueDate.isEmpty() && !dueDate.equals(invoice.getDue_date())) {
            invoice.setDue_date(dueDate);
        }

        if (total != null && total > 0 && !total.equals(invoice.getTotal())) {
            invoice.setTotal(total);
        }

        if (notes != null && !notes.isEmpty() && !notes.equals(invoice.getNotes())) {
            invoice.setNotes(notes);
        }

        invoiceRepository.save(invoice);
    }
}
