package com.idis.core.business.posts.parentpost.command;

import com.idis.core.domain.posts.parentpost.Post;
import com.idis.shared.infrastructure.IRequest;

import java.util.List;
import java.util.UUID;

public record GetPostsByCreatorIdCommand(UUID creatorId) implements IRequest<List<Post>> { }
