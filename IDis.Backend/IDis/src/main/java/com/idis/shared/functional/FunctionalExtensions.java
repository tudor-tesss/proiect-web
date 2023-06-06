package com.idis.shared.functional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The {@code FunctionalExtensions} class provides a set of utility methods for working
 * with {@link List} instances and performing common functional programming
 * operations such as filtering, applying functions, and testing predicates on lists.
 * <p>
 * The class supports various common functional patterns such as testing whether any,
 * all, or none of the elements in a list satisfy a given predicate, and checking if
 * a list contains a specific element or an element satisfying a predicate.
 * <p>
 * Additionally, the class provides methods for transforming a list by applying a function
 * to each element, as well as filtering a list by including only the elements that
 * satisfy a given predicate.
 * <p>
 * Example usage:
 * <pre>
 * List&lt;Integer&gt; numbers = List.of(1, 2, 3, 4, 5);
 * Predicate&lt;Integer&gt; isEven = num -> num % 2 == 0;
 * Function&lt;Integer, Integer&gt; doubleValue = num -> num * 2;
 *
 * boolean anyEven = FunctionalExtensions.any(numbers, isEven);
 * boolean allEven = FunctionalExtensions.all(numbers, isEven);
 * boolean noneEven = FunctionalExtensions.none(numbers, isEven);
 * boolean containsTwo = FunctionalExtensions.contains(numbers, 2);
 * boolean containsEven = FunctionalExtensions.contains(numbers, isEven);
 * List&lt;Integer&gt; doubledNumbers = FunctionalExtensions.apply(numbers, doubleValue);
 * List&lt;Integer&gt; evenNumbers = FunctionalExtensions.filter(numbers, isEven);
 * </pre>
 */
public class FunctionalExtensions {
    /**
     * Determines if any element in the given list satisfies the provided predicate.
     *
     * @param list      The list of elements to test.
     * @param predicate The predicate to evaluate for each element.
     * @param <T>       The type of elements in the list.
     * @return {@code true} if at least one element in the list satisfies the predicate,
     * {@code false} otherwise.
     */
    public static <T> boolean any(List<T> list, Predicate<T> predicate) {
        for (T item : list) {
            if (predicate.test(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if all elements in the given list satisfy the provided predicate.
     *
     * @param list      The list of elements to test.
     * @param predicate The predicate to evaluate for each element.
     * @param <T>       The type of elements in the list.
     * @return {@code true} if all elements in the list satisfy the predicate,
     * {@code false} otherwise.
     */
    public static <T> boolean all(List<T> list, Predicate<T> predicate) {
        for (T item : list) {
            if (!predicate.test(item)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines if none of the elements in the given list satisfy the provided predicate.
     *
     * @param list      The list of elements to test.
     * @param predicate The predicate to evaluate for each element.
     * @param <T>       The type of elements in the list.
     * @return {@code true} if no elements in the list satisfy the predicate,
     * {@code false} otherwise.
     */
    public static <T> boolean none(List<T> list, Predicate<T> predicate) {
        for (T item : list) {
            if (predicate.test(item)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines if the given list contains the specified item.
     *
     * @param list The list of elements to search.
     * @param item The item to look for in the list.
     * @param <T>  The type of elements in the list.
     * @return {@code true} if the list contains the item, {@code false} otherwise.
     */
    public static <T> boolean contains(List<T> list, T item) {
        return list.contains(item);
    }

    /**
     * Determines if the given list contains an element that satisfies the provided predicate.
     *
     * @param list      The list of elements to search.
     * @param predicate The predicate to evaluate for each element.
     * @param <T>       The type of elements in the list.
     * @return {@code true} if the list contains an element that satisfies the predicate,
     * {@code false} otherwise.
     */
    public static <T> boolean contains(List<T> list, Predicate<T> predicate) {
        for (T item : list) {
            if (predicate.test(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Applies the provided function to each element in the given list and returns a new
     * list with the transformed elements.
     *
     * @param list     The list of elements to transform.
     * @param function The function to apply to each element.
     * @param <T>      The type of elements in the list.
     * @return A new list containing the transformed elements.
     */
    public static <T> List<T> apply(List<T> list, Function<T, T> function) {
        var result = new ArrayList<T>();

        for (T item : list) {
            function.apply(item);
            result.add(item);
        }

        return result;
    }

    /**
     * Applies the provided {@code Consumer} to each element in the given list and
     * returns a new list with the modified elements. An overload for apply() was not possible
     * without forcing a cast on the function parameter due to Java's type erasure.
     *
     * @param list         The list of elements to modify.
     * @param consumer The {@code Consumer} to apply to each element.
     * @param <T>          The type of elements in the list.
     * @return A new list containing the modified elements.
     */
    public static <T> List<T> modify(List<T> list, Consumer<T> consumer) {
        var result = new ArrayList<T>();

        for (T item : list) {
            consumer.accept(item);
            result.add(item);
        }

        return result;
    }

    /**
     * Filters the given list based on the provided predicate and returns a new list
     * containing only the elements that satisfy the predicate.
     *
     * @param list      The list of elements to filter.
     * @param predicate The predicate to evaluate for each element.
     * @param <T>       The type of elements in the list.
     * @return A new list containing only the elements that satisfy the predicate.
     */
    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        var result = new ArrayList<T>();

        for (T item : list) {
            if (predicate.test(item)) {
                result.add(item);
            }
        }

        return result;
    }
}
