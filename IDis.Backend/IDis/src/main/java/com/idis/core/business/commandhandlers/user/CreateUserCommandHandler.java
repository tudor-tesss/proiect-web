package com.idis.core.business.commandhandlers.user;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.commands.user.CreateUserCommand;
import com.idis.core.domain.user.User;
import com.nimblej.core.IRequestHandler;
import com.nimblej.networking.database.NimbleJQueryProvider;

import java.util.concurrent.CompletableFuture;

import static com.nimblej.extensions.functional.FunctionalExtensions.any;

public final class CreateUserCommandHandler implements IRequestHandler<CreateUserCommand, User> {
    @Override
    public CompletableFuture<User> handle(CreateUserCommand createUserCommand) {
        var users = NimbleJQueryProvider.getAll(User.class);

        var duplicateResult = any(users, user -> user.getEmailAddress().equals(createUserCommand.emailAddress()));
        if (duplicateResult) {
            throw new IllegalArgumentException(BusinessErrors.User.UserAlreadyExists);
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                var user = User.create(createUserCommand.name(), createUserCommand.firstName(), createUserCommand.emailAddress());
                NimbleJQueryProvider.insert(user);

                return user;
            }
            catch (Exception e) {
                throw e;
            }
        });
    }
}
