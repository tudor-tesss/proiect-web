package com.idis.core.business.user.commandhandlers;


import com.idis.core.business.BusinessErrors;
import com.idis.core.business.user.commands.GetUserByIdCommand;
import com.idis.core.domain.user.User;
import com.nimblej.core.IRequestHandler;
import com.nimblej.networking.database.NimbleJQueryProvider;

import java.util.concurrent.CompletableFuture;

public final class GetUserByIdCommandHandler implements IRequestHandler<GetUserByIdCommand, User> {

    @Override
    public CompletableFuture<User> handle (GetUserByIdCommand getUserByIdCommand) {

        var users = NimbleJQueryProvider.getAll(User.class);

        for ( User user :users) {

            if(getUserByIdCommand.userId().equals(user.getId())){
                return CompletableFuture.completedFuture(user) ;
            }
        }
        throw new IllegalArgumentException(BusinessErrors.User.UserDoesNotExist);
    }
}
