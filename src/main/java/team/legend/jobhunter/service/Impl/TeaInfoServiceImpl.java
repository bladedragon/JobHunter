package team.legend.jobhunter.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.legend.jobhunter.dao.*;
import team.legend.jobhunter.exception.ParamErrorException;
import team.legend.jobhunter.exception.SqlErrorException;
import team.legend.jobhunter.model.*;
import team.legend.jobhunter.model.DO.TeaDO;
import team.legend.jobhunter.model.DO.TeaInfoDo;
import team.legend.jobhunter.model.DO.WxTeaDO;
import team.legend.jobhunter.service.TeaInfoService;
import team.legend.jobhunter.utils.CommonUtil;
import team.legend.jobhunter.utils.Constant;
import team.legend.jobhunter.utils.IDGenerator;
import team.legend.jobhunter.utils.SecretUtil;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class TeaInfoServiceImpl implements TeaInfoService {

    @Value("${file.imgUrl}")
    private String imgUrl;

    @Autowired
    TeaDao teaDao;
    @Autowired
    WXDao wxDao;
    @Autowired
    OrderDao orderDao;
    @Autowired
    IDGenerator idGenerator;

    @Autowired
    FileDao fileDao;
    @Autowired
    Resume_serviceDao resumeServiceDao;
    @Autowired
    Tutor_serviceDao tutorServiceDao;

    @Override
    public String saveImg(String tea_id,MultipartFile headImg) {
        if(null != headImg ){
            String originFileName = headImg.getOriginalFilename().toLowerCase();
            String[] strs = originFileName.split("\\.");
            String fileSuffix = strs[1] ;
            String preStr = tea_id;
            for (String suffix : Constant.ALLOW_IMG_SUFFIXS) {
                if(fileSuffix.equals(suffix)){
                    String preName = SecretUtil.MD5Encode(preStr);
                    String fileName = preName + CommonUtil.getNowTime();
                    String fileFullName = tea_id+"/"+fileName+"."+fileSuffix;
                    String imgFullUrl = imgUrl+fileFullName;
                    File file = new File(imgFullUrl);
                    try {
                        headImg.transferTo(file);
                        teaDao.uploadImg(tea_id,imgFullUrl);
                        return imgFullUrl;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }else{
            String imgFullUrl = teaDao.selectHeadImgByTeaId(tea_id);
            //老师一定会有头像的
            return imgFullUrl;
        }
        return null;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Map<String,Object> modifyInfo(JSONObject jsonObject,String img_url) throws ParamErrorException {
        String teaId = jsonObject.getString("teaId");
        String nickname = jsonObject.getString("nickname");
        String company = jsonObject.getString("company");
        int isOnline = jsonObject.getInteger("isOnline");
        JSONArray offerArray = jsonObject.getJSONArray("offer");
        String perDes = jsonObject.getString("perDes");
        JSONArray teleArray = jsonObject.getJSONArray("tele");
        String position = jsonObject.getString("position");
        JSONArray serviceTypeArray = jsonObject.getJSONArray("serviceType");
        Map<String,Object> teaInfo = new HashMap<>(12);

        String offers = "";
        for(int i =0;i<offerArray.size();i++){
            if(i!=0){
                offers += ".";
            }
            offers += offerArray.getString(i);
        }
        String teles ="";
        for(int i =0;i<teleArray.size();i++){
            if(i!=0){
                teles += ".";
            }
            teles += teleArray.getString(i);
        }
        String serviceTypes = "";

        int flag = 0;

        for(int i =0;i<serviceTypeArray.size();i++){
            if(i!=0){
                serviceTypes += ".";
            }
            if(serviceTypeArray.get(i).equals("resume")){
                flag += 10;
                System.out.println("插入resume");
            }
            if(serviceTypeArray.get(i).equals("tutor")){
                flag +=1;
                System.out.println("插入tutor");
            }
            serviceTypes += serviceTypeArray.getString(i);
            System.out.println(serviceTypeArray.get(i));
        }

        //老师真实姓名不能更新
        int num = teaDao.updateTea(new TeaDO(teaId,nickname,img_url,teles,"",perDes,offers,company,position,serviceTypes,isOnline));
        Teacher teacher = teaDao.selectByTeaId(teaId);
        if(teacher != null){
            List<String> teleList = CommonUtil.toStrList(teacher.getTea_tele());
            List<String> offerList = CommonUtil.toStrList(teacher.getTea_tag());
            List<String> typeList = CommonUtil.toStrList(teacher.getTea_type());
            teaInfo.put("verifyCode",teacher.getVerify_code());
            teaInfo.put("teaId",teacher.getTea_id());
            teaInfo.put("nickname",teacher.getTea_nickname());
            teaInfo.put("teaName",teacher.getTea_realname());
            teaInfo.put("headImgUrl",teacher.getTea_img_url());
            teaInfo.put("company",teacher.getTea_company());
            teaInfo.put("isOnline",teacher.getIsonline());
            teaInfo.put("offer",offerList);
            teaInfo.put("perDes",teacher.getTea_description());
            teaInfo.put("tele",teleList);
            teaInfo.put("position",teacher.getPosition());
            teaInfo.put("serviceType",typeList);
        }else{
            return null;
        }
        //同步业务到业务数据库，但是前提是保证service_id要和老师ID一一对应
        try{
        switch(flag){
            case 0:
                if(tutorServiceDao.selectServiceId(teaId)!= null){
                    tutorServiceDao.deleteTutor(teaId);
                }
                if(resumeServiceDao.selectServiceId(teaId)!= null){
                    resumeServiceDao.deleteResume(teaId);
                }
                break;
            case 1:
                String tutor_id = idGenerator.createServiceId(teaId);
                tutorServiceDao.insertTutor(new TeaInfoDo(teacher,tutor_id,System.currentTimeMillis()/1000,0));
                String sid1 = resumeServiceDao.selectServiceId(teaId);
                if(sid1!= null && !sid1.equals("")){
                    resumeServiceDao.deleteResume(teaId);
                }
                break;
            case 10:
                String resume_id = idGenerator.createServiceId(teaId);
                resumeServiceDao.inertResume(new TeaInfoDo(teacher,resume_id,System.currentTimeMillis()/1000,0));
                String sid2 = tutorServiceDao.selectServiceId(teaId);
                System.out.println(sid2);
                if(sid2 != null && !sid2.equals("")){
                    tutorServiceDao.deleteTutor(teaId);
                }
                break;
            case 11:
                //重复修改会生成同样的service_id，因此插入才会覆盖
                String service_id = idGenerator.createServiceId(teaId);
                tutorServiceDao.insertTutor(new TeaInfoDo(teacher,service_id,System.currentTimeMillis()/1000,0));
                resumeServiceDao.inertResume(new TeaInfoDo(teacher,service_id,System.currentTimeMillis()/1000,0));
                break;
            default:
                log.error("log>>[{}] service type is not allowed,which is ["+flag+"]",teaId);
                    throw new ParamErrorException("Service type is not allowed ");
            }
        }catch (RuntimeException e){
            throw new RuntimeException();
        }
        return teaInfo;
    }

       @Override
    public Map<String, Object> getTeaInfo(String teaId) {
        Map<String,Object> teaMap = new HashMap<>(5);
        System.out.println(teaId);
        Teacher teacher = teaDao.selectByTeaId(teaId);
           if(teacher != null){
               List<String> teleList = CommonUtil.toStrList(teacher.getTea_tele());
               List<String> offerList = CommonUtil.toStrList(teacher.getTea_tag());
               List<String> typeList = CommonUtil.toStrList(teacher.getTea_type());
               teaMap.put("verifyCode",teacher.getVerify_code());
               teaMap.put("teaId",teacher.getTea_id());
               teaMap.put("nickname",teacher.getTea_nickname());
               teaMap.put("teaName",teacher.getTea_realname());
               teaMap.put("headImgUrl",teacher.getTea_img_url());
               teaMap.put("company",teacher.getTea_company());
               teaMap.put("isOnline",teacher.getIsonline());
               teaMap.put("offer",offerList);
               teaMap.put("perDes",teacher.getTea_description());
               teaMap.put("tele",teleList);
               teaMap.put("position",teacher.getPosition());
               teaMap.put("serviceType",typeList);
           }else{
               return null;
           }

        System.out.println(teacher.getTea_realname());
        if(teacher == null ){
            teaMap.put("fail",null);
        }
        return  teaMap;
    }

    /**
     * 验证码验证
     * @param openid
     * @param verifyCode
     * @param userId
     * @return  MD5+SHA
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    //同时需要老师真实姓名
    public String verify(String openid, String verifyCode, String userId, String realname ) {
        String sign = openid+userId;
        String encodeStr = SecretUtil.shaCheck(openid,userId);
        String originStr = sign +encodeStr;
        String finalStr = SecretUtil.MD5Encode(originStr).toUpperCase();
        String teaId = null;

        if(verifyCode.equals(finalStr)){
           WxTeaDO wxTeaDO =wxDao.selectTeaByUserId(userId);
            String oldTeaId = wxTeaDO.getTea_id();
            if(oldTeaId == null || oldTeaId.equals("")){
                int rank = teaDao.getCount();
                teaId = idGenerator.createTeaId(userId,openid,rank);
                int num =teaDao.insertTea(teaId,wxTeaDO.getNickname(),wxTeaDO.getHeadimg_url(),
                        wxTeaDO.getGender(),realname,verifyCode);
                int result = wxDao.updateTeaId(userId,teaId);
                return teaId;
            }else{
                return "duplication";
            }
        }else{
            return null;
        }
    }

    @Override
    public Map<String, Object> getTeaHome(String teaId) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Map<String,Object> result = new LinkedHashMap<>();
        List<Map<String,Object>> mapList = new ArrayList<>();
        int accomplishNum = orderDao.getCountByTStatus(teaId,1);
        int ordersNum = orderDao.getCountByTeaId(teaId);

        Teacher teacher = teaDao.selectByTeaId(teaId);
        List<String> offerList = CommonUtil.toStrList(teacher.getTea_tag());
        result.put("orderNum",ordersNum);
        result.put("accomplishedNum",accomplishNum);
        result.put("servingNum",ordersNum-accomplishNum);
        result.put("teacher",new TeaHomeInfo(teaId,teacher.getTea_nickname(),teacher.getTea_company(),
                teacher.getIsonline(),offerList));

        List<Order>  orders = orderDao.selectNowDayOrder(teaId,6);

        for (Order order :orders) {
            Map<String,Object> map = new LinkedHashMap<>();

            Date date = null;
            try {
                date = sdf.parse(order.getOrder_date());
            } catch (ParseException e) {
                log.error(">>log: TeaHome: date parse is error");
                e.printStackTrace();
            }
            Long nowTimeStamp = date.getTime();
            String modify_date = sdf.format(order.getOrder_timestamp());
            map.put("orderId",order.getOrder_id());
            map.put("stuId",order.getStu_id());
            map.put("teaId",order.getTea_id());
            map.put("price",order.getPrice());
            map.put("orderType",order.getOrder_type());
            map.put("orderDate",order.getOrder_date());
            map.put("orderTimestamp",nowTimeStamp);
            map.put("modifyDate",modify_date);
            map.put("modifyTimestamp",order.getOrder_timestamp());
            map.put("confirmTime",order.getAppoint_timestamp());
            map.put("confirmLocation",order.getAppoint_location());

            List<String> filePaths = fileDao.selectFilePath(order.getOrder_id());
            StuDetail detail = new StuDetail(order.getRealname(),order.getTele(),order.getExperience(),order.getRequirement(),filePaths);
            map.put("detail",detail);
            mapList.add(map);
        }
        result.put("serving",mapList);

        return result;
    }


    public String getVerifyCode(String openid,String userId){
        String sign = openid+userId;
        String encodeStr = SecretUtil.shaCheck(openid,userId);
        String orignStr = sign+encodeStr;
        String finalStr = SecretUtil.MD5Encode(orignStr).toUpperCase();
        return finalStr;
    }

    public static void main(String[] args) {
        TeaInfoServiceImpl teaInfoService = new TeaInfoServiceImpl();
        String code = teaInfoService.getVerifyCode("12345","09876");
        System.out.println(code);
    }
}
