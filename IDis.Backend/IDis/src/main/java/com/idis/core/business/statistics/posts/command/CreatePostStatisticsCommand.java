package com.idis.core.business.statistics.posts.command;

import com.idis.core.business.statistics.posts.commandresponses.CreatePostStatisticsCommandResponse;
import com.nimblej.core.IRequest;

import java.util.UUID;

public record CreatePostStatisticsCommand(UUID postId) implements IRequest<CreatePostStatisticsCommandResponse> { }
