package com.idis.presentation;

import com.idis.core.business.commandhandlers.user.CreateUserCommandHandler;
import com.idis.core.business.commands.user.CreateUserCommand;
import com.idis.core.domain.user.User;
import com.idis.presentation.functions.UserFunctions;
import com.nimblej.core.Mediator;
import com.nimblej.networking.database.NimbleJQueryProvider;
import com.nimblej.networking.http.server.HttpServer;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {
        addManagedClasses();
        addMediatorHandlers();

        NimbleJQueryProvider.initiate("jdbc:postgresql://localhost:5432/IDisDev", "postgres", "Pass4Postgres1!");
        var server = HttpServer
                .create(7101)
                .withControllers(
                        new UserFunctions()
                );

        server.start();
    }

    private static void addManagedClasses() {
        NimbleJQueryProvider.addManagedClass(User.class);
    }

    private static void addMediatorHandlers() {
        var mediator = Mediator.getInstance();

        mediator.registerHandler(CreateUserCommand.class, new CreateUserCommandHandler());
    }
}
