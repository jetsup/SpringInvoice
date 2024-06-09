package com.jetsup.invoices.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void createNewProduct(Product product) {
        productRepository.save(product);
    }

    public void deleteProduct(long productId) {
        boolean productExists = productRepository.existsById(productId);

        if (!productExists) {
            throw new IllegalStateException("Product with id " + productId + " does not exist.");
        }

        productRepository.deleteById(productId);
    }

    public void updateProduct(long productId, String name, int quantity, float unitPrice) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalStateException("Product with id " + productId + " does not exist."));

        if (name != null && !name.isEmpty() && !name.equals(product.getName())) {
            product.setName(name);
        }

        if (quantity != product.getQuantity()) {
            product.setQuantity(quantity);
        }

        if (unitPrice != product.getUnitPrice()) {
            product.setUnitPrice(unitPrice);
        }

        productRepository.save(product);
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new IllegalStateException("Product with id " + productId + " does not exist."));
    }

    public List<Product> getProductsByInvoiceId(long invoiceId) {
        return productRepository.findProductsByInvoiceId(invoiceId);
    }
}
