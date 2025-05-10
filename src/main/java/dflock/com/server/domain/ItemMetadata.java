package dflock.com.server.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "item_metadata", uniqueConstraints = {
        @UniqueConstraint(columnNames = "itemName")
})
public class ItemMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String itemId;

    @Column(nullable = false, unique = true)
    private String itemName;

    private int unitPrice;
}
