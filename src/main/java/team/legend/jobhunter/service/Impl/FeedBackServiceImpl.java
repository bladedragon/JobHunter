package team.legend.jobhunter.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.legend.jobhunter.dao.EvaluateDao;
import team.legend.jobhunter.service.FeedBackService;
import team.legend.jobhunter.utils.CommonUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class FeedBackServiceImpl implements FeedBackService {

    @Autowired
    EvaluateDao feedBackDao;
    @Override
    public int feedBack(String stuId,String feedBack,String tele) {

        int num = feedBackDao.insertFeedBack(stuId,feedBack, tele,CommonUtil.getNowDate("yyyy-MM-dd HH:mm:ss"));

        if(num ==1){
            return 0;
        }else{
            log.error(">>log: feedback is error ,num:[{}]",num);
            return 1;
        }
    }
}
