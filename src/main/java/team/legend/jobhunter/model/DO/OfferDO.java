package team.legend.jobhunter.model.DO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OfferDO {

    private String offerId;
    private String company;
    private int type;
    private String location;
    private String updatre_date;
    private String description;
    private String requirement;
    private String post_way;


}
