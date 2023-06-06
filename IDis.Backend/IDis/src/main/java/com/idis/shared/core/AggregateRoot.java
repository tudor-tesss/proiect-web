package com.idis.shared.core;

import java.util.UUID;

/**
 * AggregateRoot is an abstract class that serves as a foundation for all
 * other classes that represent objects/entities in the application.
 * <p>
 * By extending AggregateRoot, classes automatically get a UUID-based
 * identifier, which is generated when an instance is created.
 * This unique identifier is useful for managing objects in the
 * application, such as when storing or retrieving them from a database.
 */
public abstract class AggregateRoot {
    protected UUID id;

    /**
     * Constructs a new AggregateRoot instance with a randomly generated UUID.
     */
    public AggregateRoot() {
        this.id = UUID.randomUUID();
    }

    /**
     * Returns the UUID of this AggregateRoot.
     *
     * @return The UUID associated with this AggregateRoot.
     */
    public UUID getId() {
        return id;
    }
}
