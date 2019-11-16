package team.legend.jobhunter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import team.legend.jobhunter.utils.CommonUtil;

import java.util.List;

@Data
@AllArgsConstructor
public class Detail {
    private String teaNickname;
    private String teaHeadImg;
    private int teaGender;
    private String teaPosition;
    private String teaCompany;
    private int teaIsonline;
    private List<String> TeaOffer;
    private String teaPreDes;
    private String stuRealname;
    private String stuTele;
    private String stuExperience;
    private String stuGuidance;
    private List<String> fileUrl;




}
