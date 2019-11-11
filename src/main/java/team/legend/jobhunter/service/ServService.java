package team.legend.jobhunter.service;

import java.util.Map;

public interface ServService {

    Map<String,Object> showResumeInfo(int pagesize,int page);

    Map<String,Object> showTutorInfo(int pagesize,int page);
}
