package com.forum.comment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.forum.comment.dto.CommentCreateDTO;
import com.forum.comment.entity.Comment;
import com.forum.comment.vo.CommentVO;

import java.util.List;

/**
 * 评论服务接口
 *
 * @author Forum Team
 */
public interface CommentService extends IService<Comment> {

    /**
     * 创建评论
     *
     * @param authorId 作者ID
     * @param dto      创建请求
     * @return 创建的评论ID
     */
    Long createComment(Long authorId, CommentCreateDTO dto);

    /**
     * 删除评论
     *
     * @param id       评论ID
     * @param authorId 当前用户ID
     * @return 是否成功
     */
    boolean deleteComment(Long id, Long authorId);

    /**
     * 获取帖子的评论列表
     *
     * @param postId 帖子ID
     * @return 评论列表
     */
    List<CommentVO> getCommentsByPostId(Long postId);

}
