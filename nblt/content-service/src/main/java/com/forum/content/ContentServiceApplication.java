package com.forum.content;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

/**
 * Content Service 启动类
 *
 * @author Forum Team
 */
@SpringBootApplication(
    scanBasePackages = {"com.forum.content", "com.forum.auth", "com.forum.common", "com.forum.config", "com.forum.post", "com.forum.comment", "com.forum.user"},
    exclude = {UserDetailsServiceAutoConfiguration.class}
)
public class ContentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentServiceApplication.class, args);
    }

}
