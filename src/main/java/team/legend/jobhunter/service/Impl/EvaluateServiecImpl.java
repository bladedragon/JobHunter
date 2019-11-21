package team.legend.jobhunter.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.legend.jobhunter.dao.EvaluateDao;
import team.legend.jobhunter.model.DO.EvaluateDO;
import team.legend.jobhunter.service.EvaluateServiec;
import team.legend.jobhunter.utils.CommonUtil;

import java.util.List;

@Service
@Slf4j
public class EvaluateServiecImpl implements EvaluateServiec {

    @Autowired
    EvaluateDao evaluateDao;
    @Override
    public int evaluate(String stu_id, String tea_id, String order_id, String comment, int degree) {

        int num = evaluateDao.insertEvalaute(stu_id,tea_id,order_id,comment,degree,CommonUtil.getNowDate("yyyy-MM-dd HH:mm:ss"));

        if(num ==1){
            return 0;
        }else{
            log.error(">>log: feedback is error ,num:[{}]",num);
            return 1;
        }
    }

    @Override
    public List<EvaluateDO> getDegree(String teaId) {
        return null;
    }
}
