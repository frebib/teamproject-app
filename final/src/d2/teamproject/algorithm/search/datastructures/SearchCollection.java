package d2.teamproject.algorithm.search.datastructures;

import java.util.Collection;

/**
 * Provides accessor methods for a generic {@link SearchCollection}
 * @param <E> Object type to be stored in the {@code SearchCollection}
 */
public interface SearchCollection<E> extends Collection<E> {
    /**
     * Gets the first item in the {@code SearchCollection}
     * @return The first item
     */
    E getHead();

    /**
     * Gets the first item in the {@code SearchCollection} without removing it
     * @return The first item
     */
    E peek();

    /**
     * Adds a new item into the {@code SearchCollection}
     * @param a Item to add
     * @return true if successful
     */
    boolean add(E a);

    /**
     * Returns if the {@code SearchCollection} is empty
     * @return true if empty
     */
    boolean isEmpty();

    /**
     * Returns if an item is within the {@code SearchCollection}
     * @param a Item to compare
     * @return true if in the {@code SearchCollection}
     */
    boolean contains(Object a);

    /**
     * Clears all elements from the {@code DataStructure}
     */
    void clear();

    /**
     * Returns a copy of the SearchCollection
     * @return a copy
     */
    SearchCollection<E> copy();
}