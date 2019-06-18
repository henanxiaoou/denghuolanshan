package top.denghuolanshan.dao;

import org.apache.ibatis.annotations.Param;
import top.denghuolanshan.beans.PageQuery;
import top.denghuolanshan.dto.SearchLogDto;
import top.denghuolanshan.model.SysLog;
import top.denghuolanshan.model.SysLogWithBLOBs;

import java.util.List;

public interface SysLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysLogWithBLOBs record);

    int insertSelective(SysLogWithBLOBs record);

    SysLogWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysLogWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(SysLogWithBLOBs record);

    int updateByPrimaryKey(SysLog record);

    /**
     *
     * @param dto
     * @return
     */
    int countBySearchDto(@Param("dto") SearchLogDto dto);

    /**
     *
     * @param dto
     * @param page
     * @return
     */
    List<SysLogWithBLOBs> getPageListBySearchDto(@Param("dto") SearchLogDto dto, @Param("page") PageQuery page);
}