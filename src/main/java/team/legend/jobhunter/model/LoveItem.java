package team.legend.jobhunter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoveItem {

    private Integer id;
    private String stu_id;
    private String save_date;
    private String offer_id;
}
