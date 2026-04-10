package com.forum.post.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 帖子实体类
 *
 * @author Forum Team
 */
@Data
@TableName("posts")
public class Post {

    /**
     * 帖子ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 作者ID
     */
    @TableField("user_id")
    private Long authorId;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 浏览次数
     * Note: Not stored in database, computed field
     */
    @TableField(exist = false)
    private Integer viewCount;

    /**
     * 点赞次数
     * Note: Not stored in database, computed field
     */
    @TableField(exist = false)
    private Integer likeCount;

    /**
     * 评论次数
     * Note: Not stored in database, computed field
     */
    @TableField(exist = false)
    private Integer commentCount;

    /**
     * 状态：0-草稿，1-已发布，2-已删除
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 逻辑删除：0-未删除，1-已删除
     * Note: Not stored in database
     */
    @TableField(exist = false)
    private Integer deleted;

}
