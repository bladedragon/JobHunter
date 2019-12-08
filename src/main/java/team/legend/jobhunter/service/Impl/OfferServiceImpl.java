package team.legend.jobhunter.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.legend.jobhunter.dao.EvaluateDao;
import team.legend.jobhunter.dao.LoveDao;
import team.legend.jobhunter.dao.OfferDao;
import team.legend.jobhunter.model.LoveItem;

import team.legend.jobhunter.model.OfferInfo;
import team.legend.jobhunter.service.OfferService;

import java.text.SimpleDateFormat;
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
        OfferInfo offerInfo = offerDao.selectOfferInfoByOfferId(offerId);
         if(offerInfo == null){
            log.error(">>log: select offerInfo is null");
            return null;
         }
         Map<String,Object> result = new LinkedHashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        LoveItem loveItem = loveDao.selectOneByStuId(stuId,offerId);

         String updateDate = sdf.format(offerInfo.getUpdate_timestamp());
         result.put("offerId",offerInfo.getOffer_id());
         result.put("offer",offerInfo.getOffer_name());
         result.put("company",offerInfo.getOffer_name());
         result.put("type",offerInfo.getOffer_type());
         result.put("locatioon",offerInfo.getOffer_location());
         result.put("udpate",updateDate);
         result.put("description",offerInfo.getOffer_description());
         result.put("requirment",offerInfo.getOffer_requirement());
         result.put("post_way",offerInfo.getOffer_post_way());
        if(loveItem == null){
            result.put("isLoved",0);
        }else{
            result.put("isloved",1);
        }

         return result;
    }

    @Override
    public List<OfferInfo> getOfferList(int page,int pagesize) {
        List<OfferInfo> offerInfos = offerDao.selectOfferInfo(pagesize,page);
            if(offerInfos != null &&!offerInfos.isEmpty()){
                return offerInfos;
            }

        return null;
    }

    @Override
    public List<OfferInfo> refreshOfferList(int page, int pagesize) {


        return null;
    }


}
