package dflock.com.server.dto;

import dflock.com.server.domain.FirstItem;
import dflock.com.server.domain.MileageFirstItem;
import dflock.com.server.domain.MileageSecondItem;
import dflock.com.server.domain.SecondItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ItemResponse {
    private List<FirstItem> firstItems;
    private List<SecondItem> secondItems;
    private List<MileageFirstItem> mileageFirstItems;
    private List<MileageSecondItem> mileageSecondItems;

    public ItemResponse(List<FirstItem> firstItems, List<SecondItem> secondItems, List<MileageFirstItem> mileageFirstItems, List<MileageSecondItem> mileageSecondItems) {
        this.firstItems = firstItems;
        this.secondItems = secondItems;
        this.mileageFirstItems = mileageFirstItems;
        this.mileageSecondItems = mileageSecondItems;
    }
}