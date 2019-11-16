package team.legend.jobhunter.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StuDetail {

    private String stuRealname;
    private String stuTele;
    private String stuExperience;
    private String stuGuidance;
    private List<String> fileUrl;
}
