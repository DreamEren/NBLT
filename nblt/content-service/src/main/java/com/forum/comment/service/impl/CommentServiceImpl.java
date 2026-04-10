package com.forum.comment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.forum.comment.dto.CommentCreateDTO;
import com.forum.comment.entity.Comment;
import com.forum.comment.mapper.CommentMapper;
import com.forum.comment.service.CommentService;
import com.forum.comment.vo.CommentVO;
import com.forum.common.exception.BusinessException;
import com.forum.common.result.ResultCode;
import com.forum.post.mapper.PostMapper;
import com.forum.user.entity.User;
import com.forum.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 评论服务实现类
 *
 * @author Forum Team
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private final CommentMapper commentMapper;
    private final PostMapper postMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public Long createComment(Long authorId, CommentCreateDTO dto) {
        // 校验帖子是否存在
        if (postMapper.selectById(dto.getPostId()) == null) {
            throw new BusinessException(ResultCode.POST_NOT_FOUND);
        }

        Comment comment = new Comment();
        comment.setPostId(dto.getPostId());
        comment.setAuthorId(authorId);
        comment.setContent(dto.getContent());
        comment.setStatus(1); // 1-正常

        commentMapper.insert(comment);
        return comment.getId();
    }

    @Override
    @Transactional
    public boolean deleteComment(Long id, Long authorId) {
        Comment comment = commentMapper.selectById(id);
        if (comment == null || comment.getStatus() == 0) {
            throw new BusinessException(ResultCode.COMMENT_NOT_FOUND);
        }

        // 只能删除自己的评论
        if (!comment.getAuthorId().equals(authorId)) {
            throw new BusinessException(ResultCode.NO_PERMISSION);
        }

        return commentMapper.deleteById(id) > 0;
    }

    @Override
    public List<CommentVO> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentMapper.selectByPostId(postId);

        if (comments.isEmpty()) {
            return Collections.emptyList();
        }

        // 批量查询作者信息
        Set<Long> authorIds = comments.stream()
                .map(Comment::getAuthorId)
                .collect(Collectors.toSet());
        Map<Long, User> authorMap = userMapper.selectBatchByIds(authorIds)
                .stream()
                .collect(Collectors.toMap(User::getId, u -> u));

        return comments.stream().map(comment -> {
            CommentVO vo = new CommentVO();
            vo.setId(comment.getId());
            vo.setPostId(comment.getPostId());
            vo.setAuthorId(comment.getAuthorId());

            User author = authorMap.get(comment.getAuthorId());
            vo.setAuthorName(author != null ? author.getNickname() : "未知用户");

            vo.setContent(comment.getContent());
            vo.setCreatedAt(comment.getCreatedAt());
            return vo;
        }).collect(Collectors.toList());
    }

}
