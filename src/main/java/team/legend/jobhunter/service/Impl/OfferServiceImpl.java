package team.legend.jobhunter.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.legend.jobhunter.dao.EvaluateDao;
import team.legend.jobhunter.dao.LoveDao;
import team.legend.jobhunter.dao.OfferDao;
import team.legend.jobhunter.model.DO.OfferDO;
import team.legend.jobhunter.model.LoveItem;

import team.legend.jobhunter.model.OfferInfo;
import team.legend.jobhunter.service.OfferService;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OfferServiceImpl implements OfferService {

    @Autowired
    OfferDao offerDao;
    @Autowired
    LoveDao loveDao;

    @Override
    public Map<String, Object> getDetail(String offerId,String stuId) {
        log.info(">>offerId:[{}]",offerId);
        OfferInfo offerInfo = offerDao.selectOfferInfoByOfferId(offerId);
         if(offerInfo == null){
            log.error(">>log: select offerInfo is null");
            return null;
         }
         Map<String,Object> result = new LinkedHashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        LoveItem loveItem = loveDao.selectOneByStuId(stuId,offerId);

//         String updateDate = null;
//         if(updateDate !=null){
//             updateDate = sdf.format(offerInfo.getUpdate_timestamp());
//         }else{
//             updateDate = "";
//         }
         result.put("offerId",offerInfo.getOffer_id());
         result.put("offer",offerInfo.getOffer_name());
         result.put("company",offerInfo.getOffer_name());
         result.put("type",offerInfo.getOffer_type());
         result.put("location",offerInfo.getOffer_location());
         result.put("timestamp",offerInfo.getUpdate_timestamp());
         result.put("description",offerInfo.getOffer_description());
         result.put("requirment",offerInfo.getOffer_requirement());
         result.put("postway",offerInfo.getOffer_post_way());
         result.put("salary",offerInfo.getOffer_salary());
         result.put("logo",offerInfo.getLogo());

        log.info("love msg:{}",loveItem);
        if(loveItem == null){
            result.put("isLoved",0);
        }else{
            result.put("isloved",1);
        }

         return result;
    }

    @Override
    public Map<String,Object> getOfferList(int page,int pagesize) {
        log.info("getOfferInfo:pagesize:[{}],page:[{}]",pagesize,page);
        List<OfferDO> offerInfos = offerDao.selectOfferInfo(page*pagesize,pagesize);
        Map<String,Object> map = new HashMap<>(2);
        log.info("offerInfos:{}",offerInfos);

        int num = offerDao.getCount();
            if(num != 0){
                if(offerInfos.isEmpty()){
                    return null;
                }
                map.put("total",num);
                map.put("list",offerInfos);
                log.info("num = {}",num);
                return map;
            }else{
                return null;
            }
    }

    @Override
    public List<OfferInfo> refreshOfferList(int page, int pagesize) {

        return null;
    }


}
