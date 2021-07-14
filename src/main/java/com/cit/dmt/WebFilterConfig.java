package com.cit.dmt;

import javax.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;

import mahara.web.filter.AuthFilter;
import mahara.web.filter.CorsFilter;
//import mahara.web.filter.GetMethodConvertingFilter;

@Configuration
public class WebFilterConfig {

//	@Bean
//	public Filter getMethodConvertingFilter() {
//		return new GetMethodConvertingFilter();
//	}
//
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@Bean
//	public FilterRegistrationBean getMethodConvertingFilterRegistration() {
//		FilterRegistrationBean registration = new FilterRegistrationBean();
//		registration.setFilter(new DelegatingFilterProxy("getMethodConvertingFilter"));
//		registration.addUrlPatterns("/*");
//		registration.setName("getMethodConvertingFilter");
//		registration.setOrder(1);
//		return registration;
//	}

	@Bean
	public Filter corsFilter() {
		return new CorsFilter();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public FilterRegistrationBean corsFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new DelegatingFilterProxy("corsFilter"));
		registration.addUrlPatterns("/*");
		registration.setName("corsFilter");
		registration.setOrder(2);
		return registration;
	}

	@Bean
	public Filter authFilter() {
		return new AuthFilter();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public FilterRegistrationBean authFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new DelegatingFilterProxy("authFilter"));
		registration.addUrlPatterns("/*");
		registration.setName("authFilter");
		registration.setOrder(3);
		return registration;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setForceEncoding(true);
		characterEncodingFilter.setEncoding("UTF-8");
		registrationBean.setFilter(characterEncodingFilter);
		return registrationBean;
	}
}
