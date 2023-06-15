package com.idis.shared.web.communication;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code Controller} annotation is used to mark methods that represent specific
 * functionality within the application.
 * <p>
 * By annotating a method with {@code Controller}, it becomes easier to identify and
 * organize application functionality, especially when working with routing in web
 * applications or command handling.
 * <p>
 * The {@code Controller} annotation includes a {@code name} parameter, which can be
 * used to give the method a more descriptive and human-readable name. This can be
 * helpful for documentation or debugging purposes.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Controller {
    /**
     * Returns the name of the function.
     *
     * @return The name of the function.
     */
    String name();
}
