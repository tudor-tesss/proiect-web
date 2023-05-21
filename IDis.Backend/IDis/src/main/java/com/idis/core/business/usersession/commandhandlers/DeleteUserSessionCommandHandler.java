package com.idis.core.business.usersession.commandhandlers;

import com.idis.core.business.usersession.commands.DeleteUserSessionCommand;
import com.idis.core.domain.user.UserSession;
import com.nimblej.core.IRequestHandler;
import com.nimblej.networking.database.NimbleJQueryProvider;

import java.util.concurrent.CompletableFuture;

public final class DeleteUserSessionCommandHandler implements IRequestHandler<DeleteUserSessionCommand, String> {
    @Override
    public CompletableFuture<String> handle(DeleteUserSessionCommand deleteUserSessionCommand) {
        var session = NimbleJQueryProvider
                .getAll(UserSession.class)
                    .stream()
                    .filter(u -> u.getId().equals(deleteUserSessionCommand.sessionId()))
                .findFirst();

        if (session.isEmpty()) {
            return CompletableFuture.completedFuture("");
        }

        NimbleJQueryProvider.delete(session.get());

        return CompletableFuture.completedFuture("");
    }
}
