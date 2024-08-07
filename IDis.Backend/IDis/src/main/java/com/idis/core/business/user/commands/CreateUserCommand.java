package com.idis.core.business.user.commands;

import com.idis.core.domain.user.User;
import com.idis.shared.infrastructure.IRequest;

public record CreateUserCommand(String name, String firstName, String emailAddress) implements IRequest<User> { }
