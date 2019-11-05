package team.legend.jobhunter.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Order {
    private String order_id;
    private String service_id;
    private String tea_id;
    private String stu_id;
    private String item_time;
    private String item_price;
    private int item_isonline;
    private String item_time_detail;
    private String order_date;
    private String order_type;
    private int order_status;
    private String order_modify_date;
    private int  iseffective;
}
