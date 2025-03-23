package com.codeflix.admin.video.catalog.application.castmember.create;

import com.codeflix.admin.video.catalog.application.UseCase;

public sealed interface CreateCastMemberUseCase
        extends UseCase<CreateCastMemberCommand, CreateCastMemberOutput>
        permits DefaultCreateCastMemberUseCase {
}
