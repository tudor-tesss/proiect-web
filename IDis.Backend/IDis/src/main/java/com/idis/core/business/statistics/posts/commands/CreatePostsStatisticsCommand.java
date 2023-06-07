package com.idis.core.business.statistics.posts.commands;

import com.idis.core.business.statistics.posts.commandresponses.CreatePostsStatisticsCommandResponse;
import com.idis.shared.infrastructure.IRequest;

public record CreatePostsStatisticsCommand() implements IRequest<CreatePostsStatisticsCommandResponse> { }
