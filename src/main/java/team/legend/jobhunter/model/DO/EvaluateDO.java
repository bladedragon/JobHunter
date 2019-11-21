package team.legend.jobhunter.model.DO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EvaluateDO {
    private String order_id;
    private String tea_id;
    private String stu_id;
    private String comment;
    private Integer degree;
}
