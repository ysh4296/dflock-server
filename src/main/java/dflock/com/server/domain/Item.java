package dflock.com.server.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    @Id
    private String id;

    private String name;

    private int quantity;

    private double probability;
}
