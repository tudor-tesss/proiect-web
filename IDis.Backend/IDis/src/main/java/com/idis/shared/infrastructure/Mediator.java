package com.idis.shared.infrastructure;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * The {@code Mediator} class is a centralized communication hub for sending requests
 * and dispatching them to their respective handlers. It follows the Singleton pattern
 * to ensure that only one instance of the mediator exists in the application.
 * <p>
 * This class maintains a map of command classes to their corresponding handlers,
 * allowing for a decoupled architecture between the sender and receiver of a command.
 * It provides methods to register handlers and send commands to be processed.
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * Mediator mediator = Mediator.getInstance();
 * mediator.registerHandler(CreateUserCommand.class, new CreateUserCommandHandler());
 * CompletableFuture<User> user = mediator.send(new CreateUserCommand("John Doe", "john.doe@example.com"));
 * }
 * </pre>
 */
public class Mediator {
    private static Mediator instance;
    private final Map<Class<? extends IRequest>, IRequestHandler> handlers = new HashMap<>();

    /**
     * Private constructor to ensure that the Mediator class cannot be instantiated
     * directly, enforcing the Singleton pattern.
     */
    private Mediator() {
    }

    /**
     * Retrieves the singleton instance of the {@code Mediator} class.
     *
     * @return the singleton instance of the {@code Mediator}
     */
    public static Mediator getInstance() {
        if (instance == null) {
            instance = new Mediator();
        }
        return instance;
    }

    /**
     * Registers a handler for a specific command class. This allows the mediator
     * to dispatch commands to the appropriate handler when the {@code send} method
     * is called.
     *
     * @param commandClass the class of the command that the handler processes
     * @param handler      the handler that processes the command
     * @param <TCommand>   the type of the command that implements {@link IRequest}
     * @param <TResult>    the type of the result returned after processing the command
     */
    public <TCommand extends IRequest<TResult>, TResult> void registerHandler(Class<TCommand> commandClass, IRequestHandler<TCommand, TResult> handler) {
        handlers.put(commandClass, handler);
    }

    /**
     * Sends a command to be processed by the registered handler for the command's class.
     * This method looks up the handler in the internal map and calls its {@code handle}
     * method, returning a {@link CompletableFuture} representing the result of the processing.
     *
     * @param command    the command to be processed
     * @param <TCommand> the type of the command that implements {@link IRequest}
     * @param <TResult>  the type of the result returned after processing the command
     * @return a {@code CompletableFuture} that represents the result of the processing
     * @throws IllegalStateException if no handler is registered for the command's class
     */
    public <TCommand extends IRequest<TResult>, TResult> CompletableFuture<TResult> send(TCommand command) {
        IRequestHandler<TCommand, TResult> handler = handlers.get(command.getClass());

        if (handler == null) {
            throw new IllegalStateException("No handler registered for command " + command.getClass());
        }

        try {
            return handler.handle(command);
        } catch (Exception e) {
            throw e;
        }
    }
}

