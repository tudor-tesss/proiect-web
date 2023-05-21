package com.idis.core.business.statistics.category.commands;

import com.idis.core.business.statistics.category.commandresponses.CreateCategoriesStatisticsCommandResponse;
import com.nimblej.core.IRequest;

public record CreateCategoriesStatisticsCommand() implements IRequest<CreateCategoriesStatisticsCommandResponse> { }
