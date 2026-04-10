package com.forum.comment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.forum.comment.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 评论 Mapper
 *
 * @author Forum Team
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 根据帖子ID查询评论列表（按时间正序）
     *
     * @param postId 帖子ID
     * @return 评论列表
     */
    @Select("SELECT id, post_id, user_id as authorId, content, status, created_at, updated_at " +
            "FROM comments WHERE post_id = #{postId} AND status = 1 ORDER BY created_at ASC")
    List<Comment> selectByPostId(@Param("postId") Long postId);

}
