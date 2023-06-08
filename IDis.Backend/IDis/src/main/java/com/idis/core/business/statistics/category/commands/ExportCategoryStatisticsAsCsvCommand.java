package com.idis.core.business.statistics.category.commands;

import com.idis.shared.infrastructure.IRequest;

import java.util.UUID;

public record ExportCategoryStatisticsAsCsvCommand(UUID categoryId) implements IRequest<String> { }
