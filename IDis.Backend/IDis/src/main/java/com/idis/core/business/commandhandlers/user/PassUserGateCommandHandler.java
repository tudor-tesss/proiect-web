package com.idis.core.business.commandhandlers.user;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.commands.user.PassUserGateCommand;
import com.idis.core.domain.user.UserGate;
import com.nimblej.core.IRequestHandler;
import com.nimblej.networking.database.NimbleJQueryProvider;

import java.util.concurrent.CompletableFuture;

public final class PassUserGateCommandHandler implements IRequestHandler<PassUserGateCommand, String> {
    @Override
    public CompletableFuture<String> handle(PassUserGateCommand passUserGateCommand) {
        var userGates = NimbleJQueryProvider.getAll(UserGate.class);
        userGates.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        var latestUserGate = userGates.stream().findFirst();

        if (latestUserGate.isEmpty()) {
            throw new IllegalArgumentException(BusinessErrors.UserGate.Pass.UserGateDoesNotExist);
        }

        try {
            latestUserGate.get().pass(passUserGateCommand.code());
            NimbleJQueryProvider.insert(latestUserGate.get());

            return CompletableFuture.completedFuture("Success");
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
