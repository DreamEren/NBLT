package com.forum.common.exception;

import com.forum.common.result.Result;
import com.forum.common.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

/**
 * 全局异常处理器
 *
 * @author Forum Team
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<Void>> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        Result<Void> result = Result.error(e.getResultCode().getCode(), e.getMessage());
        // 未登录返回 401
        if (e.getResultCode() == ResultCode.UNAUTHORIZED) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 参数校验异常
     */
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e) {
        String message = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        log.warn("参数校验失败: {}", message);
        return Result.error(ResultCode.PARAM_ERROR.getCode(), message);
    }

    /**
     * 认证异常
     */
    @ExceptionHandler(BadCredentialsException.class)
    public Result<Void> handleBadCredentialsException(BadCredentialsException e) {
        log.warn("认证失败: {}", e.getMessage());
        return Result.error(ResultCode.LOGIN_ERROR);
    }

    /**
     * 权限异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result<Void> handleAccessDeniedException(AccessDeniedException e) {
        log.warn("权限不足: {}", e.getMessage());
        return Result.error(ResultCode.FORBIDDEN);
    }

    /**
     * 其他异常
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常: " + e.getMessage(), e);
        String msg = e.getMessage() != null ? e.getMessage() : "系统繁忙，请稍后重试";
        return Result.error("系统错误: " + msg);
    }

}
