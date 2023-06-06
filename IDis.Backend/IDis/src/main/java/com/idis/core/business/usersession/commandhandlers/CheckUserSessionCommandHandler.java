package com.idis.core.business.usersession.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.usersession.commandresponses.CheckUserSessionCommandResponse;
import com.idis.core.business.usersession.commands.CheckUserSessionCommand;
import com.idis.core.domain.usersession.UserSession;
import com.idis.shared.database.QueryProvider;
import com.idis.shared.infrastructure.IRequestHandler;
import com.idis.shared.time.TimeProviderContext;

import java.util.concurrent.CompletableFuture;

import static com.idis.shared.functional.FunctionalExtensions.filter;

public final class CheckUserSessionCommandHandler implements IRequestHandler<CheckUserSessionCommand, CheckUserSessionCommandResponse> {
    @Override
    public CompletableFuture<CheckUserSessionCommandResponse> handle(CheckUserSessionCommand request) {
        var userSession = filter(
                    QueryProvider.getAll(UserSession.class),
                    u -> u.getId().equals(request.sessionId()))
                .stream().findFirst();

        if (userSession.isEmpty()) {
            throw new IllegalArgumentException(BusinessErrors.UserSession.Check.UserSessionDoesNotExist);
        }

        if (!userSession.get().getUserIpAddress().equals(request.ipAddress())) {
            throw new IllegalArgumentException(BusinessErrors.UserSession.Check.UserSessionIpAddressDoesNotMatch);
        }

        if (userSession.get().getCreatedAt().getTime() + 1000 * 60 * 60 * 24 < TimeProviderContext.getCurrentProvider().now().getTime()) { // 24 hours
            throw new IllegalArgumentException(BusinessErrors.UserSession.Check.UserSessionExpired);
        }

        if (!userSession.get().getUserId().equals(request.userId())) {
            throw new IllegalArgumentException(BusinessErrors.UserSession.Check.UserSessionUserIdDoesNotMatch);
        }

        try {
            return CompletableFuture.completedFuture(new CheckUserSessionCommandResponse(userSession.get().getUserId()));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
