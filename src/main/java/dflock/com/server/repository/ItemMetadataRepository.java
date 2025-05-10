package dflock.com.server.repository;

import dflock.com.server.domain.ItemMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemMetadataRepository extends JpaRepository<ItemMetadata, Long> {
    Optional<ItemMetadata> findByItemName(String itemName);
}
