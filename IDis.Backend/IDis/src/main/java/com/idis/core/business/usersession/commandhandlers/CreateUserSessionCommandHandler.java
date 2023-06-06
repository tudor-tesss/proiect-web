package com.idis.core.business.usersession.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.usersession.commandresponses.CreateUserSessionCommandResponse;
import com.idis.core.business.usersession.commands.CreateUserSessionCommand;
import com.idis.core.domain.user.User;
import com.idis.core.domain.usersession.UserSession;
import com.idis.shared.database.QueryProvider;
import com.idis.shared.infrastructure.IRequestHandler;

import java.util.concurrent.CompletableFuture;

import static com.idis.shared.functional.FunctionalExtensions.filter;

public final class CreateUserSessionCommandHandler implements IRequestHandler<CreateUserSessionCommand, CreateUserSessionCommandResponse> {
    @Override
    public CompletableFuture<CreateUserSessionCommandResponse> handle(CreateUserSessionCommand request) {
        var user = filter(
                        QueryProvider.getAll(User.class),
                        u -> u.getId().equals(request.userId()))
                .stream().findFirst();

        if (user.isEmpty()) {
            throw new IllegalArgumentException(BusinessErrors.UserSession.Create.UserDoesNotExist);
        }

        try {
            var userSession = UserSession.create(user.get().getId(), request.userIpAddress());
            QueryProvider.insert(userSession);

            var response = new CreateUserSessionCommandResponse(userSession.getId());

            return CompletableFuture.completedFuture(response);
        }
        catch (Exception e) {
            throw e;
        }
    }
}
