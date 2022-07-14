package com.codeflix.admin.video.catalog.infrastructure.category;

import com.codeflix.admin.video.catalog.infrastructure.MySQLGatewayTest;
import com.codeflix.admin.video.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySQLGatewayTest
public class CategoryMySQLGatewayTest {

	@Autowired
	private CategoryMySQLGateway gateway;

	@Autowired
	private CategoryRepository repository;

	@Test
	public void testInjectedDependencies() {
		Assertions.assertNotNull(gateway);
		Assertions.assertNotNull(repository);
	}
}
