package com.codeflix.admin.video.catalog.application.castmember.retrieve.get;

import com.codeflix.admin.video.catalog.application.UseCase;

public sealed abstract class GetCastMemberByIdUseCase
        extends UseCase<String, CastMemberOutput>
        permits DefaultGetCastMemberByIdUseCase {
}
