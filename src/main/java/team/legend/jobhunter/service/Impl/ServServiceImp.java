package team.legend.jobhunter.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.legend.jobhunter.dao.Resume_serviceDao;
import team.legend.jobhunter.dao.Tutor_serviceDao;
import team.legend.jobhunter.service.ServService;

import java.util.Map;

@Service
public class ServServiceImp implements ServService {

    @Autowired
    Resume_serviceDao resumeServiceDao;
    @Autowired
    Tutor_serviceDao tutorServiceDao;


    @Override
    public Map<String, Object> showInfo(int pagesize, int page)
    {
        return null;
    }
}
