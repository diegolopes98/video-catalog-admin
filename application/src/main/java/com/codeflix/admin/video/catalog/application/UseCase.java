package com.codeflix.admin.video.catalog.application;

public abstract class UseCase<IN, OUT> {

	public abstract OUT execute(IN anIn);
}