package com.example.todoManager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "jwt.secret=test-secret")
class TodoManagerApplicationTests {

	@Test
	void contextLoads() {
	}

}
