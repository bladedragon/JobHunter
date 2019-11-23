package team.legend.jobhunter.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PriceItem {
    private Integer price;
    private Integer discount;
}
