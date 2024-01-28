package com.codeflix.admin.video.catalog.application.castmember.retrieve.get;


import com.codeflix.admin.video.catalog.domain.castmember.CastMember;
import com.codeflix.admin.video.catalog.domain.castmember.CastMemberType;

import java.time.Instant;

public record CastMemberOutput(
        String id,
        String name,
        CastMemberType type,
        Instant createdAt,
        Instant updatedAt
) {

    public static CastMemberOutput from(final CastMember aMember) {
        return new CastMemberOutput(
                aMember.getId().getValue(),
                aMember.getName(),
                aMember.getType(),
                aMember.getCreatedAt(),
                aMember.getUpdatedAt()
        );
    }
}
