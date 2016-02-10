package d2.teamproject.module.algorithm.search.datastructures;

/**
 * Provides accessor methods for a generic {@link BaseDataStructure}
 * @param <E> Object type to be stored in the {@code BaseDataStructure}
 */
public interface BaseDataStructure<E> {
    /**
     * Gets the first item in the {@code BaseDataStructure}
     * @return The first item
     */
    E getHead();

    /**
     * Adds a new item into the {@code BaseDataStructure}
     * @param a Item to add
     * @return true if successful
     */
    boolean add(E a);

    /**
     * Returns if the {@code BaseDataStructure} is empty
     * @return true if empty
     */
    boolean isEmpty();

    /**
     * Returns if an item is within the {@code BaseDataStructure}
     * @param a Item to compare
     * @return true if in the {@code BaseDataStructure}
     */
    boolean contains(E a);

    /**
     * Returns a copy of the BaseDataStructure
     * @return a copy
     */
    BaseDataStructure<E> copy();
}