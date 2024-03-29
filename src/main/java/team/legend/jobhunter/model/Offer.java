package team.legend.jobhunter.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Offer {

    private String offerId;
    private String company;
    private int type;
    private String location;
    private String update_date;
    private String description;
    private String requirement;
    private String post_way;

}
