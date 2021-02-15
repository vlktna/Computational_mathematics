package Exceptions;

public class MyException extends Exception{
    public MyException(String message, Exception e){
        super(message);
//        System.err.println(e.getMessage());
        System.err.println(message);
        System.exit(1);

    }
}