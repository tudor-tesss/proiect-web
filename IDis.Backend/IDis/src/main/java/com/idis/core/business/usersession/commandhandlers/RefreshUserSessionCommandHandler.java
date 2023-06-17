package com.idis.core.business.usersession.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.usersession.commandresponses.RefreshUserSessionCommandResponse;
import com.idis.core.business.usersession.commands.RefreshUserSessionCommand;
import com.idis.core.domain.usersession.UserSession;
import com.idis.shared.database.QueryProvider;
import com.idis.shared.infrastructure.IRequestHandler;
import com.idis.shared.time.TimeProviderContext;

import java.util.concurrent.CompletableFuture;

public final class RefreshUserSessionCommandHandler implements IRequestHandler<RefreshUserSessionCommand, RefreshUserSessionCommandResponse> {
    @Override
    public CompletableFuture<RefreshUserSessionCommandResponse> handle(RefreshUserSessionCommand request) {
        return QueryProvider.getByIdAsync(UserSession.class, request.sessionId())
                .thenApplyAsync(u -> {
                    if (u.isEmpty()) {
                        throw new IllegalArgumentException(BusinessErrors.UserSession.Check.UserSessionDoesNotExist);
                    }

                    if (!u.get().getUserIpAddress().equals(request.ipAddress())) {
                        throw new IllegalArgumentException(BusinessErrors.UserSession.Check.UserSessionIpAddressDoesNotMatch);
                    }

                    if (u.get().getCreatedAt().getTime() + 1000 * 60 * 60 * 24 < TimeProviderContext.getCurrentProvider().now().getTime()) { // 24 hours
                        throw new IllegalArgumentException(BusinessErrors.UserSession.Check.UserSessionExpired);
                    }

                    if (!u.get().getUserId().equals(request.userId())) {
                        throw new IllegalArgumentException(BusinessErrors.UserSession.Check.UserSessionUserIdDoesNotMatch);
                    }

                    u.get().refresh();
                    try {
                        QueryProvider.insert(u.get());
                        return new RefreshUserSessionCommandResponse(u.get().getId());
                    }
                    catch (Exception e) {
                        throw e;
                    }
                });
    }
}
