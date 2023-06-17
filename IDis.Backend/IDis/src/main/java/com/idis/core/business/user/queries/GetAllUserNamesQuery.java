package com.idis.core.business.user.queries;

import com.idis.shared.infrastructure.IRequest;

import java.util.Map;
import java.util.UUID;

public record GetAllUserNamesQuery() implements IRequest<Map<UUID, String>> { }
