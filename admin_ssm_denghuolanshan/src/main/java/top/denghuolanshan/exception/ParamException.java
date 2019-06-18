package top.denghuolanshan.exception;

/**
 * @ClassName ParamException
 * @Description
 * @Author 小欧
 * @Date 2019/6/1 19:33
 * @Version 1.0
 **/
public class ParamException extends RuntimeException {
    public ParamException() {
    }

    public ParamException(String message) {
        super(message);
    }

    public ParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamException(Throwable cause) {
        super(cause);
    }

    public ParamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
