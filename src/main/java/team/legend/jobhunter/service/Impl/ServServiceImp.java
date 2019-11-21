package team.legend.jobhunter.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.legend.jobhunter.dao.Resume_serviceDao;
import team.legend.jobhunter.dao.Tutor_serviceDao;
import team.legend.jobhunter.model.DO.ShowTeaDO;
import team.legend.jobhunter.model.DO.TeaDO;
import team.legend.jobhunter.service.ServService;
import team.legend.jobhunter.utils.CommonUtil;

import java.util.*;

@Slf4j
@Service
public class ServServiceImp implements ServService {

    @Autowired
    Resume_serviceDao resumeServiceDao;
    @Autowired
    Tutor_serviceDao tutorServiceDao;


    @Override
    public Map<String, Object> showResumeInfo(int pagesize, int page) {
        Map<String,Object> map = new LinkedHashMap<>();
        int num = resumeServiceDao.getCount();
        if(num <= 0){
            log.error(">>log showResumeInfo is empty,num = [{}]",num);
            map.put("empty",null);
            return map;
        }else{
            map.put("total",num);
        }
        List<ShowTeaDO> resumeInfo = resumeServiceDao.selectTeaInfo(pagesize,page);

        List<Map<String,Object>> dataList = new ArrayList<>();

        for (ShowTeaDO showTeaDO:resumeInfo) {
            Map<String,Object> showTeaMap = new LinkedHashMap<>();
            String offorStr = showTeaDO.getTea_tag();
            List<String> tags = CommonUtil.toStrList(offorStr);
            showTeaMap.put("service_id",showTeaDO.getService_id());
            showTeaMap.put("tea_id",showTeaDO.getTea_id());
            showTeaMap.put("tea_img_url",showTeaDO.getTea_img_url());
            showTeaMap.put("tea_description",showTeaDO.getTea_description());
            showTeaMap.put("tea_tag",tags);
            showTeaMap.put("tea_company",showTeaDO.getTea_company());
            showTeaMap.put("gender",showTeaDO.getTea_gender());
            showTeaMap.put("isonline",showTeaDO.getIsonline());
            showTeaMap.put("service_timestamp",showTeaDO.getService_timestamp());
            showTeaMap.put("service_status",showTeaDO.getService_status());
            showTeaMap.put("position",showTeaDO.getPosition());
            dataList.add(showTeaMap);

        }

        map.put("list",dataList);
        return map;
    }

    @Override
    public Map<String, Object> showTutorInfo(int pagesize, int page) {

        Map<String,Object> map = new LinkedHashMap<>();
        int num = tutorServiceDao.getCount();
        if(num <= 0){
            log.error(">>log showTutorInfo is empty,num = [{}]",num);
            map.put("empty",null);
            return map;
        }else{
            map.put("total",num);
        }
        List<ShowTeaDO> tutorInfo = tutorServiceDao.selectTeaInfo(pagesize,page);
        List<Map<String,Object>> dataList = new ArrayList<>();

        for (ShowTeaDO showTeaDO:tutorInfo) {
            Map<String,Object> showTeaMap = new LinkedHashMap<>();
            String offorStr = showTeaDO.getTea_tag();
            List<String> tags = CommonUtil.toStrList(offorStr);
            showTeaMap.put("service_id",showTeaDO.getService_id());
            showTeaMap.put("tea_id",showTeaDO.getTea_id());
            showTeaMap.put("tea_img_url",showTeaDO.getTea_img_url());
            showTeaMap.put("tea_description",showTeaDO.getTea_description());
            showTeaMap.put("tea_tag",tags);
            showTeaMap.put("tea_company",showTeaDO.getTea_company());
            showTeaMap.put("gender",showTeaDO.getTea_gender());
            showTeaMap.put("isonline",showTeaDO.getIsonline());
            showTeaMap.put("service_timestamp",showTeaDO.getService_timestamp());
            showTeaMap.put("service_status",showTeaDO.getService_status());
            showTeaMap.put("position",showTeaDO.getPosition());
            dataList.add(showTeaMap);
        }

        map.put("list",dataList);
        return map;

    }
}
