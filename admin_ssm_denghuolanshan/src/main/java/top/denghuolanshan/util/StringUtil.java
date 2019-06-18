package top.denghuolanshan.util;

import com.google.common.base.Splitter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName StringUtil
 * @Description 解析SysRoleController中的aclIds
 * @Author 小欧
 * @Date 2019/6/15 20:20
 * @Version 1.0
 **/
public class StringUtil {
    /**
     * 该方法的作用是将传入的多个id转换成一个集合，比如 1,2,3,, 4,,,,5并且会去除空格和符号，只保留1 2 3 4 5
     * @param str
     * @return
     */
    public static List<Integer> splitToListInt(String str) {
        List<String> strList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(str);
        return strList.stream().map(strItem -> Integer.parseInt(strItem)).collect(Collectors.toList());
    }
}
