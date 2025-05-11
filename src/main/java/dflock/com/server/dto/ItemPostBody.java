package dflock.com.server.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemPostBody {
    @Schema(description = "아이템 id", example = "1")
    private Long id;

    @Schema(description = "아이템 이름", example = "Magic Box")
    private String name;

    @Schema(description = "수량", example = "10")
    private int quantity;

    @Schema(description = "드랍 확률 (0.0 ~ 1.0)", example = "0.15")
    private double probability;
}
