package com.forum.comment.controller;

import com.forum.comment.dto.CommentCreateDTO;
import com.forum.comment.service.CommentService;
import com.forum.comment.vo.CommentVO;
import com.forum.common.result.Result;
import com.forum.common.result.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评论控制器
 *
 * @author Forum Team
 */
@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 获取帖子的评论列表
     *
     * @param postId 帖子ID
     * @return 评论列表
     */
    @GetMapping("/list")
    public Result<List<CommentVO>> list(@RequestParam Long postId) {
        List<CommentVO> list = commentService.getCommentsByPostId(postId);
        return Result.success(list);
    }

    /**
     * 创建评论
     *
     * @param dto     创建请求
     * @param request HTTP请求
     * @return 创建的评论ID
     */
    @PostMapping("/create")
    public Result<Map<String, Long>> create(@Valid @RequestBody CommentCreateDTO dto,
                                               HttpServletRequest request) {
        Long authorId = getCurrentUserId(request);
        Long commentId = commentService.createComment(authorId, dto);

        Map<String, Long> result = new HashMap<>();
        result.put("id", commentId);
        return Result.success(result);
    }

    /**
     * 删除评论
     *
     * @param id      评论ID
     * @param request HTTP请求
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        Long authorId = getCurrentUserId(request);
        boolean success = commentService.deleteComment(id, authorId);
        if (success) {
            return Result.success();
        }
        return Result.error(ResultCode.ERROR);
    }

    /**
     * 从请求中获取当前用户ID
     * 通过JWT过滤器设置的属性获取
     *
     * @param request HTTP请求
     * @return 用户ID
     */
    private Long getCurrentUserId(HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }
        return Long.valueOf(userId.toString());
    }

}
