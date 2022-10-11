package com.codeflix.admin.video.catalog.infrastructure.genre;

import com.codeflix.admin.video.catalog.domain.genre.Genre;
import com.codeflix.admin.video.catalog.domain.genre.GenreGateway;
import com.codeflix.admin.video.catalog.domain.genre.GenreID;
import com.codeflix.admin.video.catalog.domain.pagination.Pagination;
import com.codeflix.admin.video.catalog.domain.pagination.SearchQuery;
import com.codeflix.admin.video.catalog.infrastructure.genre.persistence.GenreJpaEntity;
import com.codeflix.admin.video.catalog.infrastructure.genre.persistence.GenreRepository;
import com.codeflix.admin.video.catalog.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static com.codeflix.admin.video.catalog.infrastructure.utils.SpecificationUtils.like;
import static org.springframework.data.jpa.domain.Specification.where;

@Component
public class GenreMySQLGateway implements GenreGateway {

    private final GenreRepository repository;

    public GenreMySQLGateway(final GenreRepository genreRepository) {
        this.repository = Objects.requireNonNull(genreRepository);
    }

    @Override
    public Genre create(final Genre aGenre) {
        return save(aGenre);
    }

    @Override
    public void deleteById(final GenreID anId) {
        final var aGenreID = anId.getValue();
        if (this.repository.existsById(aGenreID)){
            this.repository.deleteById(aGenreID);
        }
    }

    @Override
    public Optional<Genre> findById(final GenreID anId) {
        return this.repository.findById(anId.getValue())
                .map(GenreJpaEntity::toAggregate);
    }

    @Override
    public Genre update(final Genre aGenre) {
        return save(aGenre);
    }

    @Override
    public Pagination<Genre> findAll(final SearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var where = Optional.ofNullable(aQuery.terms())
                .filter(s -> !s.isBlank())
                .map(this::assembleSpecification)
                .orElse(null);

        final var pageResult = this.repository.findAll(where(where), page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(GenreJpaEntity::toAggregate).toList()
        );
    }

    private Specification<GenreJpaEntity> assembleSpecification(final String terms) {
        return like("name", terms);
    }

    private Genre save(final Genre aGenre) {
        return this.repository.save(GenreJpaEntity.from(aGenre)).toAggregate();
    }
}
