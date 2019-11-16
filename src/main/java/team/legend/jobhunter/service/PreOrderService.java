package team.legend.jobhunter.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;
import team.legend.jobhunter.exception.SqlErrorException;
import team.legend.jobhunter.exception.UploadException;

import java.util.List;
import java.util.Map;

public interface PreOrderService {


//    Map<String, PreOrder> createPreOrder(String service_type, String service_id, String stu_id, String tea_id, TimeItem timeItem);

    int uploadFile(List<MultipartFile> files,String preOrderId) throws UploadException;

    Map<String, String> createPreOrder(JSONObject jsonObject) throws SqlErrorException;

    int cancelPreOrder(String stuId,String preOrderId);
}