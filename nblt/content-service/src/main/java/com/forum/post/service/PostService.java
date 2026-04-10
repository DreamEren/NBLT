package com.forum.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.forum.post.dto.PostCreateDTO;
import com.forum.post.entity.Post;
import com.forum.post.vo.PostDetailVO;
import com.forum.post.vo.PostListVO;

import java.util.List;

/**
 * 帖子服务接口
 *
 * @author Forum Team
 */
public interface PostService extends IService<Post> {

    /**
     * 获取帖子列表
     *
     * @param page 页码（从1开始）
     * @param size 每页大小
     * @return 帖子列表
     */
    List<PostListVO> getPostList(int page, int size);

    /**
     * 搜索帖子列表
     *
     * @param page    页码（从1开始）
     * @param size    每页大小
     * @param keyword 搜索关键词
     * @return 帖子列表
     */
    List<PostListVO> searchPostList(int page, int size, String keyword);

    /**
     * 获取帖子总数
     *
     * @return 帖子数量
     */
    Long getPostCount();

    /**
     * 获取搜索结果的帖子总数
     *
     * @param keyword 搜索关键词
     * @return 帖子数量
     */
    Long getSearchPostCount(String keyword);

    /**
     * 获取帖子详情
     *
     * @param id 帖子ID
     * @return 帖子详情
     */
    PostDetailVO getPostDetail(Long id);

    /**
     * 创建帖子
     *
     * @param authorId 作者ID
     * @param dto      创建请求
     * @return 创建的帖子ID
     */
    Long createPost(Long authorId, PostCreateDTO dto);

    /**
     * 删除帖子
     *
     * @param id       帖子ID
     * @param authorId 当前用户ID
     * @return 是否成功
     */
    boolean deletePost(Long id, Long authorId);

}
