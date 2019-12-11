package team.legend.jobhunter.service;


import team.legend.jobhunter.model.DO.OfferDO;
import team.legend.jobhunter.model.OfferInfo;

import java.util.List;
import java.util.Map;

public interface OfferService {

    Map<String,Object> getDetail(String offerId,String stuId);

    Map<String,Object> getOfferList(int page, int pagesize);

    List<OfferInfo> refreshOfferList(int page,int pagesize);
}
