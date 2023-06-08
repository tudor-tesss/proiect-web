package com.idis.presentation;

import com.idis.core.business.statistics.category.commandhandlers.*;
import com.idis.core.business.statistics.posts.commandhandlers.*;
import com.idis.core.business.statistics.category.commandhandlers.ExportCategoryStatisticsAsPdfCommandHandler;
import com.idis.core.business.statistics.category.commands.ExportCategoryStatisticsAsPdfCommand;
import com.idis.core.business.statistics.posts.commandhandlers.ExportPostStatisticsAsPdfCommandHandler;
import com.idis.core.business.statistics.posts.commands.ExportPostStatisticsAsPdfCommand;
import com.idis.core.business.posts.parentpost.command.*;
import com.idis.core.business.posts.parentpost.commandhandlers.*;
import com.idis.core.business.posts.postreply.commandhandlers.*;
import com.idis.core.business.statistics.category.commands.*;
import com.idis.core.business.usersession.commandhandlers.*;
import com.idis.core.business.statistics.posts.commands.*;
import com.idis.core.business.category.commandhandlers.*;
import com.idis.core.business.posts.parentpost.command.*;
import com.idis.core.business.posts.postreply.command.*;
import com.idis.core.business.rssfeed.commandhandlers.*;
import com.idis.core.business.usersession.commands.*;
import com.idis.core.business.user.commandhandlers.*;
import com.idis.core.business.category.commands.*;
import com.idis.core.business.rssfeed.commands.*;
import com.idis.core.domain.posts.parentpost.*;
import com.idis.core.domain.posts.postreply.*;
import com.idis.core.business.user.commands.*;
import com.idis.core.domain.usersession.*;
import com.idis.presentation.functions.*;
import com.idis.core.domain.category.*;
import com.idis.core.domain.user.*;

import com.idis.shared.infrastructure.Mediator;
import com.idis.shared.database.QueryProvider;
import com.idis.shared.web.server.HttpServer;

import java.sql.SQLException;
import java.io.IOException;

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
                QueryProvider.initiate(url, username, password);
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
                    QueryProvider.initiate(url, username, password);
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
                    QueryProvider.initiate(url, username, password);
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
                        new StatisticsFunctions(),
                        new RssFeedFunctions()
                );

        server.start();
    }

    private static void addManagedClasses() {
        QueryProvider.addManagedClass(User.class);
        QueryProvider.addManagedClass(UserGate.class);
        QueryProvider.addManagedClass(UserSession.class);
        QueryProvider.addManagedClass(Category.class);
        QueryProvider.addManagedClass(Post.class);
        QueryProvider.addManagedClass(PostReply.class);
    }

    private static void addMediatorHandlers() {
        var mediator = Mediator.getInstance();

        mediator.registerHandler(CreateUserCommand.class, new CreateUserCommandHandler());
        mediator.registerHandler(CreateUserGateCommand.class, new CreateUserGateCommandHandler());
        mediator.registerHandler(PassUserGateCommand.class, new PassUserGateCommandHandler());

        mediator.registerHandler(CreateUserSessionCommand.class, new CreateUserSessionCommandHandler());
        mediator.registerHandler(CheckUserSessionCommand.class, new CheckUserSessionCommandHandler());
        mediator.registerHandler(DeleteUserSessionCommand.class, new DeleteUserSessionCommandHandler());
        mediator.registerHandler(GetUserByIdCommand.class,new GetUserByIdCommandHandler());

        mediator.registerHandler(CreateCategoryCommand.class, new CreateCategoryCommandHandler());
        mediator.registerHandler(GetAllCategoriesCommand.class, new GetAllCategoriesCommandHandler());
        mediator.registerHandler(GetCategoriesByCreatorIdCommand.class, new GetCategoriesByCreatorIdCommandHandler());

        mediator.registerHandler(CreatePostCommand.class, new CreatePostCommandHandler());
        mediator.registerHandler(GetAllPostsInsideOfACategoryCommand.class, new GetAllPostsInsideOfACategoryCommandHandler());
        mediator.registerHandler(GetPostByIdCommand.class, new GetPostByIdCommandHandler());
        mediator.registerHandler(GetAllPostRepliesCommand.class, new GetAllPostRepliesCommandHandler());
        mediator.registerHandler(GetPostsByCreatorIdCommand.class, new GetPostsByCreatorIdCommandHandler());
        mediator.registerHandler(CreatePostReplyCommand.class, new CreatePostReplyCommandHandler());
        mediator.registerHandler(GetAllPostsCommand.class, new GetAllPostsCommandHandler());

        mediator.registerHandler(CreateCategoryStatisticsCommand.class, new CreateCategoryStatisticsCommandHandler());
        mediator.registerHandler(CreateCategoriesStatisticsCommand.class, new CreateCategoriesStatisticsCommandHandler());
        mediator.registerHandler(CreatePostStatisticsCommand.class, new CreatePostStatisticsCommandHandler());
        mediator.registerHandler(CreatePostsStatisticsCommand.class, new CreatePostsStatisticsCommandHandler());

        mediator.registerHandler(GetRssFeedCommand.class, new GetRssFeedCommandHandler());

        mediator.registerHandler(ExportPostStatisticsAsPdfCommand.class, new ExportPostStatisticsAsPdfCommandHandler());
        mediator.registerHandler(ExportCategoryStatisticsAsPdfCommand.class, new ExportCategoryStatisticsAsPdfCommandHandler());

        mediator.registerHandler(ExportPostStatisticsAsDocbookCommand.class, new ExportPostStatisticsAsDocbookCommandHandler());
        mediator.registerHandler(ExportCategoryStatisticsAsDocbookCommand.class, new ExportCategoryStatisticsAsDocbookCommandHandler());
        mediator.registerHandler(ExportCategoryStatisticsAsCsvCommand.class, new ExportCategoryStatisticsAsCsvCommandHandler());
        mediator.registerHandler(ExportPostStatisticsAsCsvCommand.class, new ExportPostStatisticsAsCsvCommandHandler());
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
