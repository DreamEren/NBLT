package com.forum.user.service;

import com.forum.user.dto.UserRegisterDTO;
import com.forum.user.entity.User;
import com.forum.user.vo.UserVO;

/**
 * 用户服务接口
 *
 * @author Forum Team
 */
public interface UserService {

    /**
     * 用户注册
     *
     * @param registerDTO 注册信息
     * @return 用户信息
     */
    UserVO register(UserRegisterDTO registerDTO);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户
     */
    User getUserByUsername(String username);

    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户
     */
    User getUserById(Long id);

    /**
     * 转换为 VO
     *
     * @param user 用户实体
     * @return 用户 VO
     */
    UserVO convertToVO(User user);

}
