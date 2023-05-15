package com.idis.presentation;

import com.idis.core.business.commandhandlers.category.CreateCategoryCommandHandler;
import com.idis.core.business.commandhandlers.user.*;
import com.idis.core.business.commands.category.CreateCategoryCommand;
import com.idis.core.business.commands.user.*;
import com.idis.core.domain.category.Category;
import com.idis.core.domain.user.*;
import com.idis.presentation.functions.*;
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
                        new GenericOptionsController(),
                        new UserFunctions(),
                        new CategoryFunctions()
                );

        server.start();
    }

    private static void addManagedClasses() {
        NimbleJQueryProvider.addManagedClass(User.class);
        NimbleJQueryProvider.addManagedClass(UserGate.class);
        NimbleJQueryProvider.addManagedClass(UserSession.class);
        NimbleJQueryProvider.addManagedClass(Category.class);
    }

    private static void addMediatorHandlers() {
        var mediator = Mediator.getInstance();

        mediator.registerHandler(CreateUserCommand.class, new CreateUserCommandHandler());
        mediator.registerHandler(CreateUserGateCommand.class, new CreateUserGateCommandHandler());

        mediator.registerHandler(PassUserGateCommand.class, new PassUserGateCommandHandler());

        mediator.registerHandler(CreateUserSessionCommand.class, new CreateUserSessionCommandHandler());
        mediator.registerHandler(CheckUserSessionCommand.class, new CheckUserSessionCommandHandler());
        mediator.registerHandler(DeleteUserSessionCommand.class, new DeleteUserSessionCommandHandler());

        mediator.registerHandler(CreateCategoryCommand.class, new CreateCategoryCommandHandler());
    }
}
