package com.idis.core.business.statistics.posts.command;

import com.nimblej.core.IRequest;

import java.util.UUID;

public record ExportPostStatisticsAsPdfCommand(UUID postId) implements IRequest<byte[]> { }
