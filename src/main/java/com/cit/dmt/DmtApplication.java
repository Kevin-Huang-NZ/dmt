package com.cit.dmt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = { "com.cit.dmt", "mahara" })
@MapperScan("com.cit.dmt.core.dao")
//@EnableConfigurationProperties
public class DmtApplication {

	public static void main(String[] args) {
		SpringApplication.run(DmtApplication.class, args);
	}

}
