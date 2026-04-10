package com.forum.common.result;

import lombok.Getter;

/**
 * 响应状态码枚举
 *
 * @author Forum Team
 */
@Getter
public enum ResultCode {

    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 失败
     */
    ERROR(500, "操作失败"),

    /**
     * 参数错误
     */
    PARAM_ERROR(400, "参数错误"),

    /**
     * 未授权
     */
    UNAUTHORIZED(401, "未授权"),

    /**
     * 禁止访问
     */
    FORBIDDEN(403, "禁止访问"),

    /**
     * 资源不存在
     */
    NOT_FOUND(404, "资源不存在"),

    /**
     * 资源已存在
     */
    ALREADY_EXISTS(409, "资源已存在"),

    /**
     * 用户名已存在
     */
    USERNAME_EXISTS(1001, "用户名已存在"),

    /**
     * 用户名或密码错误
     */
    LOGIN_ERROR(1002, "用户名或密码错误"),

    /**
     * 用户不存在
     */
    USER_NOT_FOUND(1003, "用户不存在"),

    /**
     * 旧密码错误
     */
    OLD_PASSWORD_ERROR(1004, "旧密码错误"),

    /**
     * 帖子不存在
     */
    POST_NOT_FOUND(2001, "帖子不存在"),

    /**
     * 评论不存在
     */
    COMMENT_NOT_FOUND(2002, "评论不存在"),

    /**
     * 无权限操作
     */
    NO_PERMISSION(2003, "无权限操作此资源");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
