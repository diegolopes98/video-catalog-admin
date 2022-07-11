package com.codeflix.admin.video.catalog.application;

import com.codeflix.admin.video.catalog.domain.Category;

public class UseCase {

    public Category execute() {
        return new Category();
    }
}