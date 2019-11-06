package team.legend.jobhunter.exception;


public class SqlErrorException extends Exception {
    public SqlErrorException(String msg){
        super(msg);
    }
    public SqlErrorException(){
        super("SQL RollBack" );
    }
}
