package com.idis.core.business.statistics.posts.commands;

import com.idis.core.business.statistics.posts.commandresponses.CreatePostStatisticsCommandResponse;
import com.idis.shared.infrastructure.IRequest;

import java.util.UUID;

public record CreatePostStatisticsCommand(UUID postId) implements IRequest<CreatePostStatisticsCommandResponse> { }
