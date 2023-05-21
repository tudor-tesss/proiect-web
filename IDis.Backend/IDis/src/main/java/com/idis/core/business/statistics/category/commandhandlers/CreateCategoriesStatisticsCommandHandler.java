package com.idis.core.business.statistics.category.commandhandlers;

import com.idis.core.business.statistics.category.commandresponses.CreateCategoriesStatisticsCommandResponse;
import com.idis.core.business.statistics.category.commands.CreateCategoriesStatisticsCommand;
import com.nimblej.core.IRequestHandler;

import java.util.concurrent.CompletableFuture;

public final class CreateCategoriesStatisticsCommandHandler implements IRequestHandler<CreateCategoriesStatisticsCommand, CreateCategoriesStatisticsCommandResponse> {
    @Override
    public CompletableFuture<CreateCategoriesStatisticsCommandResponse> handle(CreateCategoriesStatisticsCommand createCategoriesStatisticsCommand) {
        return null;
    }
}
