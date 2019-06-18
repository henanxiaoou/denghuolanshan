package top.denghuolanshan.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @ClassName DeptParam
 * @Description 部门
 * @Author 小欧
 * @Date 2019/6/1 22:04
 * @Version 1.0
 **/
@Data
public class DeptParam {
    private Integer id;
    @NotNull(message = "部门名称不可以为空")
    @Length(max = 15,min = 2,message = "部门名称需要在2-15个字符之间")
    private String name;

    private Integer parentId = 0;
    @NotNull(message = "展示顺序不可以为空")
    private Integer seq;
    @Length(max = 150,message = "备注长度不能超过150个字符")
    private String remark;

}
