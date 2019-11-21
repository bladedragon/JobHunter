package team.legend.jobhunter.service;

import team.legend.jobhunter.exception.SqlErrorException;

import java.util.Map;

public interface LoveService {

   int love(String stuId,String offerId) throws SqlErrorException;

   Map<String, Object> getLove(String stuId,int page,int pagesize);
}
