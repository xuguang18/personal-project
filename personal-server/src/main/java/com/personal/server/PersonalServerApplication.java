package com.personal.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * HTTP 服务入口。
 * scanBasePackages 指向 com.personal，以加载 personal-common 中的全局异常处理等组件。
 */
@SpringBootApplication(scanBasePackages = "com.personal")
public class PersonalServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonalServerApplication.class, args);
    }
}
