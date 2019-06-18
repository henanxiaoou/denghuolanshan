package top.denghuolanshan.util;


import com.sun.xml.internal.fastinfoset.sax.Properties;
import top.denghuolanshan.beans.Mail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName Test
 * @Description TODO
 * @Author 小欧
 * @Date 2019/6/5 14:49
 * @Version 1.0
 **/
public class Test {
    public static void main(String[] args) {
        Set<String> set = new HashSet<>();
        set.add("2262057819@qq.com");
        Mail mail = Mail.builder().subject("获取密码").message("您的密码为:000").receivers(set).build();
        MailUtil.send(mail);
    }

}
