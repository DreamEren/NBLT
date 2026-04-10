package com.forum.post.controller;

import com.forum.common.exception.BusinessException;
import com.forum.common.result.Result;
import com.forum.common.result.ResultCode;
import com.forum.post.dto.PostCreateDTO;
import com.forum.post.service.PostService;
import com.forum.post.vo.PostDetailVO;
import com.forum.post.vo.PostListVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 帖子控制器
 *
 * @author Forum Team
 */
@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * 获取帖子列表
     *
     * @param page 页码（默认1）
     * @param size 每页大小（默认20）
     * @param keyword 搜索关键词（可选）
     * @return 帖子列表
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword) {
        List<PostListVO> list;
        Long total;
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            list = postService.searchPostList(page, size, keyword);
            total = postService.getSearchPostCount(keyword);
        } else {
            list = postService.getPostList(page, size);
            total = postService.getPostCount();
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        
        return Result.success(result);
    }

    /**
     * 获取帖子详情
     *
     * @param id 帖子ID
     * @return 帖子详情
     */
    @GetMapping("/{id}")
    public Result<PostDetailVO> detail(@PathVariable Long id) {
        PostDetailVO vo = postService.getPostDetail(id);
        return Result.success(vo);
    }

    /**
     * 创建帖子
     *
     * @param dto     创建请求
     * @param request HTTP请求
     * @return 创建的帖子ID
     */
    @PostMapping("/create")
    public Result<Map<String, Long>> create(@Valid @RequestBody PostCreateDTO dto, 
                                              HttpServletRequest request) {
        Long authorId = getCurrentUserId(request);
        Long postId = postService.createPost(authorId, dto);
        
        Map<String, Long> result = new HashMap<>();
        result.put("id", postId);
        return Result.success(result);
    }

    /**
     * 删除帖子
     *
     * @param id      帖子ID
     * @param request HTTP请求
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        Long authorId = getCurrentUserId(request);
        boolean success = postService.deletePost(id, authorId);
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
            throw new BusinessException(ResultCode.UNAUTHORIZED, "用户未登录");
        }
        return Long.valueOf(userId.toString());
    }

}
