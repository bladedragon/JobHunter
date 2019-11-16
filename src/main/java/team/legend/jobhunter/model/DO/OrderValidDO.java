package team.legend.jobhunter.model.DO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderValidDO {
    private String tea_id;
    private String stu_id;
    private Integer order_status;
    private String appoint_location;
    private Long appoint_time;
    private Integer order_confirm;
    private Integer stu_confirm;
    private Integer tea_confirm;

    //学生确认订单完成
    OrderValidDO(String tea_id,Integer order_status,Integer order_confirm,Integer stu_confirm,Integer tea_confirm){
        this.tea_id = tea_id;
        this.order_confirm = order_confirm;
        this.order_status = order_status;
        this.stu_confirm = stu_confirm;
        this.stu_id = "";
        this.appoint_location = "";
        this.appoint_time = Long.valueOf(0);
        this.tea_confirm = tea_confirm;
    }
    //老师确认订单完成
    OrderValidDO(String stu_id,Integer order_status,Integer order_confirm,Integer tea_confirm){
        this.tea_id = "";
        this.order_confirm = order_confirm;
        this.order_status = order_status;
        this.tea_confirm = tea_confirm;
        this.stu_id = stu_id;
        this.appoint_location = "";
        this.appoint_time = Long.valueOf(0);
        this.stu_confirm = 0;
    }
    //确认老师提交时间地点
    OrderValidDO(String stu_id, Integer order_status, Integer order_confirm, Long appoint_timestamp,String appoint_location){
        this.tea_id = "";
        this.order_confirm = order_confirm;
        this.order_status = order_status;
        this.tea_confirm = 0;
        this.stu_id = stu_id;
        this.appoint_location = appoint_location;
        this.appoint_time = appoint_timestamp;
        this.stu_confirm = 0;
    }
}
