package top.denghuolanshan.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName LevelUtil
 * @Description
 * @Author 小欧
 * @Date 2019/6/1 22:20
 * @Version 1.0
 **/
public class LevelUtil {
    public final static String SEPARATOR = ".";

    public final static String ROOT = "0";

    public static String calculateLevel(String parentLevel,int parentId){
        if (StringUtils.isBlank(parentLevel)){
            return ROOT;
        }else {
            return StringUtils.join(parentLevel,SEPARATOR,parentId);
        }
    }
}
