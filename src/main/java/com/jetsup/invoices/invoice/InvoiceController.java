package com.jetsup.invoices.invoice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }


    @GetMapping
    public List<Invoice> getInvoices() {
        return invoiceService.getInvoices();
    }

    @PostMapping
    public void createNewInvoice(@RequestBody Invoice invoice) {
        invoiceService.createNewInvoice(invoice);
    }

    @DeleteMapping(path = "{invoiceId}")
    public void deleteInvoice(@PathVariable("invoiceId") Long invoiceId) {
        invoiceService.deleteInvoice(invoiceId);
    }

    @PutMapping(path = "{invoiceId}")
    public void updateInvoice(
            @PathVariable("invoiceId") Long invoiceId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String due_date,
            @RequestParam(required = false) Double total,
            @RequestParam(required = false) String notes
    ) {
        invoiceService.updateInvoice(invoiceId, status, due_date, total, notes);
    }
}
