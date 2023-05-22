package com.idis.core.domain.category;

import com.idis.core.domain.DomainErrors;
import com.nimblej.core.BaseObject;

import java.time.LocalDate;
import java.util.*;

public final class Category extends BaseObject {

    private String name;
    private UUID creatorId;
    private List<String> ratingFields;
    private Date createdAt;

    public Category() { }

    private Category(String name, List<String> ratingFields, UUID creatorId) {
        super();
        this.creatorId=creatorId;
        this.name = name;
        this.ratingFields = ratingFields;
        this.createdAt = new Date();
    }

    public static Category create(String name, List<String> ratingFields, UUID creatorId) throws IllegalArgumentException {
        var nameResult = name != null && !name.isBlank();

        for (var rating: ratingFields){

            var ratingResult = rating != null && !rating.isBlank();

            if(!ratingResult){
                throw new IllegalArgumentException(DomainErrors.Category.RatingFieldNullOrEmpty);
            }
        }

        if (!nameResult) {
            throw new IllegalArgumentException(DomainErrors.Category.NameNullOrEmpty);
        }
        return new Category(name, ratingFields, creatorId);
    }

    public String getName() {
        return name;
    }

    public UUID getCreatorId() {
        return creatorId;
    }

    public List<String> getRatingFields() {
        return ratingFields;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
