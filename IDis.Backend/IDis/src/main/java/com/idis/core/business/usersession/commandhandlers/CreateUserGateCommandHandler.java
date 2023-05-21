package com.idis.core.business.usersession.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.usersession.commands.CreateUserGateCommand;
import com.idis.core.domain.user.User;
import com.idis.core.domain.user.UserGate;
import com.idis.infrastructure.services.SendGridService;
import com.idis.infrastructure.services.SendGridTemplates;
import com.nimblej.core.IRequestHandler;
import com.nimblej.networking.database.NimbleJQueryProvider;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.nimblej.extensions.functional.FunctionalExtensions.*;

public final class CreateUserGateCommandHandler implements IRequestHandler<CreateUserGateCommand, UUID> {
    @Override
    public CompletableFuture<UUID> handle(CreateUserGateCommand createUserGateCommand) {
        var user = filter(
                        NimbleJQueryProvider.getAll(User.class),
                        u -> u.getEmailAddress().equals(createUserGateCommand.emailAddress()))
                .stream().findFirst();

        if (user.isEmpty()) {
            throw new IllegalArgumentException(BusinessErrors.UserGate.Create.UserDoesNotExist);
        }

        try {
            var userGate = UserGate.create(user.get().getId());
            NimbleJQueryProvider.insert(userGate);

            SendGridService.sendEmail(user.get().getEmailAddress(), "Login code for IDis", SendGridTemplates.userGateTemplate(user.get().getFirstName(), userGate.getCode()));

            return CompletableFuture.completedFuture(user.get().getId());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
