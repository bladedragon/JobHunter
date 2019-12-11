package team.legend.jobhunter.model.DO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OfferDO {

    private String offerid;
    private String offer;
    private String location;
    private String company;
    private Integer type;
    private String timestamp;
    private String  logo;



}
