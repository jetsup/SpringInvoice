package com.jetsup.invoices.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {
    private final ProductService productService;
    private final ProductRepository productRepository;

    @Autowired
    public ProductController(ProductService productService, ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @PostMapping(path = "new")
    public void createProduct(@RequestBody Product product) {
        productService.createNewProduct(product);
    }

    // no reding products, to read products, retrieve through invoices

    @PutMapping(path = "{productId}")
    public void updateProduct(
            @PathVariable("productId") Long productId,
            @RequestBody Product product
    ) {
        String name = product.getName();
        int quantity = product.getQuantity();
        float unitPrice = product.getUnitPrice();

        productService.updateProduct(productId, name, quantity, unitPrice);
    }

    @DeleteMapping(path = "{productId}")
    public void deleteProduct(@PathVariable("productId") Long productId) {
        productService.deleteProduct(productId);
    }
}
