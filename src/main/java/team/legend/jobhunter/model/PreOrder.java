package team.legend.jobhunter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.legend.jobhunter.utils.CommonUtil;

import javax.smartcardio.CommandAPDU;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreOrder {
    private String preorder_id;
    private String service_id;
    private String tea_id;
    private String stu_id;
    private String realname;
    private String tele;
    private String experience;
    private String requirement;
    private Integer isonline;
    private String create_date;
    private String order_type;
    private Long expire;
    private Integer price;
    private Integer discount;
    private Long timestamp;


    public PreOrder(String preOrder_id ,String service_id,String tea_id,String stu_id,
                    String realname,String tele,String experience,String requirement,Integer isonline,
                    String order_type,Integer price,Integer discount,Integer day){
        //过期时间原时间+3天
        this.expire = System.currentTimeMillis()/1000+day*24*3600;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.create_date = simpleDateFormat.format(new Date());
        this.preorder_id = preOrder_id;
        this.service_id = service_id;
        this.tea_id = tea_id;
        this.stu_id = stu_id;
        this.realname = realname;
        this.tele = tele;
        this.experience  = experience;
        this.requirement = requirement;
        this.isonline = isonline;
        this.order_type = order_type;
        this.price = price;
        this.discount = discount;
        this.timestamp = System.currentTimeMillis();

    }
}
