package com.forum.auth.service.impl;

import com.forum.auth.service.AuthService;
import com.forum.auth.vo.LoginVO;
import com.forum.common.exception.BusinessException;
import com.forum.common.result.ResultCode;
import com.forum.common.util.JwtUtil;
import com.forum.user.dto.UserLoginDTO;
import com.forum.user.entity.User;
import com.forum.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 认证服务实现
 *
 * @author Forum Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public LoginVO login(UserLoginDTO loginDTO) {
        // 1. 查找用户
        User user = userService.getUserByUsername(loginDTO.getUsername());
        if (user == null) {
            log.warn("登录失败，用户不存在: {}", loginDTO.getUsername());
            throw new BusinessException(ResultCode.LOGIN_ERROR);
        }

        // 2. 验证密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            log.warn("登录失败，密码错误: {}", loginDTO.getUsername());
            throw new BusinessException(ResultCode.LOGIN_ERROR);
        }

        // 3. 检查用户状态
        if (user.getStatus() != 1) {
            log.warn("登录失败，用户已禁用: {}", loginDTO.getUsername());
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        // 4. 生成 JWT Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        // 5. 构建返回结果
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setUserId(user.getId());
        loginVO.setUsername(user.getUsername());
        loginVO.setNickname(user.getNickname());
        loginVO.setAvatar(user.getAvatar());

        log.info("用户登录成功: {}", user.getUsername());
        return loginVO;
    }

}
