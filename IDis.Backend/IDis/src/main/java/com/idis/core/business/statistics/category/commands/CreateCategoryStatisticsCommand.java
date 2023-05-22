package com.idis.core.business.statistics.category.commands;

import com.idis.core.business.statistics.category.commandresponses.CreateCategoryStatisticsCommandResponse;
import com.nimblej.core.IRequest;

import java.util.UUID;

public record CreateCategoryStatisticsCommand(UUID categoryId) implements IRequest<CreateCategoryStatisticsCommandResponse> { }