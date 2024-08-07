package com.idis.core.business.usersession.commands;

import com.idis.core.business.usersession.commandresponses.PassUserGateCommandResponse;
import com.idis.shared.infrastructure.IRequest;

import java.util.UUID;

public record PassUserGateCommand(UUID userId, String code) implements IRequest<PassUserGateCommandResponse> { }
