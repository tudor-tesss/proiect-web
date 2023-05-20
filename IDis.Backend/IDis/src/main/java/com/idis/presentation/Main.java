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

import javax.print.Doc;
import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws IOException {
        var maxRetries = 5;
        addManagedClasses();
        addMediatorHandlers();

        var url = DeployedDetails.url;
        var username = DeployedDetails.username;
        var password = DeployedDetails.password;

        int catchCount = 0;
        for (int i = 0; i < maxRetries; i++) {
            try {
                NimbleJQueryProvider.initiate(url, username, password);
                catchCount = 0;

                break;
            } catch (SQLException e) {
                System.out.println("Failed to connect to deployed database. Retrying...");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                catchCount += 2;
            }
        }

        if (catchCount > maxRetries) {
            catchCount = 0;
            url = DockerDetails.url;
            username = DockerDetails.username;
            password = DockerDetails.password;

            for (int i = 0; i < maxRetries; i++) {
                try {
                    NimbleJQueryProvider.initiate(url, username, password);
                    catchCount = 0;

                    break;
                } catch (SQLException e) {
                    System.out.println("Failed to connect to docker database. Retrying...");

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                    catchCount += 2;
                }
            }
        }

        if (catchCount > maxRetries) {
            url = LocalDetails.url;
            username = LocalDetails.username;
            password = LocalDetails.password;

            for (int i = 0; i < maxRetries; i++) {
                try {
                    NimbleJQueryProvider.initiate(url, username, password);
                    catchCount = 0;

                    break;
                } catch (SQLException e) {
                    System.out.println("Failed to connect to local database. Retrying...");

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        if (catchCount > maxRetries) {
            System.out.println("Failed to connect to database. Exiting...");
            System.exit(1);
        }

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

    private static class DeployedDetails {
        private static final String url = "jdbc:postgresql://primary.idis-db--ylsyc29qft6l.addon.code.run:5432/IDisDev?user=_25d8fefac5e75b16&password=_80da9f9ce932c87ecbb8b11793110f&sslmode=require";
        private static final String username = "_25d8fefac5e75b16";
        private static final String password = "_80da9f9ce932c87ecbb8b11793110f";
    }

    private static class DockerDetails {
        private static final String url = "jdbc:postgresql://db:5432/IDisDev";
        private static final String username = "postgres";
        private static final String password = "Pass4Postgres1!";
    }

    private static class LocalDetails {
        private static final String url = "jdbc:postgresql://localhost:5432/IDisDev";
        private static final String username = "postgres";
        private static final String password = "Pass4Postgres1!";
    }
}
