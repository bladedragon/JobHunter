package team.legend.jobhunter.exception;

import lombok.Setter;


public class HttpReqException extends Exception{

    public  HttpReqException(String msg){
            super(msg);
        }

}
