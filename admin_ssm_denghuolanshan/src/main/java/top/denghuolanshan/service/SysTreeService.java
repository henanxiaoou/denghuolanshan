package top.denghuolanshan.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.denghuolanshan.dao.SysAclMapper;
import top.denghuolanshan.dao.SysAclModuleMapper;
import top.denghuolanshan.dao.SysDeptMapper;
import top.denghuolanshan.dto.AclDto;
import top.denghuolanshan.dto.AclModuleLevelDto;
import top.denghuolanshan.dto.DeptLevelDto;
import top.denghuolanshan.model.SysAcl;
import top.denghuolanshan.model.SysAclModule;
import top.denghuolanshan.model.SysDept;
import top.denghuolanshan.param.AclModuleParam;
import top.denghuolanshan.util.LevelUtil;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName SysTreeService
 * @Description 部门树和权限树和角色树业务逻辑
 * @Author 小欧
 * @Date 2019/6/2 13:32
 * @Version 1.0
 **/
@Service
public class SysTreeService {
    @Resource
    private SysDeptMapper sysDeptMapper;
    @Resource
    private SysAclModuleMapper sysAclModuleMapper;
    @Autowired
    private SysCoreService sysCoreService;
    @Autowired
    private SysAclMapper sysAclMapper;

    /**
     * 以下为角色树
     * @param roleId
     * @return
     */
    public List<AclModuleLevelDto> roleTree(int roleId){
        // 当前用户已分配的权限点
        List<SysAcl> userAclList = sysCoreService.getCurrentUserAclList();
        // 当前角色分配的权限点
        List<SysAcl> roleAclList = sysCoreService.getRoleAclList(roleId);
        List<AclDto> aclDtoList = Lists.newArrayList();

        Set<Integer> userAclIdSet = userAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());
        Set<Integer> roleAclIdSet = roleAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());
        // 获取所有的权限点
        List<SysAcl> allAclList = sysAclMapper.getAll();
        for (SysAcl acl : allAclList) {
            AclDto dto = AclDto.adapt(acl);
            if (userAclIdSet.contains(acl.getId())){
                dto.setHasAcl(true);
            }
            if (roleAclIdSet.contains(acl.getId())){
                dto.setChecked(true);
            }
            aclDtoList.add(dto);
        }
        return aclListToTree(aclDtoList);
    }

    /**
     *将aclDtoList转换为权限树
     * @param aclDtoList
     * @return
     */
    public List<AclModuleLevelDto> aclListToTree(List<AclDto> aclDtoList){
        if (CollectionUtils.isEmpty(aclDtoList)){
            return Lists.newArrayList();
        }
        List<AclModuleLevelDto> aclModuleLevelList = aclModuleTree();
        Multimap<Integer,AclDto> moduleIdAclMap = ArrayListMultimap.create();
        for (AclDto dto : aclDtoList) {
            // 状态值为1时可用，0时不可用
            if (dto.getStatus() == 1){
                moduleIdAclMap.put(dto.getAclModuleId(),dto);
            }
        }
        bindAclsWithOrder(aclModuleLevelList,moduleIdAclMap);
        return aclModuleLevelList;
    }

    /**
     * 将权限点绑定到权限树上
     * @param aclModuleLevelList
     * @param moduleIdAclMap
     */
    public void bindAclsWithOrder(List<AclModuleLevelDto> aclModuleLevelList, Multimap<Integer,AclDto> moduleIdAclMap){
        if (CollectionUtils.isEmpty(aclModuleLevelList)){
            return;
        }
        for (AclModuleLevelDto dto : aclModuleLevelList) {
            List<AclDto> aclDtoList = (List<AclDto>)moduleIdAclMap.get(dto.getId());
            if (CollectionUtils.isNotEmpty(aclDtoList)) {
                Collections.sort(aclDtoList,aclSeqComparator);
                dto.setAclList(aclDtoList);
            }
            bindAclsWithOrder(dto.getAclModuleList(),moduleIdAclMap);
        }
    }
    /**
     * 以下为权限树
     * 该类的作用时准备好权限树的数据
     * @return
     */
    public List<AclModuleLevelDto> aclModuleTree(){
        // 获取所有权限模块
        List<SysAclModule> aclModuleList =   sysAclModuleMapper.getAllAclModule();
        // 定义权限模块树形结构
        List<AclModuleLevelDto> dtoList = Lists.newArrayList();
        for (SysAclModule aclModule : aclModuleList) {
            dtoList.add(AclModuleLevelDto.adapt(aclModule));
        }
        return aclModuleListToTree(dtoList);
    }

    /**
     * 根据当前方法适配权限树
     * 该类的作用时将数据转换为权限树
     * @return
     */
    public List<AclModuleLevelDto> aclModuleListToTree(List<AclModuleLevelDto> dtoList){
        if (CollectionUtils.isEmpty(dtoList)) {
            return Lists.newArrayList();
        }
        // 一个String类型的key值对应一个列表，然后操作列表即可得到一个树
        Multimap<String,AclModuleLevelDto> levelAclModuleMap = ArrayListMultimap.create();
        List<AclModuleLevelDto> rootList = Lists.newArrayList();
        for (AclModuleLevelDto dto : dtoList) {
            levelAclModuleMap.put(dto.getLevel(),dto);
            // 当 当前权限没有父权限时
            if (LevelUtil.ROOT.equals(dto.getLevel())){
                rootList.add(dto);
            }
        }
        // 排序
        Collections.sort(rootList,aclModuleSeqComparator);
        transformAclModuleTree(rootList,LevelUtil.ROOT,levelAclModuleMap);
        return rootList;
    }

    /**
     * 转换成树形结构
     */
    public void transformAclModuleTree(List<AclModuleLevelDto> dtoList,String level, Multimap<String,AclModuleLevelDto> levelAclModuleMap){
        for (int i = 0; i < dtoList.size(); i++) {
            AclModuleLevelDto dto = dtoList.get(i);
            String nextLevel = LevelUtil.calculateLevel(level,dto.getId());
            // 取出下一个层级的列表
            List<AclModuleLevelDto> tempList = (List<AclModuleLevelDto>)levelAclModuleMap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempList)){
                // 排序
                Collections.sort(tempList,aclModuleSeqComparator);
                dto.setAclModuleList(tempList);
                transformAclModuleTree(tempList,nextLevel,levelAclModuleMap);
            }
        }
    }
    public Comparator<AclModuleLevelDto> aclModuleSeqComparator = new Comparator<AclModuleLevelDto>() {
        @Override
        public int compare(AclModuleLevelDto o1, AclModuleLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };

    /**
     * 以下为部门树业务逻辑
     * 该类的作用时准备好权限树的数据
     * @return
     */
    public List<DeptLevelDto> deptTree(){
        // 获取所有部门模块
        List<SysDept> deptList = sysDeptMapper.getAllDept();
        // 定义部门模块树形结构
        List<DeptLevelDto> dtoList = Lists.newArrayList();
        // 遍历部门模块
        for (SysDept dept : deptList) {
            DeptLevelDto dto = DeptLevelDto.adapt(dept);
            dtoList.add(dto);
        }
        return deptListToTree(dtoList);
    }

    /**
     * 该类的作用时将数据转换为权限树
     * @param deptLevelDtoList
     * @return
     */
    public List<DeptLevelDto> deptListToTree(List<DeptLevelDto> deptLevelDtoList){
        if (CollectionUtils.isEmpty(deptLevelDtoList)) {
            return Lists.newArrayList();
        }
        Multimap<String,DeptLevelDto> levelDtoMap = ArrayListMultimap.create();
        List<DeptLevelDto> rootList = Lists.newArrayList();
        for (DeptLevelDto dto : deptLevelDtoList) {
            levelDtoMap.put(dto.getLevel(),dto);
            if (LevelUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }
        //按照seq从小到大排序
        Collections.sort(rootList, new Comparator<DeptLevelDto>() {
            @Override
            public int compare(DeptLevelDto o1, DeptLevelDto o2) {
                return o1.getSeq() - o2.getSeq();
            }
        });
        // 递归生成树
        transformDeptTree(deptLevelDtoList,LevelUtil.ROOT,levelDtoMap);
        return rootList;
    }
    public void transformDeptTree(List<DeptLevelDto> deptLevelList,String level,Multimap<String,DeptLevelDto> levelDtoMultimap){
        for (int i = 0; i < deptLevelList.size(); i++) {
            // 遍历该层的每个元素
            DeptLevelDto deptLevelDto = deptLevelList.get(i);
            // 处理当层的数据
            String nextLevel = LevelUtil.calculateLevel(level,deptLevelDto.getId());
            // 处理下一层
            List<DeptLevelDto> tempDeptList = (List<DeptLevelDto>) levelDtoMultimap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempDeptList)) {
                // 排序
                Collections.sort(tempDeptList,deptLSeqComparator);
                // 设置下一层部门
                deptLevelDto.setDeptList(tempDeptList);
                // 进入到下一层处理
                transformDeptTree(tempDeptList,nextLevel,levelDtoMultimap);
            }
        }
    }
    public Comparator<DeptLevelDto> deptLSeqComparator = new Comparator<DeptLevelDto>() {
        @Override
        public int compare(DeptLevelDto o1, DeptLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };
    public Comparator<AclDto> aclSeqComparator = new Comparator<AclDto>() {
        @Override
        public int compare(AclDto o1, AclDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };

    public List<AclModuleLevelDto> userAclTree(int userId) {
        List<SysAcl> userAclList = sysCoreService.getUserAclList(userId);
        List<AclDto> aclDtoList = Lists.newArrayList();
        for (SysAcl acl : userAclList) {
            AclDto dto = AclDto.adapt(acl);
            dto.setHasAcl(true);
            dto.setChecked(true);
            aclDtoList.add(dto);
        }
        return aclListToTree(aclDtoList);
    }
}
