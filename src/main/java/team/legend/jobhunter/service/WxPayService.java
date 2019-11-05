package team.legend.jobhunter.service;

import org.springframework.stereotype.Service;
import team.legend.jobhunter.model.Order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Service
public interface WxPayService {

     Map<String ,Object>  wxPay(String nonce_str, Order order, String body,String ipAddr);

     void wxNotify(HttpServletResponse response, HttpServletRequest request);

}
