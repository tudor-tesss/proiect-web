package com.idis.core.business.user.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.user.commands.GetUserByIdCommand;
import com.idis.core.domain.user.User;
import com.idis.shared.database.QueryProvider;
import com.idis.shared.infrastructure.IRequestHandler;

import java.util.concurrent.CompletableFuture;

public final class GetUserByIdCommandHandler implements IRequestHandler<GetUserByIdCommand, User> {
    @Override
    public CompletableFuture<User> handle (GetUserByIdCommand getUserByIdCommand) {
        var users = QueryProvider.getAll(User.class);
        for (var user : users) {
            if (getUserByIdCommand.userId().equals(user.getId())){
                return CompletableFuture.completedFuture(user) ;
            }
        }

        throw new IllegalArgumentException(BusinessErrors.User.UserDoesNotExist);
    }
}
