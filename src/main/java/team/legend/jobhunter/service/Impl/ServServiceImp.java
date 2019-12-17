package team.legend.jobhunter.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.legend.jobhunter.dao.PriceDao;
import team.legend.jobhunter.dao.Resume_serviceDao;
import team.legend.jobhunter.dao.Tutor_serviceDao;
import team.legend.jobhunter.model.DO.ShowTeaDO;
import team.legend.jobhunter.model.DO.TeaDO;
import team.legend.jobhunter.model.PriceItem;
import team.legend.jobhunter.service.ServService;
import team.legend.jobhunter.utils.CommonUtil;
import team.legend.jobhunter.utils.Constant;

import java.util.*;

@Slf4j
@Service
public class ServServiceImp implements ServService {

    @Autowired
    Resume_serviceDao resumeServiceDao;
    @Autowired
    Tutor_serviceDao tutorServiceDao;
    @Autowired
    PriceDao priceDao;


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
            Map<String,Integer> prices = new HashMap<>(2);
            Map<String,Integer> discounts = new HashMap<>(2);

            Map<String,Object> showTeaMap = new LinkedHashMap<>();
            String offorStr = showTeaDO.getTea_tag();
            List<String> tags = CommonUtil.toStrList(offorStr);
            showTeaMap.put("service_id",showTeaDO.getService_id());
            showTeaMap.put("tea_id",showTeaDO.getTea_id());
            showTeaMap.put("tea_nickname",showTeaDO.getTea_nickname());
            showTeaMap.put("tea_img_url",showTeaDO.getTea_img_url());
            showTeaMap.put("tea_description",showTeaDO.getTea_description());
            showTeaMap.put("tea_tag",tags);
            showTeaMap.put("tea_company",showTeaDO.getTea_company());
            showTeaMap.put("gender",showTeaDO.getTea_gender());
            showTeaMap.put("isonline",showTeaDO.getIsonline());
            showTeaMap.put("service_timestamp",showTeaDO.getService_timestamp());
            showTeaMap.put("service_status",showTeaDO.getService_status());
            showTeaMap.put("position",showTeaDO.getPosition());
            switch (showTeaDO.getIsonline()){
                case 1:
                    PriceItem priceItem = priceDao.selectPriceByType(Constant.OFFLINE_PRICE_STATUS);
                    prices.put("offlinePrice",priceItem.getPrice());
                    discounts.put("offlineDiscount",priceItem.getDiscount());
                    break;
                case 0:
                    PriceItem offPrice = priceDao.selectPriceByType(Constant.ONLINE_PRICE_STATUS);
                    prices.put("onlinePrice",offPrice.getPrice());
                    discounts.put("onlineDiscount",offPrice.getDiscount());
                    break;
                case 2:
                    PriceItem priceItem2 = priceDao.selectPriceByType(Constant.OFFLINE_PRICE_STATUS);
                    PriceItem priceItem3 =priceDao.selectPriceByType(Constant.ONLINE_PRICE_STATUS);
                    prices.put("onlinePrice",priceItem3.getPrice());
                    prices.put("offlinePrice",priceItem2.getPrice());
                    discounts.put("onlineDiscount",priceItem3.getDiscount());
                    discounts.put("offlineDiscount",priceItem2.getDiscount());
                    break;
                default:
                    prices.put("ErrorPrice",Constant.INFINITE_PRICE);
                    discounts.put("EeeorDiscount",0);
                    log.error(">>log: price select met exception when ger isOnline: [{}]",showTeaDO.getIsonline());
                    break;
            }
            showTeaMap.put("price",prices);
            showTeaMap.put("discount",discounts);
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

            Map<String,Integer> prices = new HashMap<>(2);
            Map<String,Integer> discounts = new HashMap<>(2);

            Map<String,Object> showTeaMap = new LinkedHashMap<>();
            String offorStr = showTeaDO.getTea_tag();
            List<String> tags = CommonUtil.toStrList(offorStr);
            showTeaMap.put("service_id",showTeaDO.getService_id());
            showTeaMap.put("tea_id",showTeaDO.getTea_id());
            showTeaMap.put("tea_nickname",showTeaDO.getTea_nickname());
            showTeaMap.put("tea_img_url",showTeaDO.getTea_img_url());
            showTeaMap.put("tea_description",showTeaDO.getTea_description());
            showTeaMap.put("tea_tag",tags);
            showTeaMap.put("tea_company",showTeaDO.getTea_company());
            showTeaMap.put("gender",showTeaDO.getTea_gender());
            showTeaMap.put("isonline",showTeaDO.getIsonline());
            showTeaMap.put("service_timestamp",showTeaDO.getService_timestamp());
            showTeaMap.put("service_status",showTeaDO.getService_status());
            showTeaMap.put("position",showTeaDO.getPosition());

            switch (showTeaDO.getIsonline()){
                case 1:
                    PriceItem priceItem = priceDao.selectPriceByType(Constant.OFFLINE_PRICE_STATUS);
                    prices.put("offlinePrice",priceItem.getPrice());
                    discounts.put("offlineDiscount",priceItem.getDiscount());
                    break;
                case 0:
                    PriceItem priceItem1 = priceDao.selectPriceByType(Constant.ONLINE_PRICE_STATUS);
                    prices.put("onlinePrice",priceItem1.getPrice());
                    discounts.put("onlineDiscount",priceItem1.getDiscount());
                    break;
                case 2:
                    PriceItem priceItem2 = priceDao.selectPriceByType(Constant.OFFLINE_PRICE_STATUS);
                    PriceItem priceItem3 =priceDao.selectPriceByType(Constant.ONLINE_PRICE_STATUS);
                    prices.put("onlinePrice",priceItem3.getPrice());
                    prices.put("offlinePrice",priceItem2.getPrice());
                    discounts.put("onlineDiscount",priceItem3.getDiscount());
                    discounts.put("offlineDiscount",priceItem2.getDiscount());
                    break;
                default:
                    prices.put("ErrorPrice",Constant.INFINITE_PRICE);
                    discounts.put("EeeorDiscount",0);
                    log.error(">>log: price select met exception when ger isOnline: [{}]",showTeaDO.getIsonline());
                    break;
            }
            showTeaMap.put("price",prices);
            showTeaMap.put("discount",discounts);
            dataList.add(showTeaMap);
        }

        map.put("list",dataList);
        return map;

    }
}
