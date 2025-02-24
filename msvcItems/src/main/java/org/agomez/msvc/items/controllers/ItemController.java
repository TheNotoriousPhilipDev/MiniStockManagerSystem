package org.agomez.msvc.items.controllers;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.agomez.libs.msvc.commons.entities.Product;
import org.agomez.msvc.items.models.Item;
import org.agomez.msvc.items.services.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RefreshScope //nos permite actualizar nuestros compnonentes con los cambios en las propiedades
@RestController
@RequestMapping("/api/v1/items")
public class ItemController {

    private final Logger logger = LoggerFactory.getLogger(ItemController.class);
    private final ItemService itemService;
    private final CircuitBreakerFactory cBreakerFactory;
    private final Environment environment;

    @Value("${configuration.text}")
    private String text;


    public ItemController(ItemService itemService, CircuitBreakerFactory cBreakerFactory, Environment environment) {
        this.itemService = itemService;
        this.cBreakerFactory = cBreakerFactory;
        this.environment = environment;
    }

    @GetMapping("/fetch-config")
    public ResponseEntity<?> fetchConfig(@Value("${server.port}") String port) {
        Map<String, String> map = new HashMap<>();
        map.put("text", text);
        map.put("port", port);
        logger.info(port);
        logger.info(text);
        if (environment.getActiveProfiles().length > 0 && environment.getActiveProfiles()[0].equals("dev")) {
            map.put("author.name", environment.getProperty("configuration.author.name"));
            map.put("author.email", environment.getProperty("configuration.author.email"));

        }
        return ResponseEntity.ok(map);

    }

    @GetMapping
    public List<Item> list() {
        return itemService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> details(@PathVariable Long id) {
        Optional<Item> itemOptional = cBreakerFactory.create("items")
                .run(() -> itemService.findById(id), //here we are trying to execute the remote method
                        e -> {
                            logger.error(e.getMessage());
                            Product product = new Product();
                            product.setCreateAt(LocalDate.now());
                            product.setName("Circuit breaker");
                            product.setPrice(1000.00);
                            return Optional.of(new Item(product, 5));
                        });
        if(itemOptional.isPresent()){
            return ResponseEntity.ok(itemOptional.get());
        }
        return ResponseEntity.status(404)
                .body(Collections.singletonMap(
                        "message",
                        "Product not found"));
    }

    @CircuitBreaker(name = "items", fallbackMethod = "getFallBackMethodProduct")
    @GetMapping("/details/{id}")
    public ResponseEntity<?> detailsDeclarativeVersion(@PathVariable Long id) {
        Optional<Item> itemOptional = itemService.findById(id);
        if(itemOptional.isPresent()){
            return ResponseEntity.ok(itemOptional.get());
        }
        return ResponseEntity.status(404)
                .body(Collections.singletonMap(
                        "message",
                        "Product not found"));


    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestBody Product product) {
        logger.info("Product created");
        return itemService.save(product);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Product update(@RequestBody Product product, @PathVariable Long id) {
        logger.info("Product updated");
        return itemService.update(product, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        itemService.delete(id);
    }


}
