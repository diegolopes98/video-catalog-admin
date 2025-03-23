package com.codeflix.admin.video.catalog.application.castmember.create;

import com.codeflix.admin.video.catalog.domain.castmember.CastMember;
import com.codeflix.admin.video.catalog.domain.castmember.CastMemberGateway;

import java.util.Objects;

public non-sealed class DefaultCreateCastMemberUseCase implements CreateCastMemberUseCase {

    private final CastMemberGateway castMemberGateway;

    public DefaultCreateCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public CreateCastMemberOutput execute(final CreateCastMemberCommand aCommand) {
        final var aName = aCommand.name();
        final var aType = aCommand.type();

        final var aMember = CastMember.newMember(aName, aType);

        return CreateCastMemberOutput.from(this.castMemberGateway.create(aMember));
    }
}
