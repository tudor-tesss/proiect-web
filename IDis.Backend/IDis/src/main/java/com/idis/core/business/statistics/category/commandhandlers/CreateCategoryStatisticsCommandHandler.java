package com.idis.core.business.statistics.category.commandhandlers;

import com.idis.core.business.statistics.category.commandresponses.CreateCategoryStatisticsCommandResponse;
import com.idis.core.business.statistics.category.commands.CreateCategoryStatisticsCommand;
import com.nimblej.core.IRequestHandler;

import java.util.concurrent.CompletableFuture;

public final class CreateCategoryStatisticsCommandHandler implements IRequestHandler<CreateCategoryStatisticsCommand, CreateCategoryStatisticsCommandResponse> {
    @Override
    public CompletableFuture<CreateCategoryStatisticsCommandResponse> handle(CreateCategoryStatisticsCommand createCategoryStatisticsCommand) {
        return null;
    }
}