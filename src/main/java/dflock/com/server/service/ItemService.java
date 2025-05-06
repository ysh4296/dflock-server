package dflock.com.server.service;
import dflock.com.server.domain.Item;
import dflock.com.server.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository repo;

    public ItemService(ItemRepository repo) {
        this.repo = repo;
    }

    public List<Item> findAll() {
        return repo.findAll();
    }

    public Item save(Item item) {
        return repo.save(item);
    }
}