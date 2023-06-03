package com.idis.core.business.pdf.post.commands;

import com.nimblej.core.IRequest;

import java.util.UUID;

public record GetPdfForPostStatsCommand(UUID postId) implements IRequest<byte[]> { }
