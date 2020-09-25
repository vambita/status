package com.vambita.work.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.ServletContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class StatusApplicationTests {

	@Autowired
	private ServletContext context;

	@Test
	void contextLoads() {
		assertEquals("MockServletContext", context.getServerInfo());
	}

}
