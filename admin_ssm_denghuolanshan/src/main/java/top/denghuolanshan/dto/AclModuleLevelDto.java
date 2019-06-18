package top.denghuolanshan.dto;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import top.denghuolanshan.model.SysAclModule;
import top.denghuolanshan.model.SysDept;

import java.security.acl.Acl;
import java.util.List;

/**
 * @ClassName AclModuleLevelDto
 * @Description
 * @Author 小欧
 * @Date 2019/6/9 18:07
 * @Version 1.0
 **/
@Data
public class AclModuleLevelDto extends SysAclModule {
    private List<AclModuleLevelDto> aclModuleList = Lists.newArrayList();
    private List<AclDto> aclList = Lists.newArrayList();
    public static AclModuleLevelDto adapt(SysAclModule aclModule){
        AclModuleLevelDto dto = new AclModuleLevelDto();
        BeanUtils.copyProperties(aclModule,dto);
        return dto;
    }
}
