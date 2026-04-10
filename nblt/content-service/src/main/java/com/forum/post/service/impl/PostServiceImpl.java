package com.forum.post.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.forum.common.exception.BusinessException;
import com.forum.common.result.ResultCode;
import com.forum.post.dto.PostCreateDTO;
import com.forum.post.entity.Post;
import com.forum.post.mapper.PostMapper;
import com.forum.post.service.PostService;
import com.forum.post.vo.PostDetailVO;
import com.forum.post.vo.PostListVO;
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
 * 帖子服务实现类
 *
 * @author Forum Team
 */
@Service
@RequiredArgsConstructor
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    private final PostMapper postMapper;
    private final UserMapper userMapper;

    @Override
    public List<PostListVO> getPostList(int page, int size) {
        if (page < 1) page = 1;
        if (size < 1 || size > 100) size = 20;
        
        int offset = (page - 1) * size;
        List<Post> posts = postMapper.selectPublishedPosts(size, offset);
        
        return convertToPostListVO(posts);
    }

    @Override
    public List<PostListVO> searchPostList(int page, int size, String keyword) {
        if (page < 1) page = 1;
        if (size < 1 || size > 100) size = 20;
        if (keyword == null || keyword.trim().isEmpty()) {
            return getPostList(page, size);
        }
        
        int offset = (page - 1) * size;
        List<Post> posts = postMapper.searchPublishedPosts(keyword.trim(), size, offset);
        
        return convertToPostListVO(posts);
    }

    /**
     * 将 Post 列表转换为 PostListVO 列表
     */
    private List<PostListVO> convertToPostListVO(List<Post> posts) {
        if (posts.isEmpty()) {
            return Collections.emptyList();
        }

        // 批量查询作者信息
        Set<Long> authorIds = posts.stream()
                .map(Post::getAuthorId)
                .collect(Collectors.toSet());
        Map<Long, User> authorMap = userMapper.selectBatchByIds(authorIds)
                .stream()
                .collect(Collectors.toMap(User::getId, u -> u));

        return posts.stream().map(post -> {
            PostListVO vo = new PostListVO();
            vo.setId(post.getId());
            vo.setTitle(post.getTitle());
            vo.setSummary(generateSummary(post.getContent()));
            vo.setAuthorId(post.getAuthorId());
            
            User author = authorMap.get(post.getAuthorId());
            String authorName = "未知用户";
            if (author != null) {
                authorName = author.getNickname() != null && !author.getNickname().isEmpty()
                        ? author.getNickname()
                        : author.getUsername();
            }
            vo.setAuthorName(authorName);
            
            vo.setViewCount(0);
            vo.setLikeCount(0);
            vo.setCommentCount(0);
            vo.setCreatedAt(post.getCreatedAt());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public Long getPostCount() {
        return postMapper.countPublishedPosts();
    }

    @Override
    public Long getSearchPostCount(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getPostCount();
        }
        return postMapper.countSearchPublishedPosts(keyword.trim());
    }

    @Override
    public PostDetailVO getPostDetail(Long id) {
        Post post = postMapper.selectPublishedById(id);
        if (post == null) {
            throw new BusinessException(ResultCode.POST_NOT_FOUND);
        }

        // 增加浏览次数（异步更佳，但这里简单处理）
        postMapper.incrementViewCount(id);

        PostDetailVO vo = new PostDetailVO();
        vo.setId(post.getId());
        vo.setTitle(post.getTitle());
        vo.setContent(post.getContent());
        vo.setAuthorId(post.getAuthorId());
        
        // 查询作者信息
        User author = userMapper.selectById(post.getAuthorId());
        String authorName = "未知用户";
        if (author != null) {
            authorName = author.getNickname() != null && !author.getNickname().isEmpty() 
                    ? author.getNickname() 
                    : author.getUsername();
        }
        vo.setAuthorName(authorName);
        
        vo.setViewCount(0);
        vo.setLikeCount(0);
        vo.setCommentCount(0);
        vo.setCreatedAt(post.getCreatedAt());
        vo.setUpdatedAt(post.getUpdatedAt());
        
        return vo;
    }

    @Override
    @Transactional
    public Long createPost(Long authorId, PostCreateDTO dto) {
        Post post = new Post();
        post.setAuthorId(authorId);
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setViewCount(0);
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setStatus(1); // 已发布
        
        postMapper.insert(post);
        return post.getId();
    }

    @Override
    @Transactional
    public boolean deletePost(Long id, Long authorId) {
        Post post = postMapper.selectById(id);
        if (post == null) {
            throw new BusinessException(ResultCode.POST_NOT_FOUND);
        }
        
        // 只能删除自己的帖子
        if (!post.getAuthorId().equals(authorId)) {
            throw new BusinessException(ResultCode.NO_PERMISSION);
        }
        
        return postMapper.deleteById(id) > 0;
    }

    /**
     * 生成内容摘要
     *
     * @param content 内容
     * @return 摘要
     */
    private String generateSummary(String content) {
        if (content == null || content.isEmpty()) {
            return "";
        }
        // 去除HTML标签（如果有的话）
        String plainText = content.replaceAll("<[^\u003e]*>", "");
        if (plainText.length() <= 200) {
            return plainText;
        }
        return plainText.substring(0, 200) + "...";
    }

}
