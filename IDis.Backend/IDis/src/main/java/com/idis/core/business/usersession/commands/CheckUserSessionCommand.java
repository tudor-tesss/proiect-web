package com.idis.core.business.usersession.commands;

import com.idis.core.business.usersession.commandresponses.CheckUserSessionCommandResponse;
import com.nimblej.core.IRequest;

import java.util.UUID;

public record CheckUserSessionCommand(UUID userId, UUID sessionId, String ipAddress) implements IRequest<CheckUserSessionCommandResponse> { }
