package com.zyplayer.doc.api.framework.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.*;

/**
 * 开启api接口文档模块注解
 *
 * @author 离狐千慕
 * @since 2023-11-04
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
@ComponentScan(basePackages = {
		"com.zyplayer.doc.api",
})
public @interface EnableDocApi {

}
