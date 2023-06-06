package com.idis.core.business.category.commands;

import com.idis.core.domain.category.Category;
import com.idis.shared.infrastructure.IRequest;

import java.util.List;
import java.util.UUID;

public record GetCategoriesByCreatorIdCommand(UUID creatorId) implements IRequest<List<Category>> { }
