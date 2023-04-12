package com.idis.core.business.commandhandlers.test;

import com.idis.core.business.commands.test.CreateTestObjCommand;
import com.idis.core.domain.test.TestObj;
import com.nimblej.core.IRequestHandler;
import com.nimblej.networking.database.NimbleJQueryProvider;

import java.util.concurrent.CompletableFuture;

import static com.nimblej.extensions.functional.FunctionalExtensions.filter;

public final class CreateTestObjCommandHandler implements IRequestHandler<CreateTestObjCommand, TestObj> {
    @Override
    public CompletableFuture<TestObj> handle(CreateTestObjCommand request) {
        System.out.println("name: " + request.name());
        System.out.println("age: " + request.age());
        System.out.println("dateOfBirth: " + request.dateOfBirth());

        var nameResult = request.name() == null || request.name().isBlank();
        if (nameResult) {
            return CompletableFuture.supplyAsync(() -> null);
        }

        var testObjs = NimbleJQueryProvider.getAll(TestObj.class);

        var duplicateResult = filter(testObjs, testObj -> testObj.getName().equals(request.name()));
        if (duplicateResult.size() > 0) {
            return CompletableFuture.supplyAsync(() -> null);
        }

        return CompletableFuture
                .supplyAsync(() -> {
                    var test = TestObj.create(request.name(), request.age(), request.dateOfBirth());

                    if (test == null) {
                        return null;
                    }

                    NimbleJQueryProvider.insert(test);

                    return test;
                });
    }
}
