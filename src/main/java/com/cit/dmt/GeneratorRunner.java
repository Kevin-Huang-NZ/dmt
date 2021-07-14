package com.cit.dmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.alibaba.druid.util.StringUtils;

import mahara.generator.Generator;
import mahara.generator.GeneratorConfig;

@Component
public class GeneratorRunner implements ApplicationRunner {

	@Autowired
	private GeneratorConfig generatorConfig;
	
	@Autowired
	private Generator generator;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (StringUtils.equals(generatorConfig.getOnOff(), "1")) {
			generator.generate();
		}
	}

}
