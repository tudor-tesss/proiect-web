package com.idis.core.business.commands.user;

import com.nimblej.core.IRequest;

import java.util.UUID;

public record CreateUserGateCommand(String emailAddress) implements IRequest<UUID> { }
