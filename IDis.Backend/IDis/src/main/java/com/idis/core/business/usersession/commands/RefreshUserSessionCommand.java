package com.idis.core.business.usersession.commands;

import com.idis.core.business.usersession.commandresponses.RefreshUserSessionCommandResponse;
import com.idis.shared.infrastructure.IRequest;

import java.util.UUID;

public record RefreshUserSessionCommand(UUID userId, UUID sessionId, String ipAddress) implements IRequest<RefreshUserSessionCommandResponse> { }
