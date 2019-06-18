package top.denghuolanshan.beans;

import com.google.common.collect.Lists;
import lombok.*;

import java.util.List;
/**
 * @ClassName PageQuery
 * @Description 分页返回
 * @Author 小欧
 * @Date 2019/6/4 18:49
 * @Version 1.0
 **/
@Getter
@Setter
@ToString
@Builder
public class PageResult<T> {

    private List<T> data = Lists.newArrayList();

    private int total = 0;
}
