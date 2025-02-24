package org.agomez.msvc.products.services;

import lombok.RequiredArgsConstructor;
import org.agomez.libs.msvc.commons.entities.Product;
import org.agomez.msvc.products.repository.ProductRepository;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final Environment environment;

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return ((List<Product>) productRepository.findAll()).stream().peek(product -> product.setPort(Integer.parseInt(Objects.requireNonNull(environment.getProperty("local.server.port"))))).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id).map(product -> {
            product.setPort(Integer.parseInt(Objects.requireNonNull(environment.getProperty("local.server.port"))));
            return product;
        });
    }

    @Override
    @Transactional
    public Product save(Product product) {
        return this.productRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
