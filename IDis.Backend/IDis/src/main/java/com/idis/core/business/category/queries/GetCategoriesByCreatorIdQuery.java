package com.idis.core.business.category.queries;

import com.idis.core.domain.category.Category;
import com.idis.shared.infrastructure.IRequest;

import java.util.List;
import java.util.UUID;

public record GetCategoriesByCreatorIdQuery(UUID creatorId) implements IRequest<List<Category>> { }
