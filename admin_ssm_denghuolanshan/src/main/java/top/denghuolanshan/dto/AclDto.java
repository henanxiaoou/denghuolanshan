package top.denghuolanshan.dto;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import top.denghuolanshan.model.SysAcl;

/**
 * @ClassName AclDto
 * @Description 角色点DTO
 * @Author 小欧
 * @Date 2019/6/15 14:56
 * @Version 1.0
 **/
@Data
public class AclDto extends SysAcl {
    /**
     * 是否要默认选择
     */
    private boolean checked = false;
    /**
     * 是否有权限操作
     */
    private boolean hasAcl = false;
    public static AclDto adapt(SysAcl acl){
        AclDto dto = new AclDto();
        BeanUtils.copyProperties(acl,dto);
        return dto;
    }
}
