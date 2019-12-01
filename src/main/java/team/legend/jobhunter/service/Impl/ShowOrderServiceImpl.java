package team.legend.jobhunter.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.legend.jobhunter.dao.FileDao;
import team.legend.jobhunter.dao.OrderDao;
import team.legend.jobhunter.model.DO.FileDO;
import team.legend.jobhunter.model.Detail;
import team.legend.jobhunter.model.Order;
import team.legend.jobhunter.service.ShowOrderService;
import team.legend.jobhunter.utils.CommonUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class ShowOrderServiceImpl implements ShowOrderService {

    @Autowired
    OrderDao orderDao;
    @Autowired
    FileDao fileDao;

    @Override
    public Map<String,Object> showOrder(String userId,int isTea,int isAccomplished,int page,int pagesize) {
        Map<String, Object> result = new LinkedHashMap<>();
        List<Map<String,Object>> mapList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        List<Order> orderList = null;
        int count = 0;

        if(isTea == 1){
            orderList = orderDao.selectByTeaId(userId,isAccomplished,page,pagesize);
            count = orderDao.getCountByTStatus(userId,isAccomplished);

        }else{
            orderList = orderDao.selectByStuId(userId,isAccomplished,page,pagesize);
            count = orderDao.getCountBySStatus(userId,isAccomplished);

        }

        if(orderList == null ||orderList.isEmpty()){
            log.error("cannot find orders ,userId :[{}],is Teacher? [{}]",userId,isTea);
            return null;
        }

        log.info("select list is [{}]",count);
        result.put("total",count);
        for (Order order: orderList) {
            Map<String,Object> map = new LinkedHashMap<>();
            Date date = null;
            try {
                date = sdf.parse(order.getOrder_date());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long nowTimeStamp = date.getTime();
            String modify_date = sdf.format(order.getOrder_timestamp());

            map.put("orderId",order.getOrder_id());
            map.put("stuId",order.getStu_id());
            map.put("teaId",order.getTea_id());
            map.put("price",order.getPrice());
            map.put("orderType",order.getOrder_type());
            map.put("orderStatus",order.getOrder_status());
            map.put("orderDate",order.getOrder_date());
            map.put("orderTimestamp",nowTimeStamp);
            map.put("modifyDate",modify_date);
            map.put("modifyTimestamp",order.getOrder_timestamp());
            if(isAccomplished == 0){
                map.put("teaConfirm",order.getTea_confirm());
                map.put("stuConfirm", order.getStu_confirm());
                map.put("isConfirm",order.getOrder_comfirm());
            }

            map.put("confirmTime",order.getAppoint_timestamp());
            map.put("confirmLocation",order.getAppoint_location());


            List<String> offerList = CommonUtil.toStrList(order.getTea_tag());
            List<FileDO> filePaths = fileDao.selectFilePath(order.getOrder_id(),0);
            List<FileDO> teaFiles = fileDao.selectFilePath(order.getOrder_id(),1);
            Detail detail = new Detail(order.getTea_nickname(),order.getTea_img_url(),order.getTea_gender(),
                    order.getPosition(), order.getTea_company(),order.getIsonline(),offerList,order.getTea_description(),
                    order.getRealname(),order.getTele(),order.getExperience(),order.getRequirement(),filePaths,teaFiles);
            map.put("detail",detail);
            mapList.add(map);
        }
        result.put("list",mapList);
        return result;
    }


}
