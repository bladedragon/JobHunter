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
        if(offerId.equals(loveItems.getOffer_id())) {
            num = loveDao.delete(stuId);
            flag = 1;
        }else{
            num = loveDao.insert(stuId,datetime,offerId);
            flag =2;
        }

        if(num != 1){
            log.error("cannot insert or delete loveItem stuid[{}], offerId[{}]",stuId,offerId);
            throw new SqlErrorException("cannot love ");
        }
        return flag;

    }

    @Override
    public Map<String, Object> getLove(String stuId) {

        Map<String,Object> responseMap = new LinkedHashMap<>();
        List<Map<String,Object>> mapList = new ArrayList<>();
        SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Map<String,Object> map = new LinkedHashMap<>();
        int num = loveDao.count(stuId);
        responseMap.put("total",num);
        List<LoveItem> loveItemList = loveDao.selectByStuId(stuId);
        for (LoveItem loveItem: loveItemList) {
            OfferInfo offerInfo = offerDao.selectOfferInfoByOfferId(loveItem.getOffer_id());
            String date = simp.format(offerInfo.getUpdate_timestamp());
            map.put("offerId",offerInfo.getOffer_id());
            map.put("offer",offerInfo.getOffer_name());
            map.put("company",offerInfo.getOffer_company_name());
            map.put("type",offerInfo.getOffer_type());
            map.put("location",offerInfo.getOffer_location());
            map.put("update_date",date);
            map.put("love_date",loveItem.getSave_date());
            mapList.add(map);
        }
        responseMap.put("offerList",mapList);
        return responseMap;
    }

}
