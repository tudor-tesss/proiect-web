package com.idis.core.business.usersession.commands;

import com.idis.shared.infrastructure.IRequest;

import java.util.UUID;

public record CreateUserGateCommand(String emailAddress) implements IRequest<UUID> { }
