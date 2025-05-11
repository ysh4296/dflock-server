package dflock.com.server.controller;

import dflock.com.server.domain.ItemMetadata;
import dflock.com.server.dto.ItemResponse;
import dflock.com.server.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/items")
public class ItemController {

    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "모든 아이템 조회")
    public ItemResponse getItems() {
        return service.findAll();
    }

    @GetMapping("/metadata")
    @Operation(summary = "모든 아이템 메타데이터 조회")
    public List<ItemMetadata> getItemMetadata() {
        return service.findAllMetadata();
    }
}
