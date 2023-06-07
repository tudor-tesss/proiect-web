package com.idis.core.business.statistics.posts.commandresponses;

import java.util.List;

public record CreatePostsStatisticsCommandResponse(List<PostStatistics> statistics) { }
