package com.codeflix.admin.video.catalog.application.category.create;

import com.codeflix.admin.video.catalog.domain.category.CategoryGateway;
import com.codeflix.admin.video.catalog.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {

    @InjectMocks
    private DefaultCreateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_thenShouldReturnCategoryId() {
        final var expectedName = "Movies";
        final var expectedDescription = "Movies Category";
        final var expectedIsActive = true;

        final var aCommand =
                CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        Mockito
                .when(categoryGateway.create(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());
        Mockito.verify(categoryGateway, Mockito.times(1))
                .create(Mockito.argThat(aCategory -> Objects.equals(expectedName, aCategory.getName())
                        && Objects.equals(expectedDescription, aCategory.getDescription())
                        && Objects.equals(expectedIsActive, aCategory.isActive())
                        && Objects.nonNull(aCategory.getId())
                        && Objects.nonNull(aCategory.getCreatedAt())
                        && Objects.nonNull(aCategory.getUpdatedAt())
                        && Objects.isNull(aCategory.getDeletedAt())));
    }

    @Test
    public void givenAValidCommandWithInactiveCategory_whenCallsCreateCategory_thenShouldReturnCategoryId() {
        final var expectedName = "Movies";
        final var expectedDescription = "Movies Category";
        final var expectedIsActive = false;

        final var aCommand =
                CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        Mockito
                .when(categoryGateway.create(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());
        Mockito.verify(categoryGateway, Mockito.times(1))
                .create(Mockito.argThat(aCategory -> Objects.equals(expectedName, aCategory.getName())
                        && Objects.equals(expectedDescription, aCategory.getDescription())
                        && Objects.equals(expectedIsActive, aCategory.isActive())
                        && Objects.nonNull(aCategory.getId())
                        && Objects.nonNull(aCategory.getCreatedAt())
                        && Objects.nonNull(aCategory.getUpdatedAt())
                        && Objects.nonNull(aCategory.getDeletedAt())));
    }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnDomainException() {
        final String expectedName = null;
        final var expectedDescription = "Movies Category";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand =
                CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
        Mockito.verify(categoryGateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsException_thenShouldReturnException() {
        final var expectedName = "Movies";
        final var expectedDescription = "Movies Category";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "gateway mock exception";
        final var expectedErrorCount = 1;

        final var aCommand =
                CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        Mockito
                .when(categoryGateway.create(Mockito.any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException =
                Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
        Mockito.verify(categoryGateway, Mockito.times(1))
                .create(Mockito.argThat(aCategory -> Objects.equals(expectedName, aCategory.getName())
                        && Objects.equals(expectedDescription, aCategory.getDescription())
                        && Objects.equals(expectedIsActive, aCategory.isActive())
                        && Objects.nonNull(aCategory.getId())
                        && Objects.nonNull(aCategory.getCreatedAt())
                        && Objects.nonNull(aCategory.getUpdatedAt())
                        && Objects.isNull(aCategory.getDeletedAt())));
    }
}
