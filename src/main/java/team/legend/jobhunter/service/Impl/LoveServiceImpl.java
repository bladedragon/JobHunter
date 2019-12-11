package team.legend.jobhunter.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.legend.jobhunter.dao.LoveDao;
import team.legend.jobhunter.dao.OfferDao;
import team.legend.jobhunter.exception.SqlErrorException;
import team.legend.jobhunter.model.LoveItem;
import team.legend.jobhunter.model.OfferInfo;
import team.legend.jobhunter.service.LoveService;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class LoveServiceImpl implements LoveService {

    @Autowired
    LoveDao loveDao;
    @Autowired
    OfferDao offerDao;

    @Override
    public int love(String stuId,String offerId) throws SqlErrorException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datetime = simpleDateFormat.format(new Date());

        LoveItem loveItems=  loveDao.selectOneByStuId(stuId,offerId);
        int num = 0;
        //标识是否是收藏
        int flag = 0;

        if(loveItems !=null && offerId.equals(loveItems.getOffer_id())) {
            num = loveDao.delete(stuId,offerId);
            flag = 0;
        }else{
            num = loveDao.insert(stuId,datetime,offerId);
            flag =1;
        }

        if(num != 1){
            log.error("cannot insert or delete loveItem stuid[{}], offerId[{}], num = [{}]",stuId,offerId,num);
            throw new SqlErrorException("cannot love ");
        }
        return flag;

    }

    @Override
    public Map<String, Object> getLove(String stuId,int page,int pagesize) {

        Map<String,Object> responseMap = new LinkedHashMap<>();
        List<Map<String,Object>> mapList = new ArrayList<>();
        SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        int num = loveDao.count(stuId);
        responseMap.put("total",num);
        if(num == 0){
            log.info("count = 0");
            return null;
        }
        List<LoveItem> loveItemList = loveDao.selectByStuId(stuId,page,pagesize);
        System.out.println(loveItemList);
        for (LoveItem loveItem: loveItemList) {
            Map<String,Object> map = new LinkedHashMap<>();
//            String date = null;
            OfferInfo offerInfo = offerDao.selectOfferInfoByOfferId(loveItem.getOffer_id());
            log.info("offerINfo:{}",offerInfo);
//            if(offerInfo.getUpdate_timestamp()!=null){
//                 date = simp.format(offerInfo.getUpdate_timestamp());
//            }else{
//                date = "";
//            }
            map.put("offerId",offerInfo.getOffer_id());
            map.put("offer",offerInfo.getOffer_name());
            map.put("company",offerInfo.getOffer_company_name());
            map.put("type",offerInfo.getOffer_type());
            map.put("location",offerInfo.getOffer_location());
            map.put("timestamp",offerInfo.getUpdate_timestamp());
            map.put("loveDate",loveItem.getSave_date());
            map.put("logo",offerInfo.getLogo());


            mapList.add(map);
        }
        responseMap.put("offerList",mapList);
        return responseMap;
    }

    public static void main(String[] args) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        System.out.println(System.currentTimeMillis());
        long num = 1575908857067L;
        Long longnum = num;
        String date = simpleDateFormat.format(num);
        System.out.println(date);
    }

}
