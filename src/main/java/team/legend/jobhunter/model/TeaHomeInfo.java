package team.legend.jobhunter.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TeaHomeInfo {
    private String  teaId;
    private String teaName;
    private String company;
    private int  isOnline;
    private List<String> offer;
}
