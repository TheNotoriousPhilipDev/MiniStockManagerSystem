package org.agomez.msvc.items.services;

import org.agomez.libs.msvc.commons.entities.Product;
import org.agomez.msvc.items.models.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    List<Item> findAll();
    Optional<Item> findById(Long id);
    Product save(Product product);
    Product update(Product product, Long id);
    void delete(Long id);

}
