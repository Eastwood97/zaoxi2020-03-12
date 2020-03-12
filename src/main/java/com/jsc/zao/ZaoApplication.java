package com.jsc.zao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

//@ImportResource(locations = {"classpath*:spring-aop-aspectJ.xml"})
@EnableAspectJAutoProxy
@EnableScheduling
@SpringBootApplication
public class ZaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZaoApplication.class, args);


    }

}
