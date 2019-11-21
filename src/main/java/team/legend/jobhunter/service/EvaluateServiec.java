package team.legend.jobhunter.service;

import team.legend.jobhunter.model.DO.EvaluateDO;

import java.util.List;
import java.util.Map;

public interface EvaluateServiec {

    int evaluate(String stu_id,String tea_id,String order_id, String comment,int degree);

    List<EvaluateDO> getDegree(String teaId);
}
