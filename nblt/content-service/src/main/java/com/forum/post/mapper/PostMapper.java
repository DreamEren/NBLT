package com.forum.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forum.post.entity.Post;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 帖子 Mapper
 *
 * @author Forum Team
 */
@Mapper
public interface PostMapper extends BaseMapper<Post> {

    /**
     * 查询已发布的帖子列表（按时间倒序）
     *
     * @param limit  限制数量
     * @param offset 偏移量
     * @return 帖子列表
     */
    @Select("SELECT id, user_id, title, content, status, created_at, updated_at FROM posts WHERE status = 1 ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}")
    @Results({
            @Result(column = "user_id", property = "authorId"),
            @Result(column = "created_at", property = "createdAt"),
            @Result(column = "updated_at", property = "updatedAt")
    })
    List<Post> selectPublishedPosts(@Param("limit") int limit, @Param("offset") int offset);

    /**
     * 统计已发布的帖子数量
     *
     * @return 帖子数量
     */
    @Select("SELECT COUNT(*) FROM posts WHERE status = 1")
    Long countPublishedPosts();

    /**
     * 根据ID查询已发布的帖子（跳过逻辑删除拦截器）
     *
     * @param id 帖子ID
     * @return 帖子
     */
    @Select("SELECT id, user_id, title, content, status, created_at, updated_at FROM posts WHERE id = #{id} AND status = 1")
    @Results({
            @Result(column = "user_id", property = "authorId"),
            @Result(column = "created_at", property = "createdAt"),
            @Result(column = "updated_at", property = "updatedAt")
    })
    Post selectPublishedById(@Param("id") Long id);

    /**
     * 增加浏览次数
     *
     * @param id 帖子ID
     * @return 影响行数
     */
    // view_count column does not exist in posts table, skipping increment
    default int incrementViewCount(@Param("id") Long id) {
        return 0;
    }

    /**
     * 根据关键词搜索已发布的帖子（按时间倒序）
     *
     * @param keyword 搜索关键词（匹配标题）
     * @param limit   限制数量
     * @param offset  偏移量
     * @return 帖子列表
     */
    @Select("SELECT id, user_id, title, content, status, created_at, updated_at FROM posts WHERE status = 1 AND title LIKE CONCAT('%', #{keyword}, '%') ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}")
    @Results({
            @Result(column = "user_id", property = "authorId"),
            @Result(column = "created_at", property = "createdAt"),
            @Result(column = "updated_at", property = "updatedAt")
    })
    List<Post> searchPublishedPosts(@Param("keyword") String keyword, @Param("limit") int limit, @Param("offset") int offset);

    /**
     * 统计关键词搜索的已发布帖子数量
     *
     * @param keyword 搜索关键词
     * @return 帖子数量
     */
    @Select("SELECT COUNT(*) FROM posts WHERE status = 1 AND title LIKE CONCAT('%', #{keyword}, '%')")
    Long countSearchPublishedPosts(@Param("keyword") String keyword);

}
