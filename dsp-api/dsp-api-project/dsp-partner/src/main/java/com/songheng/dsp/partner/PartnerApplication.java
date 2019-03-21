package com.songheng.dsp.partner;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author: luoshaobing
 * @date: 2019/3/21 11:58
 * @description: PartnerApplication启动类
 */
@SpringBootApplication
@EnableDubboConfig
@EnableScheduling
public class PartnerApplication {
    public static void main(String[] args){
        //设置dubbo使用slf4j来桥接，再由slf4j 转接到 log4j2 进行日志输出
        System.setProperty("dubbo.application.logger","slf4j");
        SpringApplication.run(PartnerApplication.class, args);
    }
}