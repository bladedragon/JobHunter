package team.legend.jobhunter.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.legend.jobhunter.dao.EvaluateDao;
import team.legend.jobhunter.exception.UploadException;
import team.legend.jobhunter.service.FeedBackService;
import team.legend.jobhunter.utils.CommonUtil;
import team.legend.jobhunter.utils.Constant;
import team.legend.jobhunter.utils.SecretUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FeedBackServiceImpl implements FeedBackService {

    @Value("${file.feedUrl}")
    String ffileUrl;
    @Value("${file.webFeedUrl}")
    String webFFileUrl;

    @Autowired
    EvaluateDao feedBackDao;
    @Override
    public int feedBack(String stuId,String feedBack,String tele) {

        if(tele ==null || tele.equals("")){
         tele = "";
        }
        int num = feedBackDao.insertFeedBack(stuId,feedBack, tele,CommonUtil.getNowDate("yyyy-MM-dd HH:mm:ss"));
        log.info("insert num = [{}]",num);
        if(num == 1){
            return 0;
        }else{
            log.error(">>log: feedback is error ,num:[{}]",num);
            return 1;
        }
    }

    @Override
    @Transactional(rollbackFor = IOException.class)
    public int uploadFile(List<MultipartFile> files, String userId) throws UploadException {

        int failNum = 0;

        for (MultipartFile file : files) {
            if(file.getSize()> Constant.MAX_FILE_SIZE){
                throw new UploadException("exceed max size");
            }
            String originFileName = file.getOriginalFilename();
            String[] strs = originFileName.split("\\.");
            String fileSuffix = strs[strs.length-1];
            String preName = SecretUtil.MD5Encode(userId).substring(0, 10);
            String fileName = preName +"." + fileSuffix ;
            String fileFullName = userId.substring(0,5)+"/"+fileName;
            String fileFullUrl = ffileUrl +fileFullName;
            File dir = new File(ffileUrl+userId.substring(0,5));
            dir.mkdirs();
            File savedfile = new File(fileFullUrl);
            try {
                file.transferTo(savedfile);
            } catch (IOException e) {
                e.printStackTrace();
                log.error(">>log filename:{} upload fail",fileFullName);
                failNum++;
            }
        }
        return failNum;
    }
}
