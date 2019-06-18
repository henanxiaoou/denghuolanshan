package top.denghuolanshan.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @ClassName SearchLogDto
 * @Description TODO
 * @Author 小欧
 * @Date 2019/6/17 16:13
 * @Version 1.0
 **/
@Data
public class SearchLogDto {
    private Integer type; // LogType

    private String beforeSeg;

    private String afterSeg;

    private String operator;
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    private Date fromTime;
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    private Date toTime;
}
