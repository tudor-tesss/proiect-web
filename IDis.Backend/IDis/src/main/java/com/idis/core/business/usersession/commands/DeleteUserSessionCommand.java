package com.idis.core.business.usersession.commands;

import com.nimblej.core.IRequest;

import java.util.UUID;

public record DeleteUserSessionCommand(UUID sessionId) implements IRequest<String> { }
