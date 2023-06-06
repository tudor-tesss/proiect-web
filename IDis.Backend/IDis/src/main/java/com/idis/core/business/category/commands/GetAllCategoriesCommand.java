package com.idis.core.business.category.commands;

import com.idis.core.domain.category.Category;
import com.idis.shared.infrastructure.IRequest;

import java.util.List;

public record GetAllCategoriesCommand() implements IRequest<List<Category>> { }
