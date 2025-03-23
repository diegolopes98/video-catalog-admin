package com.codeflix.admin.video.catalog.application.castmember.update;

import com.codeflix.admin.video.catalog.application.UseCase;

public sealed interface UpdateCastMemberUseCase
        extends UseCase<UpdateCastMemberCommand, UpdateCastMemberOutput>
        permits DefaultUpdateCastMemberUseCase {
}
