package top.denghuolanshan.beans;

import lombok.*;

import java.util.Set;

/**
 * @ClassName Mail
 * @Description 封装邮件
 * @Author 小欧
 * @Date 2019/6/5 14:29
 * @Version 1.0
 **/
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mail {

    /**
     * 邮件主题
     */
    private String subject;
    /**
     * 邮件信息
     */
    private String message;
    /**
     * 收件人邮箱
     */
    private Set<String> receivers;

}
