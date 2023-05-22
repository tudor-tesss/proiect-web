package com.idis.core.business.posts.parentpost.command;

import com.idis.core.domain.DomainErrors;
import com.idis.core.domain.posts.parentpost.Post;
import com.nimblej.core.IRequest;

import java.util.Map;
import java.util.UUID;

public record CreatePostCommand(UUID authorId, UUID categoryId, String title, String body, Map<String, Integer> ratings) implements IRequest<Post> { }
