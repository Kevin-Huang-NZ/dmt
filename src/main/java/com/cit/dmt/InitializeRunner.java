package com.cit.dmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.cit.dmt.core.service.AuthService;
import com.cit.dmt.core.service.FileUploadService;

@Component
public class InitializeRunner implements ApplicationRunner {

	@Autowired
	private AuthService authService;

	@Autowired
	private FileUploadService fileUploadService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		fileUploadService.init();
		authService.getAndCacheRoleFuns();
	}

}
