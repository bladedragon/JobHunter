package team.legend.jobhunter.model.DO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TimeDo {
    private String service_id;
    private String item_id;
    private String item_time;
    private Integer item_status;
    private Integer isonline;
    private String item_timestamp;

}
