package com.idis.core.business.statistics.posts.commands;

import com.idis.shared.infrastructure.IRequest;

import java.util.UUID;

public record ExportPostStatisticsAsDocbookCommand(UUID postId) implements IRequest<String> { }
