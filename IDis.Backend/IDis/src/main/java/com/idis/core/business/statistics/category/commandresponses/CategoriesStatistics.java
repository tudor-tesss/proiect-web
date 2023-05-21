package com.idis.core.business.statistics.category.commandresponses;

import java.util.Map;

public record CategoriesStatistics(Map<String, Integer> postCountByCategory) { }
