package com.codeflix.admin.video.catalog.e2e.category;

import com.codeflix.admin.video.catalog.E2ETest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@E2ETest
@Testcontainers
public class CategoryE2ETest {

	@Container
	private static final MySQLContainer MYSQL_CONTAINER =
			new MySQLContainer("mysql:8.0.29")
					.withPassword("123456")
					.withUsername("root")
					.withDatabaseName("videos_adm");

	@DynamicPropertySource
	public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
		final var mappedPort = MYSQL_CONTAINER.getMappedPort(3306);
		registry.add("mysql.port", () -> mappedPort);
	}
}
