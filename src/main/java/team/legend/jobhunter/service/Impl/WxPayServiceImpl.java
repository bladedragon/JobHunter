package team.legend.jobhunter.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import team.legend.jobhunter.dao.WXDao;
import team.legend.jobhunter.model.DO.StuDO;
import team.legend.jobhunter.model.Order;
import team.legend.jobhunter.service.WxPayService;
import team.legend.jobhunter.utils.HttpUtil;
import team.legend.jobhunter.utils.IPAddrUtil;
import team.legend.jobhunter.utils.PayUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class WxPayServiceImpl implements WxPayService {

    @Value("${wxapi.appid}")
    private String appid;

    @Value("${wxapi.appSecret}")
    private String secretId;
    @Value("${wxapi.mch_id}")
    private String mch_id;
    @Value("${wxapi.notify_url}")
    private String notify_url;
    @Value("${wxapi.TRADETYPE}")
    private String TRADETYPE;
    @Value("${wxapi.key}")
    private  String key;
    @Value("${wxapi.pay_url}")
    private String pay_url;

    @Autowired
    WXDao wxDao;

    @Override
    public Map<String, Object> wxPay(String nonce_str , Order order, String body,String ipAddr) {

        //之前还需要对调用api的用户的openid和查出来的opneid做校验，待完善
        String openid = wxDao.selectOpenidByUserId(order.getStu_id());

        Map<String, String> packageParams = new HashMap<>();
        packageParams.put("appid", appid);
        packageParams.put("mch_id", mch_id);
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("body", body);
        //商户订单号,自己的订单ID
        packageParams.put("out_trade_no", order.getOrder_id() + "");
        //支付金额，这边需要转成字符串类型，否则后面的签名会失败
        packageParams.put("total_fee", order.getItem_price() + "");
        packageParams.put("spbill_create_ip", ipAddr);
        //支付成功后的回调地址
        packageParams.put("notify_url", notify_url);
        //支付方式
        packageParams.put("trade_type", TRADETYPE);
        //用户的openID，自己获取
        packageParams.put("openid", openid + "");

        String prestr = PayUtil.createLinkString(packageParams);
        String mysign = PayUtil.sign(prestr,key,"utf-8").toUpperCase();
        String xml = "<xml>" + "<appid>" + appid + "</appid>"
                + "<body><![CDATA[" + body + "]]></body>"
                + "<mch_id>" + mch_id + "</mch_id>"
                + "<nonce_str>" + nonce_str + "</nonce_str>"
                + "<notify_url>" + notify_url + "</notify_url>"
                + "<openid>" + openid + "</openid>"
                + "<out_trade_no>" + order.getOrder_id() + "</out_trade_no>"
                + "<spbill_create_ip>" + ipAddr + "</spbill_create_ip>"
                + "<total_fee>" + order.getItem_price() + "</total_fee>"//支付的金额，单位：分
                + "<trade_type>" + TRADETYPE + "</trade_type>"
                + "<sign>" + mysign + "</sign>"
                + "</xml>";

        Map<String, Object> response = new HashMap<String, Object>();
        String result = HttpUtil.httpPost(xml,pay_url);
        try {
            Map map = PayUtil.doXMLParse(result);
            String return_code = (String) map.get("return_code");
            String result_code = (String) map.get("result_code");
            if(return_code == "SUCCESS" && return_code.equals(result_code)){
                //返回的预付单信息
                String prepay_id = (String) map.get("prepay_id");
                response.put("nonceStr", nonce_str);
                response.put("package", "prepay_id=" + prepay_id);
                Long timeStamp = System.currentTimeMillis() / 1000;
                //这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                response.put("timeStamp", timeStamp + "");
                //拼接签名需要的参数
                String stringSignTemp = "appId=" + appid + "&nonceStr=" + nonce_str + "&package=prepay_id=" + prepay_id + "&signType=MD5&timeStamp=" + timeStamp;
                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                String paySign = PayUtil.sign(stringSignTemp, key, "utf-8").toUpperCase();
                response.put("paySign", paySign);
                response.put("appid", appid);
            }
            if(return_code.equals("SUCCESS") && !return_code.equals(result_code)){
                response.put("Result_Fail",map.get("err_code"));
                response.put("err_code_des",map.get("err_code_des"));;
            }
            if(return_code.equals("FAIL")){
                response.put("Return_Fail",map.get("return_msg"));

            }

            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void wxNotify(HttpServletResponse response, HttpServletRequest request){
        BufferedReader br = null;
        String notityXml = null;
        try {
            br = new BufferedReader(new InputStreamReader(request.getInputStream()));

            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();
            //sb为微信返回的xml
            notityXml = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String resXml = "";
        Map map = null;
        try {
            map = PayUtil.doXMLParse(notityXml);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String returnCode = (String) map.get("return_code");
        if ("SUCCESS".equals(returnCode)) {
            //验证签名是否正确
            Map<String, String> validParams = PayUtil.paraFilter(map);  //回调验签时需要去除sign和空值参数
            String prestr = PayUtil.createLinkString(validParams);
            //此处不仅对回调的参数进行验签，还需要对返回的金额与系统订单的金额进行比对等
            if (PayUtil.verify(prestr, (String) map.get("sign"), key, "utf-8")) {
                /**此处添加自己的业务逻辑代码start**/
                //获取订单信息，查询金额进行对比
                //获取订单的是否支付信息，如果是则跳过
                //注意要判断微信支付重复回调，支付成功后微信会重复的进行回调


                /**此处添加自己的业务逻辑代码end**/

                //通知微信服务器已经支付成功
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            }
        } else {
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }

        BufferedOutputStream out = null;
        try {
            out = new BufferedOutputStream(
                    response.getOutputStream());
            out.write(resXml.getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
