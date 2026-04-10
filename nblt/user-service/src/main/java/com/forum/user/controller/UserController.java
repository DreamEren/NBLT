package com.forum.user.controller;

import com.forum.common.result.Result;
import com.forum.user.dto.UserRegisterDTO;
import com.forum.user.entity.Post;
import com.forum.user.entity.User;
import com.forum.user.mapper.PostMapper;
import com.forum.user.service.UserService;
import com.forum.user.vo.PostVO;
import com.forum.user.vo.UserVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户控制器
 *
 * @author Forum Team
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PostMapper postMapper;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<UserVO> register(@Valid @RequestBody UserRegisterDTO registerDTO) {
        UserVO userVO = userService.register(registerDTO);
        return Result.success("注册成功", userVO);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/me")
    public Result<UserVO> getCurrentUser(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return Result.error(401, "未登录");
        }
        Long userId = (Long) authentication.getPrincipal();
        User user = userService.getUserById(userId);
        if (user == null) {
            return Result.error(404, "用户不存在");
        }
        return Result.success(userService.convertToVO(user));
    }

    /**
     * 获取当前用户的帖子列表
     */
    @GetMapping("/my-posts")
    public Result<List<PostVO>> getMyPosts(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return Result.error(401, "未登录");
        }
        Long userId = (Long) authentication.getPrincipal();
        List<Post> posts = postMapper.selectByUserId(userId);
        List<PostVO> postVOs = posts.stream()
                .map(this::convertToPostVO)
                .collect(Collectors.toList());
        return Result.success(postVOs);
    }

    private PostVO convertToPostVO(Post post) {
        PostVO vo = new PostVO();
        vo.setId(post.getId());
        vo.setUserId(post.getUserId());
        vo.setTitle(post.getTitle());
        // 生成内容摘要（前200字符）
        String content = post.getContent();
        if (content != null && content.length() > 200) {
            vo.setSummary(content.substring(0, 200) + "...");
        } else {
            vo.setSummary(content);
        }
        vo.setViewCount(post.getViewCount());
        vo.setLikeCount(post.getLikeCount());
        vo.setCommentCount(post.getCommentCount());
        vo.setCreatedAt(post.getCreatedAt());
        return vo;
    }

}
