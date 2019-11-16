package team.legend.jobhunter.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.legend.jobhunter.dao.FileDao;
import team.legend.jobhunter.dao.PreOrderDao;
import team.legend.jobhunter.dao.ShowPreOrderDao;
import team.legend.jobhunter.dao.TeaDao;
import team.legend.jobhunter.model.DO.OrderTeaDO;
import team.legend.jobhunter.model.Detail;
import team.legend.jobhunter.model.PreOrder;
import team.legend.jobhunter.model.Teacher;
import team.legend.jobhunter.service.ShowPreOrderService;
import team.legend.jobhunter.utils.CommonUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class ShowPreOrderServiceImpl implements ShowPreOrderService {

    @Autowired
    PreOrderDao preOrderDao;
    @Autowired
    TeaDao teaDao;
    @Autowired
    FileDao fileDao;

    @Override
    public List<Map<String,Object>> showPreOrder(String userId) {


        List<Map<String,Object>> mapList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        List<PreOrder> preOrderList = preOrderDao.selectByStuId(userId);

        for (PreOrder preOrder: preOrderList) {
            Map<String ,Object> map = new LinkedHashMap<>();
            map.put("preOrderId",preOrder.getPreorder_id());
            map.put("teaId",preOrder.getTea_id());
            map.put("stuId",preOrder.getStu_id());
            map.put("orderDate",preOrder.getCreate_date());

            Date date = null;
            try {
                date = sdf.parse(preOrder.getCreate_date());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long nowTimeStamp = date.getTime();
            String modify_date = sdf.format(preOrder.getTimestamp());

            map.put("orderTimeStamp",nowTimeStamp);
            map.put("modifyDate",modify_date);
            map.put("modifyTimeStamp",preOrder.getTimestamp());
            map.put("orderType",preOrder.getOrder_type());
            map.put("price",preOrder.getPrice());
            map.put("discount",preOrder.getDiscount());
//            Map<String,Object> detail = new HashMap<>(13);

            OrderTeaDO teacher = teaDao.selectOrderDoBYTeaId(preOrder.getTea_id());
            if(teacher == null){
                log.error("cannot find teacher info,teaId :[{}]",preOrder.getTea_id());
                List<String> filePaths = fileDao.selectFilePath(preOrder.getPreorder_id());
                Detail detail = new Detail("","",0,"","",0,
                        null,"",preOrder.getRealname(),preOrder.getTele(),preOrder.getExperience(),preOrder.getRequirement(),filePaths);
//                detail.put("teaError",1);
//                detail.put("stuRealname",preOrder.getRealname());
//                detail.put("stuTele",preOrder.getTele());
//                detail.put("stuExperience",preOrder.getExperience());
//                detail.put("stuGuidance",preOrder.getRequirement());
//                detail.put("fileUrl",filePaths);
                map.put("detail",detail);
                map.put("teaError",1);
                mapList.add(map);
                continue;
            }
            List<String> offerList = CommonUtil.toStrList(teacher.getTea_tag());
            List<String> filePaths = fileDao.selectFilePath(preOrder.getPreorder_id());
            Detail detail = new Detail(teacher.getTea_nickname(),teacher.getTea_img_url(),teacher.getTea_gender(),
                    teacher.getPosition(),teacher.getTea_company(),teacher.getIsonline(),offerList,teacher.getTea_description(),
                    preOrder.getRealname(),preOrder.getTele(),preOrder.getExperience(),preOrder.getRequirement(),filePaths);
//            detail.put("teaName",teacher.getTea_nickname());
//            detail.put("teaHeadImg",teacher.getTea_img_url());
//            detail.put("teaGender",teacher.getTea_gender());
//            detail.put("teaPosition",teacher.getPosition());
//            detail.put("teaCompany",teacher.getTea_company());
//            detail.put("teaIsOnline",teacher.getIsonline());
//            List<String> offerList = CommonUtil.toStrList(teacher.getTea_tag());
//            detail.put("teaOffer",offerList);
//            detail.put("teaPreDes",teacher.getTea_description());
//            //学生信息
//            detail.put("stuRealname",preOrder.getRealname());
//            detail.put("stuTele",preOrder.getTele());
//            detail.put("stuExperience",preOrder.getExperience());
//            detail.put("stuGuidance",preOrder.getRequirement());
//            List<String> filePaths = fileDao.selectFilePath(preOrder.getPreorder_id());
//            detail.put("fileUrl",filePaths);
            map.put("detail",detail);
            mapList.add(map);
        }

        System.out.println(mapList);
        return mapList;
    }
}
