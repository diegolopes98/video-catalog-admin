package com.codeflix.admin.video.catalog.application;

public interface UseCase<IN, OUT> {
	OUT execute(IN anIn);
}