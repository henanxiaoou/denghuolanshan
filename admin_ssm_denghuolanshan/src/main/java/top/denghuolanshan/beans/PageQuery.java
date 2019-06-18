package top.denghuolanshan.beans;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

/**
 * @ClassName PageQuery
 * @Description 分页查询
 * @Author 小欧
 * @Date 2019/6/4 18:49
 * @Version 1.0
 **/
public class PageQuery {
    @Getter
    @Setter
    @Min(value = 1,message = "当前页码不合法")
    private int pageNo = 1;
    @Setter
    @Getter
    @Min(value = 1,message = "每页展示的数量不合法")
    private int pageSize = 10;
    @Setter
    private int offset;

    public int getOffset() {
        return (pageNo - 1)*pageSize;
    }
}
