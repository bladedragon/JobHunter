package team.legend.jobhunter.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.legend.jobhunter.dao.Resume_serviceDao;
import team.legend.jobhunter.dao.TeaDao;
import team.legend.jobhunter.dao.Tutor_serviceDao;
import team.legend.jobhunter.dao.WXDao;
import team.legend.jobhunter.exception.SqlErrorException;
import team.legend.jobhunter.model.DO.TeaDO;
import team.legend.jobhunter.model.Teacher;
import team.legend.jobhunter.service.TeaInfoService;
import team.legend.jobhunter.utils.CommonUtil;
import team.legend.jobhunter.utils.Constant;
import team.legend.jobhunter.utils.IDGenerator;
import team.legend.jobhunter.utils.SecretUtil;

import java.io.File;
import java.io.IOException;
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
    IDGenerator idGenerator;

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
            String preStr = strs[0];
            for (String suffix : Constant.ALLOW_SUFFIXS) {
                if(fileSuffix.equals(suffix)){
                    String preName = SecretUtil.MD5Encode(preStr);
                    String fileName = preName + CommonUtil.getNowTime();
                    String fileFullName = fileName+"."+fileSuffix;
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
            return imgFullUrl;
        }
        return null;
    }

    @Transactional(rollbackFor = SqlErrorException.class)
    @Override
    public Map<String,Object> modifyInfo(JSONObject jsonObject,String img_url) {
        String teaId = jsonObject.getString("teaId");
        String teaName = jsonObject.getString("teaName");
        String nickname = jsonObject.getString("nickname");
        String company = jsonObject.getString("company");
        int isOnline = jsonObject.getInteger("isOnline");
        JSONArray offerArray = jsonObject.getJSONArray("offer");
        String perDes = jsonObject.getString("perDes");
        JSONArray teleArray = jsonObject.getJSONArray("tele");
        String positon = jsonObject.getString("positon");
        JSONArray serviceTypeArray = jsonObject.getJSONArray("serviceType");
        Map<String,Object> teaInfo = new HashMap<>();

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
        for(int i =0;i<serviceTypeArray.size();i++){
            if(i!=0){
                serviceTypes += ".";
            }
            if(serviceTypeArray.get(i).equals("resume")){
                teaDao.updateResumeInfo(teaId,nickname,img_url,perDes,offers,company,isOnline,
                        System.currentTimeMillis()/1000,positon);
            }
            if(serviceTypeArray.get(i).equals("tutor")){
                teaDao.updateTutorInfo(teaId,nickname,img_url,perDes,offers,company,isOnline,
                        System.currentTimeMillis()/1000,positon);
            }
            serviceTypes += serviceTypeArray.getString(i);
        }

        int num = teaDao.updateTea(new TeaDO(teaId,nickname,teaName,img_url,teles,"",perDes,offers,company,positon,serviceTypes,isOnline));
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
            return teaInfo;
        }
        return null;
    }

    @Override
    public Map<String, Object> getTeaInfo(String teaId) {
        Map<String,Object> teaMap = new HashMap<>(5);
        System.out.println(teaId);
            Teacher teacher = teaDao.selectByTeaId(teaId);
            teaMap.put("teacher",teacher);
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
    @Transactional(rollbackFor = SqlErrorException.class)
    public String verify(String openid, String verifyCode, String userId) {
        String sign = openid+userId;
        String encodeStr = SecretUtil.shaCheck(openid,userId);
        String originStr = sign +encodeStr;
        String finalStr = SecretUtil.MD5Encode(originStr).toUpperCase();
        String teaId = null;

        if(verifyCode.equals(finalStr)){
            String oldTeaId =wxDao.selectTeaIdByUserId(userId);
            System.out.println(oldTeaId);
            if(oldTeaId == null || oldTeaId.equals("")){
                int rank = teaDao.getCount();
                teaId = idGenerator.createTeaId(userId,openid,rank);
                int num =teaDao.insertTea(teaId,verifyCode);
                int result = wxDao.updateTeaId(userId,teaId);
                return teaId;
            }else{
                return "duplication";
            }
        }else{
            return null;
        }
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
