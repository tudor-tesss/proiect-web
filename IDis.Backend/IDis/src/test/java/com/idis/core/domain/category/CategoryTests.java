package com.idis.core.domain.category;

import com.idis.core.domain.DomainErrors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryTests {
    @ParameterizedTest
    @NullAndEmptySource
    public void given_create_when_nameIsNullOrEmpty_then_shouldThrowIllegalArgumentException(String name) {
        // Arrange
        List<String> ratings = new ArrayList<>();
        ratings.add("rat1");
        ratings.add("rat2");

        // Act
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            Category.create(name,ratings, UUID.randomUUID());
        });

        // Assert
        var actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(DomainErrors.Category.NameNullOrEmpty));
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void given_create_when_ratingFieldIsNullOrEmpty_then_shouldThrowIllegalArgumentException(String rating) {
        // Arrange
        var name = "categoryName";
        List ratingField = new ArrayList<>();
        ratingField.add(rating);

        // Act
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            Category.create(name,ratingField, UUID.randomUUID());
        });

        // Assert
        var actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(DomainErrors.Category.RatingFieldNullOrEmpty));
    }

    @Test
    public void given_create_when_notViolatingConstraints_then_shouldCreate() {
        // Arrange
        var name = "name";
        List<String> ratings = new ArrayList<>();
        ratings.add("rat1");
        ratings.add("rat2");
        var creatorId = UUID.randomUUID();

        // Act
        var category = Category.create(name, ratings,creatorId);

        // Assert
        assertNotNull(category);
        assertEquals(category.getName(),name);
        assertEquals(category.getRatingFields(),ratings);
        assertEquals(category.getCreatorId(),creatorId);
    }
}
