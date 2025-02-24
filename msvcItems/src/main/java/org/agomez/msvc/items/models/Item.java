package org.agomez.msvc.items.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.agomez.libs.msvc.commons.entities.Product;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    private Product product;
    private int quantity;

    public double getTotal() {
        return product.getPrice() * quantity;
    }
}
