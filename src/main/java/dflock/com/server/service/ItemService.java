package dflock.com.server.service;

import dflock.com.server.domain.ItemMetadata;
import dflock.com.server.dto.ItemResponse;
import dflock.com.server.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final FirstItemRepository firstItemRepo;
    private final SecondItemRepository secondItemRepo;
    private final MileageFirstItemRepository mileageFirstItemRepo;
    private final MileageSecondItemRepository mileageSecondItemRepo;
    private final ItemMetadataRepository itemMetadataRepo;

    public ItemService(
            FirstItemRepository firstItemRepo,
            SecondItemRepository secondItemRepo,
            MileageFirstItemRepository mileageFirstItemRepository,
            MileageSecondItemRepository mileageSecondItemRepository,
            ItemMetadataRepository itemMetadataRepo
    ) {
        this.firstItemRepo = firstItemRepo;
        this.secondItemRepo = secondItemRepo;
        this.mileageFirstItemRepo = mileageFirstItemRepository;
        this.mileageSecondItemRepo = mileageSecondItemRepository;
        this.itemMetadataRepo = itemMetadataRepo;

    }

    public ItemResponse findAll() {
        return new ItemResponse(
                firstItemRepo.findAll(),
                secondItemRepo.findAll(), // SecondItem
                mileageFirstItemRepo.findAll(), // MileageFirstItem
                mileageSecondItemRepo.findAll() // MileageSecondItem
        );
    }

    public List<ItemMetadata> findAllMetadata() {
        return itemMetadataRepo.findAll();
    }
}
