package com.idis.core.business.commands.user;

import com.nimblej.core.IRequest;

public record CreateUserGateCommand(String emailAddress) implements IRequest<String> { }
