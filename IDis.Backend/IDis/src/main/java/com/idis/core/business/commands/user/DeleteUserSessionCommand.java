package com.idis.core.business.commands.user;

import com.nimblej.core.IRequest;

import java.util.UUID;

public record DeleteUserSessionCommand(UUID sessionId) implements IRequest<String> { }
