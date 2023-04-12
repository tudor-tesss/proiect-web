package com.idis.core.business.commands.user;

import com.idis.core.domain.user.User;
import com.nimblej.core.IRequest;

public record CreateUserCommand(String name, String firstName, String emailAddress) implements IRequest<User> { }
