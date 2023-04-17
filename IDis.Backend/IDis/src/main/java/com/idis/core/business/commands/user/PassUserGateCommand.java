package com.idis.core.business.commands.user;

import com.idis.core.business.commandresponses.user.PassUserGateCommandResponse;
import com.idis.core.domain.user.UserGate;
import com.nimblej.core.IRequest;

import java.util.UUID;

public record PassUserGateCommand(UUID userId, String code) implements IRequest<PassUserGateCommandResponse> { }
