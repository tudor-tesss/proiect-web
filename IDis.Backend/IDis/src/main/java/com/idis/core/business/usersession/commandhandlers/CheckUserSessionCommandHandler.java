package com.idis.core.business.usersession.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.usersession.commandresponses.CheckUserSessionCommandResponse;
import com.idis.core.business.usersession.commands.CheckUserSessionCommand;
import com.idis.core.domain.user.UserSession;
import com.nimblej.core.IRequestHandler;
import com.nimblej.networking.database.NimbleJQueryProvider;

import java.util.concurrent.CompletableFuture;

import static com.nimblej.extensions.functional.FunctionalExtensions.*;

public final class CheckUserSessionCommandHandler implements IRequestHandler<CheckUserSessionCommand, CheckUserSessionCommandResponse> {
    @Override
    public CompletableFuture<CheckUserSessionCommandResponse> handle(CheckUserSessionCommand checkUserSessionCommand) {
        var userSession = filter(
                    NimbleJQueryProvider.getAll(UserSession.class),
                    u -> u.getId().equals(checkUserSessionCommand.sessionId()))
                .stream().findFirst();


        System.out.println("User ID: " + userSession.get().getUserId() + " Command User ID: " + checkUserSessionCommand.userId());
        System.out.println("User IP: " + userSession.get().getUserIpAddress() + " Command User IP: " + checkUserSessionCommand.ipAddress());

        if (userSession.isEmpty()) {
            throw new IllegalArgumentException(BusinessErrors.UserSession.Check.UserSessionDoesNotExist);
        }

        if (!userSession.get().getUserIpAddress().equals(checkUserSessionCommand.ipAddress())) {
            throw new IllegalArgumentException(BusinessErrors.UserSession.Check.UserSessionIpAddressDoesNotMatch);
        }

        if (userSession.get().getCreatedAt().getTime() + 1000 * 60 * 60 * 24 < System.currentTimeMillis()) { // 24 hours
            throw new IllegalArgumentException(BusinessErrors.UserSession.Check.UserSessionExpired);
        }

        if (!userSession.get().getUserId().equals(checkUserSessionCommand.userId())) {
            throw new IllegalArgumentException(BusinessErrors.UserSession.Check.UserSessionUserIdDoesNotMatch);
        }


        try {
            return CompletableFuture.completedFuture(new CheckUserSessionCommandResponse(userSession.get().getUserId()));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
