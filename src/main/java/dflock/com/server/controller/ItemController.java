package dflock.com.server.controller;

import dflock.com.server.domain.Item;
import dflock.com.server.dto.ItemPostRequest;
import dflock.com.server.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "아이템 조회")
    public List<Item> getItems() {
        return service.findAll();
    }

    @PostMapping
    @Operation(summary = "아이템 생성")
    public Item createItem(@RequestBody ItemPostRequest request) {
        Item item = Item.builder()
                .id(request.getId())
                .name(request.getName())
                .quantity(request.getQuantity())
                .probability(request.getProbability())
                .build();
        return service.save(item);
    }
}