package com.book;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.book.mapper")
public class BookApplication {
	/*
	--借阅记录查，删（还了的才能删），
	--入库记录查，删（需要同步修改图书的数量）
	 * 用户模块：图书管理员的增删改查，读者的的增删改查
	 * 系统模块：用户登陆、读者注册、管理员登陆、图书管理员登陆
	 * 注册、登陆要图形验证码
	 */
	public static void main(String[] args) {
		SpringApplication.run(BookApplication.class, args);
	}


	/**
	 * 4位验证码 工具类
	 */
	@Bean
	public CircleCaptcha circleCaptcha() {
		return CaptchaUtil.createCircleCaptcha(120, 60, 4, 20);
	}
}
