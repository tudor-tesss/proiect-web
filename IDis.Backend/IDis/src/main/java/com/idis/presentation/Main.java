package com.idis.presentation;

import com.idis.core.business.category.commandhandlers.CreateCategoryCommandHandler;
import com.idis.core.business.category.commands.CreateCategoryCommand;
import com.idis.core.business.posts.parentpost.command.CreatePostCommand;
import com.idis.core.business.posts.parentpost.commandhandlers.CreatePostCommandHandler;
import com.idis.core.business.statistics.category.commandhandlers.CreateCategoriesStatisticsCommandHandler;
import com.idis.core.business.statistics.category.commandhandlers.CreateCategoryStatisticsCommandHandler;
import com.idis.core.business.statistics.category.commands.CreateCategoriesStatisticsCommand;
import com.idis.core.business.statistics.category.commands.CreateCategoryStatisticsCommand;
import com.idis.core.business.user.commandhandlers.CreateUserCommandHandler;
import com.idis.core.business.user.commands.CreateUserCommand;
import com.idis.core.business.usersession.commandhandlers.*;
import com.idis.core.business.usersession.commands.*;
import com.idis.core.domain.category.Category;
import com.idis.core.domain.posts.parentpost.Post;
import com.idis.core.domain.user.*;
import com.idis.core.domain.usersession.UserGate;
import com.idis.core.domain.usersession.UserSession;
import com.idis.presentation.functions.*;
import com.nimblej.core.Mediator;
import com.nimblej.networking.database.NimbleJQueryProvider;
import com.nimblej.networking.http.server.HttpServer;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Starting server...\n");

        var maxRetries = 2;
        var delay = 100;

        addManagedClasses();
        addMediatorHandlers();

        var url = DeployedDetails.url;
        var username = DeployedDetails.username;
        var password = DeployedDetails.password;

        int catchCount = 0;
        for (int i = 0; i < maxRetries; i++) {
            try {
                NimbleJQueryProvider.initiate(url, username, password);
                System.out.println("Connected to deployed database.\n");
                catchCount = 0;

                break;
            } catch (SQLException e) {
                System.out.println("Failed to connect to deployed database. Retrying... in " + delay + "ms");

                try {
                    Thread.sleep(delay);
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
                    System.out.println("Connected to docker database.\n");
                    catchCount = 0;

                    break;
                } catch (SQLException e) {
                    System.out.println("Failed to connect to docker database. Retrying... in " + delay + "ms");

                    try {
                        Thread.sleep(delay);
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
                    System.out.println("Connected to local database.\n");
                    catchCount = 0;

                    break;
                } catch (SQLException e) {
                    System.out.println("Failed to connect to local database. Retrying... in " + delay + "ms");

                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        if (catchCount > maxRetries) {
            System.out.println("Failed to connect to database. Exiting...\n");
            System.exit(1);
        }

        var server = HttpServer
                .create(7101)
                .withControllers(
                        new GenericOptionsController(),
                        new UserFunctions(),
                        new CategoryFunctions(),
                        new PostFunctions(),
                        new StatisticsFunctions()
                );

        server.start();
    }

    private static void addManagedClasses() {
        NimbleJQueryProvider.addManagedClass(User.class);
        NimbleJQueryProvider.addManagedClass(UserGate.class);
        NimbleJQueryProvider.addManagedClass(UserSession.class);
        NimbleJQueryProvider.addManagedClass(Category.class);
        NimbleJQueryProvider.addManagedClass(Post.class);
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

        mediator.registerHandler(CreatePostCommand.class, new CreatePostCommandHandler());

        mediator.registerHandler(CreateCategoryStatisticsCommand.class, new CreateCategoryStatisticsCommandHandler());
        mediator.registerHandler(CreateCategoriesStatisticsCommand.class, new CreateCategoriesStatisticsCommandHandler());
    }

    private static class DeployedDetails {
        private static final String url = "jdbc:postgresql://primary.idis-db--ylsyc29qft6l.addon.code.run:5432/IDisDev?user=_25d8fefac5e75b16&password=_80da9f9ce932c87ecbb8b11793110f&sslmode=require";
        private static final String username = "_25d8fefac5e75b16";
        private static final String password = "_80da9f9ce932c87ecbb8b11793110f";
    }

    private static class DockerDetails {
        private static final String url = "jdbc:postgresql://postgresidis:5432/IDisDev";
        private static final String username = "postgres";
        private static final String password = "Pass4Postgres1!";
    }

    private static class LocalDetails {
        private static final String url = "jdbc:postgresql://localhost:5432/IDisDev";
        private static final String username = "postgres";
        private static final String password = "Pass4Postgres1!";
    }
}
