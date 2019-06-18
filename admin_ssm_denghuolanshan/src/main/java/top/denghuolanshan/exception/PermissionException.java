package top.denghuolanshan.exception;

/**
 * @ClassName PermissionException
 * @Description
 * @Author 小欧
 * @Date 2019/6/1 15:13
 * @Version 1.0
 **/
public class PermissionException extends RuntimeException {
    public PermissionException() {
    }

    public PermissionException(String message) {
        super(message);
    }

    public PermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public PermissionException(Throwable cause) {
        super(cause);
    }

    public PermissionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
