package com.idis.shared.infrastructure;

import java.util.concurrent.CompletableFuture;

/**
 * The {@code IRequestHandler} interface represents a handler for processing requests.
 * It defines the contract that all request handlers must adhere to.
 *
 * @param <TCommand> the type of the command that implements {@link com.idis.shared.infrastructure.IRequest
 * @param <TResult> the type of the result returned after processing the command
 */
public interface IRequestHandler<TCommand extends IRequest<TResult>, TResult> {
    /**
     * Handles the given command asynchronously and returns a {@link CompletableFuture}
     * representing the result of the processing.
     *
     * @param command the command to be processed
     * @return a {@code CompletableFuture} that represents the result of the processing
     */
    CompletableFuture<TResult> handle(TCommand command);
}
