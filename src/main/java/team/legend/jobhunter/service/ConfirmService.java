package team.legend.jobhunter.service;

import team.legend.jobhunter.exception.SqlErrorException;

import java.util.Map;

public interface ConfirmService {

    Map<String,String> teaFill(String teaId,String orderId,String location,Long appointTime) throws SqlErrorException;

    int confirm(String userId,String orderId);

    Map<String,String> confirmAccomplish(String userId,String orderId,int isTea) throws SqlErrorException;
}
