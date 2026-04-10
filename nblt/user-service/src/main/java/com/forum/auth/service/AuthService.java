package com.forum.auth.service;

import com.forum.auth.vo.LoginVO;
import com.forum.user.dto.UserLoginDTO;

/**
 * 认证服务接口
 *
 * @author Forum Team
 */
public interface AuthService {

    /**
     * 用户登录
     *
     * @param loginDTO 登录信息
     * @return 登录结果（包含 Token）
     */
    LoginVO login(UserLoginDTO loginDTO);

}
