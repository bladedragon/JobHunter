package team.legend.jobhunter.service;

import org.springframework.web.multipart.MultipartFile;
import team.legend.jobhunter.exception.OrderErrorException;
import team.legend.jobhunter.exception.SqlErrorException;
import team.legend.jobhunter.exception.UploadException;
import team.legend.jobhunter.model.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {

    Map<String, Object> createOrder(String tea_id,String stu_id,String service_id,String service_type,int price,
                                   int discount,int isonline,String realname,String tele,String experience,String requirement) throws OrderErrorException, SqlErrorException;

    Map<String,Object> createOrder(String preOrderId) throws SqlErrorException;

    int uploadFile(List<MultipartFile> files, String orderId,int isTea) throws UploadException;

}
