package com.forum.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.forum.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * 用户 Mapper
 * 第一版：content-service 临时直接查询 users 表
 *
 * @author Forum Team
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据ID集合批量查询用户
     *
     * @param ids 用户ID集合
     * @return 用户列表
     */
    @Select("<script>" +
            "SELECT id, username, nickname FROM users WHERE id IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    List<User> selectBatchByIds(@Param("ids") Set<Long> ids);

    /**
     * 根据ID查询用户（只查需要的字段，避免@TableLogic问题）
     *
     * @param id 用户ID
     * @return 用户
     */
    @Select("SELECT id, username, nickname FROM users WHERE id = #{id}")
    User selectById(@Param("id") Long id);

}
