package top.denghuolanshan.dto;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import top.denghuolanshan.model.SysDept;

import java.util.List;

/**
 * @ClassName DeptLevelDto
 * @Description
 * @Author 小欧
 * @Date 2019/6/2 13:28
 * @Version 1.0
 **/
@Data
public class DeptLevelDto extends SysDept {
    private List<DeptLevelDto> deptList = Lists.newArrayList();

    public static DeptLevelDto adapt(SysDept dept){
        DeptLevelDto dto = new DeptLevelDto();
        BeanUtils.copyProperties(dept,dto);
        return dto;
    }
}
