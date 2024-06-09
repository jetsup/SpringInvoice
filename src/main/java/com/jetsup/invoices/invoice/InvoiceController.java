package com.jetsup.invoices.invoice;

import com.jetsup.invoices.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final ProductRepository productRepository;

    @Autowired
    public InvoiceController(InvoiceService invoiceService, ProductRepository productRepository) {
        this.invoiceService = invoiceService;
        this.productRepository = productRepository;
    }


    @GetMapping
    public List<Invoice> getInvoices() {
        return invoiceService.getInvoices();
    }

    @GetMapping(path = "{invoiceId}")
    public Map<String, Object> getInvoice(@PathVariable("invoiceId") Long invoiceId) {
        return invoiceService.getInvoice(invoiceId);
    }

    @PostMapping(path = "new")
    public void createNewInvoice(@RequestBody Invoice invoice) {
        invoiceService.createNewInvoice(invoice);
    }

    @DeleteMapping(path = "{invoiceId}")
    public void deleteInvoice(@PathVariable("invoiceId") Long invoiceId) {
        invoiceService.deleteInvoice(invoiceId);
    }

    @PutMapping(path = "{invoiceId}")
    public void updateInvoice(@PathVariable("invoiceId") Long invoiceId, @RequestBody Invoice requestBody) {
        String invoiceStatus = requestBody.getInvoiceStatus();
        String clientName = requestBody.getClientName();
        String clientEmail = requestBody.getClientEmail();
        String clientStreetAddress = requestBody.getClientStreetAddress();
        String clientCity = requestBody.getClientCity();
        String clientZipCode = requestBody.getClientZipCode();
        String clientCountry = requestBody.getClientCountry();
        String dueDate = requestBody.getDueDate();
        String notes = requestBody.getNotes();

        invoiceService.updateInvoice(invoiceId, invoiceStatus, clientName, clientEmail, clientStreetAddress, clientCity, clientZipCode, clientCountry, dueDate, notes);
    }
}
