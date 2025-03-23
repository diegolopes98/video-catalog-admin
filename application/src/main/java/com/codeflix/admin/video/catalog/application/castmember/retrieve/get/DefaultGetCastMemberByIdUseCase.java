package com.codeflix.admin.video.catalog.application.castmember.retrieve.get;


import com.codeflix.admin.video.catalog.domain.castmember.CastMember;
import com.codeflix.admin.video.catalog.domain.castmember.CastMemberGateway;
import com.codeflix.admin.video.catalog.domain.castmember.CastMemberID;
import com.codeflix.admin.video.catalog.domain.exceptions.NotFoundException;

import java.util.Objects;

public non-sealed class DefaultGetCastMemberByIdUseCase implements GetCastMemberByIdUseCase {

    private final CastMemberGateway castMemberGateway;

    public DefaultGetCastMemberByIdUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public CastMemberOutput execute(final String anIn) {
        final var aMemberId = CastMemberID.from(anIn);
        return this.castMemberGateway.findById(aMemberId)
                .map(CastMemberOutput::from)
                .orElseThrow(() -> NotFoundException.with(CastMember.class, aMemberId));
    }
}
