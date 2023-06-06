package com.idis.core.business.user.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.user.commands.CreateUserCommand;
import com.idis.core.domain.user.User;
import com.idis.shared.database.QueryProvider;
import com.idis.shared.infrastructure.IRequestHandler;

import java.util.concurrent.CompletableFuture;

import static com.idis.shared.functional.FunctionalExtensions.any;

public final class CreateUserCommandHandler implements IRequestHandler<CreateUserCommand, User> {
    @Override
    public CompletableFuture<User> handle(CreateUserCommand createUserCommand) {
        var users = QueryProvider.getAll(User.class);

        var duplicateResult = any(users, user -> user.getEmailAddress().equals(createUserCommand.emailAddress()));
        if (duplicateResult) {
            throw new IllegalArgumentException(BusinessErrors.User.UserAlreadyExists);
        }

        try {
            var user = User.create(createUserCommand.name(), createUserCommand.firstName(), createUserCommand.emailAddress());
            QueryProvider.insert(user);

            return CompletableFuture.completedFuture(user);
        }
        catch (Exception e) {
            throw e;
        }
    }
}
