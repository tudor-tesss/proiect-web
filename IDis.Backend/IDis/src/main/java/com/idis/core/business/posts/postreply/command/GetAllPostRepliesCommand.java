package com.idis.core.business.posts.postreply.command;

import com.idis.core.domain.posts.postreply.PostReply;
import com.idis.shared.infrastructure.IRequest;

import java.util.List;
import java.util.UUID;

public record GetAllPostRepliesCommand(UUID postId) implements IRequest<List<PostReply>> { }
