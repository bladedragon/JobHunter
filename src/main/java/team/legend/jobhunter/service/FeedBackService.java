package team.legend.jobhunter.service;

import org.springframework.web.multipart.MultipartFile;
import team.legend.jobhunter.exception.UploadException;

import java.util.List;
import java.util.Map;

public interface FeedBackService {

     int feedBack(String stuId,String feedback,String tele);

     int uploadFile(List<MultipartFile> files, String userId) throws UploadException;
}
