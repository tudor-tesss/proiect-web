package com.idis.core.business.statistics.posts.commands;

import com.idis.shared.infrastructure.IRequest;

import java.util.UUID;

public record ExportPostStatisticsAsCsvCommand(UUID postId) implements IRequest<String> { }
