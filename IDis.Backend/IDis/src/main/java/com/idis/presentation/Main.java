package com.idis.presentation;

import com.idis.core.business.commandhandlers.test.CreateTestObjCommandHandler;
import com.idis.core.business.commands.test.CreateTestObjCommand;
import com.idis.core.domain.test.TestObj;
import com.idis.presentation.functions.test.TestController;
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
                        new TestController()
                );

        server.start();
    }

    private static void addManagedClasses() {
        NimbleJQueryProvider.addManagedClass(TestObj.class);
    }

    private static void addMediatorHandlers() {
        var mediator = Mediator.getInstance();

        mediator.registerHandler(CreateTestObjCommand.class, new CreateTestObjCommandHandler());
    }
}
