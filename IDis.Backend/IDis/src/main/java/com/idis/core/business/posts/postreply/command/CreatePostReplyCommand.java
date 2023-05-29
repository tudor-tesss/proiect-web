package com.idis.core.business.posts.postreply.command;

import com.idis.core.domain.posts.postreply.PostReply;
import com.nimblej.core.IRequest;

import java.util.Map;
import java.util.UUID;

public record CreatePostReplyCommand(UUID authorId, UUID parentPostId, String title, String body, Map<String, Integer> ratings)  implements IRequest<PostReply> { }
