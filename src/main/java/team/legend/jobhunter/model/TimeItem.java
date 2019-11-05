package team.legend.jobhunter.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TimeItem {
    private String service_id;
    private String item_id;
    private String item_time;
    private String item_origin_price;
    private String service_type;
    private int item_status;
    private int isonline;
    private String item_time_detail;
    private String item_discount;
}
