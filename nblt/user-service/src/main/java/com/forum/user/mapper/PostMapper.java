package com.forum.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.forum.user.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 帖子 Mapper（临时放在 user-service 中）
 *
 * @author Forum Team
 */
@Mapper
public interface PostMapper extends BaseMapper<Post> {

    /**
     * 根据用户ID查询帖子列表
     *
     * @param userId 用户ID
     * @return 帖子列表
     */
    @Select("SELECT * FROM posts WHERE user_id = #{userId} AND status = 1 ORDER BY created_at DESC")
    List<Post> selectByUserId(@Param("userId") Long userId);

}
