package com.forum.post.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 帖子列表项VO
 *
 * @author Forum Team
 */
@Data
public class PostListVO {

    /**
     * 帖子ID
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容摘要
     */
    private String summary;

    /**
     * 作者ID
     */
    private Long authorId;

    /**
     * 作者名称
     */
    private String authorName;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 点赞次数
     */
    private Integer likeCount;

    /**
     * 评论次数
     */
    private Integer commentCount;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

}
