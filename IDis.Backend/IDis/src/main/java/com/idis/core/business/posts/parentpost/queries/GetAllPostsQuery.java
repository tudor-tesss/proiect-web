package com.idis.core.business.posts.parentpost.queries;

import com.idis.core.domain.posts.parentpost.Post;
import com.idis.shared.infrastructure.IRequest;

import java.util.List;

public record GetAllPostsQuery() implements IRequest<List<Post>> { }
