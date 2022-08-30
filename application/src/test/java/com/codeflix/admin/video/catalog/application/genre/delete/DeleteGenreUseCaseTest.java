package com.codeflix.admin.video.catalog.application.genre.delete;

import com.codeflix.admin.video.catalog.application.UseCaseTest;
import com.codeflix.admin.video.catalog.domain.genre.Genre;
import com.codeflix.admin.video.catalog.domain.genre.GenreGateway;
import com.codeflix.admin.video.catalog.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DeleteGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(
                genreGateway
        );
    }

    @Test
    public void givenAValidGenreID_whenCallsDeleteGenre_thenShouldDeleteProperly() {
        // given
        final var aGenre = Genre.newGenre("Action", true);
        final var expectedID = aGenre.getId();

        doNothing()
                .when(genreGateway).deleteById(any());
        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedID.getValue()));

        // then
        Mockito.verify(genreGateway, times(1)).deleteById(expectedID);
    }


    @Test
    public void givenAnInvalidGenreID_whenCallsDeleteGenre_thenShouldDoNothing() {
        // given
        final var expectedID = GenreID.from("123");

        doNothing()
                .when(genreGateway).deleteById(any());
        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedID.getValue()));

        // then
        Mockito.verify(genreGateway, times(1)).deleteById(expectedID);
    }

    @Test
    public void givenAValidGenreID_whenCallsDeleteGenreAndGatewayThrows_thenShouldThrowException() {
        // given
        final var aGenre = Genre.newGenre("Action", true);
        final var expectedID = aGenre.getId();

        doThrow(new IllegalStateException("Gateway error"))
                .when(genreGateway).deleteById(any());
        // when
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(expectedID.getValue())
        );

        // then
        Mockito.verify(genreGateway, times(1)).deleteById(expectedID);
    }
}
