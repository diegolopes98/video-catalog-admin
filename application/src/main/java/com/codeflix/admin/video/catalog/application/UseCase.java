package com.codeflix.admin.video.catalog.application;

import com.codeflix.admin.video.catalog.domain.category.Category;

public class UseCase {

    public Category execute() {
        return Category.newCategory("movie", "description", true);
    }
}