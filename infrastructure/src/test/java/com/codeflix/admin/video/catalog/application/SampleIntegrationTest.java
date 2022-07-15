package com.codeflix.admin.video.catalog.application;

import com.codeflix.admin.video.catalog.IntegrationTest;
import com.codeflix.admin.video.catalog.application.category.create.CreateCategoryUseCase;
import com.codeflix.admin.video.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class SampleIntegrationTest {

	@Autowired
	private CreateCategoryUseCase useCase;

	@Autowired
	private CategoryRepository repository;

	@Test
	public void testBeansInjection() {
		Assertions.assertNotNull(useCase);
		Assertions.assertNotNull(repository);
	}
}
