package com.idis.core.business.commands.user;

import com.idis.core.business.commandresponses.user.CheckUserSessionCommandResponse;
import com.nimblej.core.IRequest;

import java.util.UUID;

public record CheckUserSessionCommand(UUID userId, UUID sessionId, String ipAddress) implements IRequest<CheckUserSessionCommandResponse> { }
