package com.codeflix.admin.video.catalog.domain.category;

import com.codeflix.admin.video.catalog.domain.AggregateRoot;
import com.codeflix.admin.video.catalog.domain.exceptions.NotificationException;
import com.codeflix.admin.video.catalog.domain.utils.InstantUtils;
import com.codeflix.admin.video.catalog.domain.validation.ValidationHandler;
import com.codeflix.admin.video.catalog.domain.validation.handler.NotificationValidationHandler;

import java.time.Instant;
import java.util.Objects;

public class Category extends AggregateRoot<CategoryID> {

	private String name;
	private String description;
	private boolean active;
	private Instant createdAt;
	private Instant updatedAt;
	private Instant deletedAt;

	private Category(
			final CategoryID anId,
			final String aName,
			final String aDescription,
			final boolean isActive,
			final Instant aCreationDate,
			final Instant aUpdateDate,
			final Instant aDeleteDate
	) {
		super(anId);
		this.name = aName;
		this.description = aDescription;
		this.active = isActive;
		this.createdAt = Objects.requireNonNull(aCreationDate, "'createdAt' should not be null");
		this.updatedAt = Objects.requireNonNull(aUpdateDate, "'updatedAt' should not be null");
		this.deletedAt = aDeleteDate;

		selfValidate();
	}

	public static Category newCategory(
			final String aName,
			final String aDescription,
			final boolean isActive
	) {
		final var id = CategoryID.unique();
		final var now = InstantUtils.now();
		final var deletedAt = isActive ? null : now;
		return new Category(
				id,
				aName,
				aDescription,
				isActive,
				now,
				now,
				deletedAt
		);
	}

	public static Category from(Category aCategory) {
		return with(
				aCategory.getId(),
				aCategory.getName(),
				aCategory.getDescription(),
				aCategory.isActive(),
				aCategory.getCreatedAt(),
				aCategory.getUpdatedAt(),
				aCategory.getDeletedAt()
		);
	}

	public static Category with(
			final CategoryID anId,
			final String aName,
			final String aDescription,
			final boolean isActive,
			final Instant aCreationDate,
			final Instant anUpdateDate,
			final Instant aDeleteDate
	) {
		return new Category(
				anId,
				aName,
				aDescription,
				isActive,
				aCreationDate,
				anUpdateDate,
				aDeleteDate
		);
	}

	@Override
	public void validate(final ValidationHandler handler) {
		new CategoryValidator(this, handler).validate();
	}

	public Category activate() {
		final var now = InstantUtils.now();
		this.deletedAt = null;
		this.active = true;
		this.updatedAt = now;
		return this;
	}

	public Category deactivate() {
		final var now = InstantUtils.now();
		if (getDeletedAt() == null) {
			this.deletedAt = now;
		}
		this.active = false;
		this.updatedAt = now;
		return this;
	}

	public Category update(final String aName, final String aDescription, final boolean isActive) {
		if (isActive) {
			activate();
		} else {
			deactivate();
		}
		this.name = aName;
		this.description = aDescription;
		this.updatedAt = InstantUtils.now();

		selfValidate();

		return this;
	}

	public CategoryID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public boolean isActive() {
		return active;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public Instant getDeletedAt() {
		return deletedAt;
	}

	private void selfValidate() {
		final var notificationHandler = NotificationValidationHandler.create();

		validate(notificationHandler);

		if (notificationHandler.hasErrors()) {
			throw new NotificationException("Failed to validate Aggregate Category", notificationHandler);
		}
	}
}
