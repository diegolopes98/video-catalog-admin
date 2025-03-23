package com.codeflix.admin.video.catalog.application.castmember.retrieve.get;

import com.codeflix.admin.video.catalog.application.UseCase;

public sealed interface GetCastMemberByIdUseCase
        extends UseCase<String, CastMemberOutput>
        permits DefaultGetCastMemberByIdUseCase {
}
