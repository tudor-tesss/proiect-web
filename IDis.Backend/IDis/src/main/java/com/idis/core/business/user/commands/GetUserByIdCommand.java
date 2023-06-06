package com.idis.core.business.user.commands;

import com.idis.core.domain.user.User;
import com.idis.shared.infrastructure.IRequest;

import java.util.UUID;

public record GetUserByIdCommand(UUID userId) implements IRequest<User> { }
