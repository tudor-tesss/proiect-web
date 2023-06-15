package com.idis.core.business.user.queryhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.user.queries.GetUserByIdQuery;
import com.idis.core.domain.user.User;
import com.idis.shared.database.QueryProvider;
import com.idis.shared.infrastructure.IRequestHandler;

import java.util.concurrent.CompletableFuture;

public final class GetUserByIdQueryHandler implements IRequestHandler<GetUserByIdQuery, User> {
    @Override
    public CompletableFuture<User> handle(GetUserByIdQuery getUserByIdCommand) {
        return QueryProvider.getByIdAsync(User.class, getUserByIdCommand.userId()).thenApply(user -> {
            if(user.isEmpty()) {
                throw new IllegalArgumentException(BusinessErrors.User.UserDoesNotExist);
            }

            return user.get();
        });
    }
}
