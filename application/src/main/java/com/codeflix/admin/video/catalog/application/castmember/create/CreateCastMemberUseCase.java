package com.codeflix.admin.video.catalog.application.castmember.create;

import com.codeflix.admin.video.catalog.application.UseCase;

public sealed abstract class CreateCastMemberUseCase
        extends UseCase<CreateCastMemberCommand, CreateCastMemberOutput>
        permits DefaultCreateCastMemberUseCase {
}
