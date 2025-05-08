package dflock.com.server.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity는 Item으로 통일 가능
 * Repository만 분리하면 됨
 * webdataloader는 접속 url을 환경설정으로 import하도록 하여 유기적인 업데이트를 도모함
 */
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

