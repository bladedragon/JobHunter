package team.legend.jobhunter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import team.legend.jobhunter.model.DO.FileDO;

import java.util.List;
import java.util.Map;

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
    private List<FileDO> fileUrl;
    private List<FileDO> teaFileUrl;



}
