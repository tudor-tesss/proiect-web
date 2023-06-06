package com.idis.core.business.usersession.commandhandlers;

import com.idis.core.business.usersession.commands.DeleteUserSessionCommand;
import com.idis.core.domain.usersession.UserSession;
import com.idis.shared.database.QueryProvider;
import com.idis.shared.infrastructure.IRequestHandler;

import java.util.concurrent.CompletableFuture;

public final class DeleteUserSessionCommandHandler implements IRequestHandler<DeleteUserSessionCommand, String> {
    @Override
    public CompletableFuture<String> handle(DeleteUserSessionCommand deleteUserSessionCommand) {
        var session = QueryProvider
                .getAll(UserSession.class)
                    .stream()
                    .filter(u -> u.getId().equals(deleteUserSessionCommand.sessionId()))
                .findFirst();

        if (session.isEmpty()) {
            return CompletableFuture.completedFuture("");
        }

        QueryProvider.delete(session.get());

        return CompletableFuture.completedFuture("");
    }
}
