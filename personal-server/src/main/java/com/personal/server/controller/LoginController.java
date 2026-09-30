package com.personal.server.controller;

import com.personal.common.Result;
import com.personal.server.dto.LoginRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {

    // 暂为写死的账号校验，后续接入用户表后替换
    private static final String MOCK_USERNAME = "admin";
    private static final String MOCK_PASSWORD = "123456";

    @PostMapping("/login")
    public Result<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
        if (MOCK_USERNAME.equals(request.getUsername()) && MOCK_PASSWORD.equals(request.getPassword())) {
            return Result.success(Map.of("username", request.getUsername()));
        }
        return Result.fail("用户名或密码错误");
    }
}
