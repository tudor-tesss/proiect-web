package com.idis.core.business.usersession.commands;

import com.idis.core.business.usersession.commandresponses.CreateUserSessionCommandResponse;
import com.nimblej.core.IRequest;

import java.util.UUID;

public record CreateUserSessionCommand(UUID userId, String userIpAddress) implements IRequest<CreateUserSessionCommandResponse> { }
