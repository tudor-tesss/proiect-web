package com.idis.core.business.commands.user;

import com.idis.core.business.commandresponses.user.CreateUserSessionCommandResponse;
import com.nimblej.core.IRequest;

import java.util.UUID;

public record CreateUserSessionCommand(UUID userId, String userIpAddress) implements IRequest<CreateUserSessionCommandResponse> { }
