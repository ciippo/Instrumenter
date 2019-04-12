package com.instrumentation.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Spring {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("spring-config.xml");
		Object bean = appContext.getBean("test");
		System.out.println(bean.getClass());
	}
}
