package com.forum.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * User Service 启动类
 *
 * @author Forum Team
 */
@SpringBootApplication(scanBasePackages = {"com.forum.user", "com.forum.auth", "com.forum.common"})
@MapperScan({"com.forum.user.mapper"})
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
