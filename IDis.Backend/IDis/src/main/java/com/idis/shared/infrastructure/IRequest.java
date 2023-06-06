package com.idis.shared.infrastructure;

/**
 * The {@code BaseCommand} interface represents a base command that serves as a marker interface
 * for defining command objects in the application.
 * <p>
 * Command objects are typically used to encapsulate the data and behavior required
 * to perform a specific action within the application.
 * <p>
 * The interface is generic, allowing the definition of a command object that returns a
 * specific type of response object. This can be useful for type safety and expressing
 * the intent of the command.
 *
 * @param <TResponse> The type of response object that the command should return.
 */
public interface IRequest<TResponse> { }
