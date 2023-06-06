package com.idis.core.business.category.commands;

import com.idis.core.domain.category.Category;
import com.idis.shared.infrastructure.IRequest;

import java.util.List;
import java.util.UUID;

public record CreateCategoryCommand(String name, List<String> ratingFields, UUID creatorId) implements IRequest<Category> { }
