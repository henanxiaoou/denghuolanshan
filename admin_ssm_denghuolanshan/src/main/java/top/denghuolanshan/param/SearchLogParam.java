package top.denghuolanshan.param;

import lombok.Data;

/**
 * @ClassName SearchLogParam
 * @Description TODO
 * @Author 小欧
 * @Date 2019/6/17 11:32
 * @Version 1.0
 **/
@Data
public class SearchLogParam {

    private Integer type;

    private String beforeSeg;

    private String afterSeg;

    private String operator;
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    private String fromTime;

    private String toTime;
}
