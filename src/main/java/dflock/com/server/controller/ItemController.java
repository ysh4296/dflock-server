package dflock.com.server.controller;

import dflock.com.server.domain.Item;
import dflock.com.server.service.ItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @GetMapping
    public List<Item> getItems() {
        return service.findAll();
    }

    @PostMapping
    public Item createItem(@RequestBody Item item) {
        return service.save(item);
    }
}