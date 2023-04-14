package com.idis.core.business.commands.user;

import com.nimblej.core.IRequest;

import java.util.UUID;

public record PassUserGateCommand(UUID userId, String code) implements IRequest<String> { }
