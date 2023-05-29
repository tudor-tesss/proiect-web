package com.idis.core.business.posts.parentpost.command;

import com.idis.core.domain.posts.parentpost.Post;
import com.nimblej.core.IRequest;

import java.util.List;
import java.util.UUID;

public record GetAllPostsInsideOfACategoryCommand (UUID categoryId) implements IRequest<List<Post>> { }
