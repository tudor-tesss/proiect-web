package com.idis.core.business.statistics.category.commandresponses;

import java.util.List;

public record CreateCategoriesStatisticsCommandResponse(
        List<CategoryStatistics> statistics
) { }