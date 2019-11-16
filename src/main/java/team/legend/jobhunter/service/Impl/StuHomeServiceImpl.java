package team.legend.jobhunter.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.legend.jobhunter.service.StuHomeService;
import team.legend.jobhunter.dao.OrderDao;
import team.legend.jobhunter.dao.PreOrderDao;
import team.legend.jobhunter.dao.WXDao;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class StuHomeServiceImpl implements StuHomeService {

    @Autowired
    WXDao wxDao;
    @Autowired
    PreOrderDao preOrderDao;
    @Autowired
    OrderDao orderDao;

    @Override
    public Map<String, Object> getStuHome(String stuId) {
        Map<String,Object> map = new LinkedHashMap<>();
        int savedNum = preOrderDao.getCountByStuId(stuId);
        int accomplishedNum = orderDao.getCountBySStatus(stuId,1);
        int orderNum = orderDao.getCountByStuId(stuId);
        int servingNum = orderNum-accomplishedNum;
        map.put("stuId",stuId);
        map.put("saved",savedNum);
        map.put("serving",servingNum);
        map.put("accomplished",accomplishedNum);

        return map;
    }


}
