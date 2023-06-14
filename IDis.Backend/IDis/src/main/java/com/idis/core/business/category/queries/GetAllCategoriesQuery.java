package com.idis.core.business.category.queries;

import com.idis.core.domain.category.Category;
import com.idis.shared.infrastructure.IRequest;

import java.util.List;

public record GetAllCategoriesQuery() implements IRequest<List<Category>> { }
