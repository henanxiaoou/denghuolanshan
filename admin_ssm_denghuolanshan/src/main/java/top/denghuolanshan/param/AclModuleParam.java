package top.denghuolanshan.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName AclModuleParam
 * @Description TODO
 * @Author 小欧
 * @Date 2019/6/7 17:05
 * @Version 1.0
 **/
@Data
public class AclModuleParam {
    private Integer id;
    @NotBlank(message = "权限模块名称不能为空")
    @Length(min = 2,max = 20,message = "权限模块名称长度需要在2-20个字符之间")
    private String name;

    private Integer parentId = 0;
    @NotNull(message = "权限模块展示不能为空")
    private Integer seq;
    @NotNull(message = "权限模块状态不能为空")
    @Min(value = 0,message = "权限模块状态不合法")
    @Max(value = 1,message = "权限模块状态不合法")
    private Integer status;
    @Length(max = 200,message = "权限模块备注不能超过200个字符")
    private String remark;

}
