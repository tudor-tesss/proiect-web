package com.idis.core.business.user.queryhandlers;

import com.idis.core.business.user.queries.GetAllUserNamesQuery;
import com.idis.core.domain.user.User;
import com.idis.shared.database.QueryProvider;
import com.idis.shared.infrastructure.IRequestHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class GetAllUserNamesQueryHandler implements IRequestHandler<GetAllUserNamesQuery, Map<UUID, String>> {
    @Override
    public CompletableFuture<Map<UUID, String>> handle(GetAllUserNamesQuery request) {
        return QueryProvider.getAllAsync(User.class).thenApply(l -> {
            Map<UUID, String> map = new HashMap<>();
            for(var u : l) {
                map.put(u.getId(), u.getFirstName().concat(" ").concat(u.getName()));
            }
            return map;
        });
    }
}
