package com.codeflix.admin.video.catalog.application.castmember.delete;


import com.codeflix.admin.video.catalog.application.UnitUseCase;

public sealed abstract class DeleteCastMemberUseCase
        extends UnitUseCase<String>
        permits DefaultDeleteCastMemberUseCase {
}
