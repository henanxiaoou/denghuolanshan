package top.denghuolanshan.util;

import java.util.Date;
import java.util.Random;

/**
 * @ClassName PassWordUtil
 * @Description 密码加密工具类
 * @Author 小欧
 * @Date 2019/6/3 20:36
 * @Version 1.0
 **/
public class PassWordUtil {

    public final static String[] word = {
            "a","b","c","d","e","f","g", "h",
            "j","k","m","n","p","q", "r","s",
            "t","u","v","w","x", "y","z",
            "A","B","C","D","E","F","G","H",
            "J","K","M","N","P","Q","R","S",
            "T","U","V","W","X","Y","Z"
    };
    public final static String[] num = {
           "2","3","4","5","6","7","8","9"
    };
    public static String randomPassword(){
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random(System.currentTimeMillis());
        boolean flag = false;
        int length = random.nextInt(3)+8;
        for (int i = 0; i < length; i++) {
            if (flag) {
                stringBuffer.append(num[random.nextInt(num.length)]);
            }else {
                stringBuffer.append(word[random.nextInt(word.length)]);
            }
            flag = !flag;
        }
        return stringBuffer.toString();
    }

    public static void main(String[] args) throws InterruptedException {
        System.err.println(randomPassword());
        Thread.sleep(10);
        System.err.println(randomPassword());
        Thread.sleep(10);
        System.err.println(randomPassword());
    }
}
