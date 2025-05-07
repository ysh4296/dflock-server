package dflock.com.server.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dflock.com.server.domain.Item;
import dflock.com.server.repository.ItemRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class DataLoader {

    private final ItemRepository itemRepository;

    public DataLoader(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @PostConstruct
    public void loadItemsFromJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = getClass().getResourceAsStream("/static/items.json");
            List<Item> items = mapper.readValue(is, new TypeReference<List<Item>>() {});
            System.out.println(items);
            itemRepository.saveAll(items);
            System.out.println("✅ 초기 아이템 데이터 로드 완료");
        } catch (Exception e) {
            System.err.println("❌ 초기 데이터 로드 실패: " + e.getMessage());
        }
    }
}
