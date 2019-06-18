package top.denghuolanshan.param;

import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;

/**
 * @ClassName Testvo
 * @Description 封装对象
 * @Author 小欧
 * @Date 2019/6/1 18:47
 * @Version 1.0
 **/
@Data
public class TestVo {
    @NotBlank
    private String msg;

    @NotNull(message = "id不能为空")
    @Max(value = 10,message = "id不能大于10")
    @Min(value = 0,message = "id不能小于0")
    private Integer id;

    @NotEmpty
    private List<String> str;
}
