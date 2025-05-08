package dflock.com.server.repository;

import dflock.com.server.domain.SecondItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecondItemRepository extends JpaRepository<SecondItem, Long> {
}
