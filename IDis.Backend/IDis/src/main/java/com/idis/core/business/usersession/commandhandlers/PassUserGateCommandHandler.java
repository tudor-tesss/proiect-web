package com.idis.core.business.usersession.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.usersession.commandresponses.PassUserGateCommandResponse;
import com.idis.core.business.usersession.commands.PassUserGateCommand;
import com.idis.core.domain.user.UserGate;
import com.nimblej.core.IRequestHandler;
import com.nimblej.networking.database.NimbleJQueryProvider;

import java.util.concurrent.CompletableFuture;

public final class PassUserGateCommandHandler implements IRequestHandler<PassUserGateCommand, PassUserGateCommandResponse> {
    @Override
    public CompletableFuture<PassUserGateCommandResponse> handle(PassUserGateCommand passUserGateCommand) {
        var userGates = NimbleJQueryProvider.getAll(UserGate.class);
        userGates.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        var latestUserGate = userGates.stream().findFirst();

        if (latestUserGate.isEmpty()) {
            throw new IllegalArgumentException(BusinessErrors.UserGate.Pass.UserGateDoesNotExist);
        }

        try {
            var userGate = latestUserGate.get();
            userGate.pass(passUserGateCommand.code());
            NimbleJQueryProvider.insert(userGate);

            var userGateResponse = new PassUserGateCommandResponse(userGate.getUserId());

            return CompletableFuture.completedFuture(userGateResponse);
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
