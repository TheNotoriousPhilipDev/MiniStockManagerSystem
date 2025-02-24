package org.agomez.msvc.products.controllers;

import lombok.RequiredArgsConstructor;
import org.agomez.libs.msvc.commons.entities.Product;
import org.agomez.msvc.products.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final Logger logger = LoggerFactory.getLogger(ProductController.class.getName());


    @GetMapping
    public  ResponseEntity<?> list() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> details (@PathVariable Long id) {
//        if (id.equals(10L)) {
//            throw new IllegalStateException("Error getting product");
//        }
//        if (id.equals(7L)) {
//            TimeUnit.SECONDS.sleep(5L);
//        }
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Product product) {
        logger.info("Product created");
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Product product, @PathVariable Long id) {
        logger.info("Product updated");
        Optional<Product> productOptional = productService.findById(id);
        if (productOptional.isPresent()) {
            Product productDB = productOptional.get();
            productDB.setName(product.getName());
            productDB.setPrice(product.getPrice());
            productDB.setCreateAt(product.getCreateAt());
            return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(productDB));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        logger.info("Product deleted");
        Optional<Product> productOptional = productService.findById(id);
        if (productOptional.isPresent()) {
            productService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
