package com.idis.core.business.user.queries;

import com.idis.core.domain.user.User;
import com.idis.shared.infrastructure.IRequest;

import java.util.UUID;

public record GetUserByIdQuery(UUID userId) implements IRequest<User> { }
