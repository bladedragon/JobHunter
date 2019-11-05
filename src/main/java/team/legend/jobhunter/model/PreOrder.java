package team.legend.jobhunter.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PreOrder {
    private String preOrder_id;
    private String order_id;
    private String service_id;
    private String tea_id;
    private String stu_id;
    private String item_id;
    private String item_time;
    private String item_price;
    private int item_isonline;
    private String item_time_detail;
    private String create_date;
    private String order_type;
    private long expire_time;

}
