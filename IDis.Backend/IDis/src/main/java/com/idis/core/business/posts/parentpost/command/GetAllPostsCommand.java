package com.idis.core.business.posts.parentpost.command;

import com.idis.core.domain.posts.parentpost.Post;
import com.nimblej.core.IRequest;

import java.util.List;

public record GetAllPostsCommand() implements IRequest<List<Post>> { }
