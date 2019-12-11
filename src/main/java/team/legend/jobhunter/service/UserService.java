package team.legend.jobhunter.service;

import team.legend.jobhunter.exception.AuthorizeErrorException;
import team.legend.jobhunter.exception.HttpReqException;
import team.legend.jobhunter.model.WXUser;

import java.util.List;
import java.util.Map;

public interface UserService {
    public Map<Integer,String> login(String code);

    public Map<String,Object> authorizeEncrypted(String user_id,String openid,String encryptedData, String iv) throws AuthorizeErrorException;

    public Map<String,Object> authorizeData(String rawData,String signature,String userId) throws AuthorizeErrorException;

    public Map<String,Object> getOldUserData(String user_id);
}
