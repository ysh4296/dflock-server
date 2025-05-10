package dflock.com.server.repository;

import dflock.com.server.domain.FirstItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FirstItemRepository extends JpaRepository<FirstItem, Long> {
}
