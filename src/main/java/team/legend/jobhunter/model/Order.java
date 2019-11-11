package team.legend.jobhunter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.omg.PortableInterceptor.INACTIVE;
import team.legend.jobhunter.utils.CommonUtil;

@AllArgsConstructor
@Data
public class Order {
    private String order_id;
    private String tea_id;
    private String stu_id;
    private Integer discount;
    private Integer price;
    private String order_type;
    //状态为0 表示服务尚未确认  状态为1表示服务已确认  状态为2表示服务结束
    private Integer order_status;
    private Integer order_actual_payment;
    //双方预约时间戳
    private Long appoint_timestamp;
    private Integer order_comfirm;
    //订单创建时间
    private String order_date;
    //修改的时间戳
    private Long order_timestamp;
    private Integer iseffective;
    private Integer isonline;
    private Integer tea_confirm;
    private Integer stu_confirm;
    private String requirement;
    private String experience;
    private String tele;
    private String realname;


    public Order(String order_id,String tea_id,String stu_id,int discount,int price,String order_type,Integer iseffective,
          Integer isonline,String requirement,String experience,String tele,String realname){
        this.order_status = 0;
        this.order_actual_payment = null;
        this.appoint_timestamp = null;
        this.order_comfirm = null;
        this.order_date = CommonUtil.getNowDate();
        this.order_timestamp = System.currentTimeMillis()/1000;
        this.tea_confirm = null;
        this.stu_confirm = null;

        this.order_id = order_id;
        this.tea_id = tea_id;
        this.stu_id = stu_id;
        this.discount = discount;
        this.price=  price;
        this.order_type = order_type;
        this.iseffective = iseffective;
        this.isonline = isonline;
        this.realname = realname;
        this.requirement = requirement;
        this.experience = experience;
        this.tele = tele;
    }


}
