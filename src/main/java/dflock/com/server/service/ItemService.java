package dflock.com.server.service;
import dflock.com.server.domain.FirstItem;
import dflock.com.server.repository.FirstItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final FirstItemRepository repo;

    public ItemService(FirstItemRepository repo) {
        this.repo = repo;
    }

    public List<FirstItem> findAll() {
        return repo.findAll();
    }

    public FirstItem save(FirstItem item) {
        return repo.save(item);
    }
}