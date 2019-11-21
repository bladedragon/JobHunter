package team.legend.jobhunter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.omg.PortableInterceptor.INACTIVE;
import team.legend.jobhunter.model.DO.OrderTeaDO;
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
    private String appoint_location;
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



    private String tea_nickname;
    private String tea_tele;
    private String tea_tag;
    private String tea_img_url;
    private Integer tea_gender;
    private String tea_email;
    private String tea_description;
    private String tea_company;
    private String position;


    public Order(String order_id, String tea_id, String stu_id, int discount, int price, String order_type, Integer iseffective,
                 Integer isonline, String requirement, String experience, String tele, String realname,
                  String tea_nickname,String tea_tele,String tea_tag,String tea_img_url,Integer tea_gender,String tea_email,String tea_description,String tea_company,String position){
        this.order_status = 0;
        this.order_actual_payment = null;
        this.appoint_timestamp = null;
        this.order_comfirm = null;
        this.order_date = CommonUtil.getNowDate("yyyy-MM-dd HH:mm:ss");
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

        this.tea_nickname = tea_nickname;
        this.tea_company = tea_company;
        this.tea_description = tea_description;
        this.tea_email = tea_email;
        this.tea_gender = tea_gender;
        this.tea_img_url = tea_img_url;
        this.tea_tag = tea_tag;
        this.tea_tele = tea_tele;
        this.position = position;
    }


    public Order(String order_id, String tea_id, String stu_id, Integer discount, Integer price, String order_type,
                 Integer order_status, Integer order_actual_payment, String appoint_location, Long appoint_timestamp, Integer order_comfirm,
                 String order_date, Long order_timestamp, Integer iseffective, Integer isonline, Integer tea_confirm, Integer stu_confirm,
                 String requirement, String experience, String tele, String realname, String tea_nickname, String tea_tele, String tea_tag,
                 String tea_img_url, Integer tea_gender, String tea_email, String tea_description, String tea_company, String position,String unKonw) {
        this.order_id = order_id;
        this.tea_id = tea_id;
        this.stu_id = stu_id;
        this.discount = discount;
        this.price = price;
        this.order_type = order_type;
        this.order_status = order_status;
        this.order_actual_payment = order_actual_payment;
        this.appoint_location = appoint_location;
        this.appoint_timestamp = appoint_timestamp;
        this.order_comfirm = order_comfirm;
        this.order_date = order_date;
        this.order_timestamp = order_timestamp;
        this.iseffective = iseffective;
        this.isonline = isonline;
        this.tea_confirm = tea_confirm;
        this.stu_confirm = stu_confirm;
        this.requirement = requirement;
        this.experience = experience;
        this.tele = tele;
        this.realname = realname;
        this.tea_nickname = tea_nickname;
        this.tea_tele = tea_tele;
        this.tea_tag = tea_tag;
        this.tea_img_url = tea_img_url;
        this.tea_gender = tea_gender;
        this.tea_email = tea_email;
        this.tea_description = tea_description;
        this.tea_company = tea_company;
        this.position = position;
        System.out.println("unKnow:"+unKonw);
    }
}
