package com.codeflix.admin.video.catalog.infrastructure.castmember.persistence;

import com.codeflix.admin.video.catalog.domain.castmember.CastMember;
import com.codeflix.admin.video.catalog.domain.castmember.CastMemberID;
import com.codeflix.admin.video.catalog.domain.castmember.CastMemberType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity(name = "CastMember")
@Table(name = CastMemberJpaEntity.TABLE_NAME)
public class CastMemberJpaEntity {
    public static final String TABLE_NAME = "cast_member";

    @Id
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private CastMemberType type;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    public CastMemberJpaEntity() {
    }

    private CastMemberJpaEntity(
            final String id,
            final String name,
            final CastMemberType type,
            final Instant createdAt,
            final Instant updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CastMemberJpaEntity from(final CastMember aMember) {
        return new CastMemberJpaEntity(
                aMember.getId().getValue(),
                aMember.getName(),
                aMember.getType(),
                aMember.getCreatedAt(),
                aMember.getUpdatedAt()
        );
    }

    public CastMember toAggregate() {
        return CastMember.with(
                CastMemberID.from(getId()),
                getName(),
                getType(),
                getCreatedAt(),
                getUpdatedAt()
        );
    }

    public String getId() {
        return id;
    }

    public CastMemberJpaEntity setId(final String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CastMemberJpaEntity setName(final String name) {
        this.name = name;
        return this;
    }

    public CastMemberType getType() {
        return type;
    }

    public CastMemberJpaEntity setType(final CastMemberType type) {
        this.type = type;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public CastMemberJpaEntity setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public CastMemberJpaEntity setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
