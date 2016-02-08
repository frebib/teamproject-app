package d2.teamproject.module.algorithm.search.datastructures;

/**
 * Provides accessor methods for a generic {@link d2.teamproject.module.algorithm.search.datastructures.DataStructure}
 *
 * @param <A> Object type to be stored in the {@code DataStructure}
 */
public interface DataStructure<A> {
    /**
     * Gets the first item in the {@code DataStructure}
     *
     * @return The first item
     */
    public A getHead();

    /**
     * Adds a new item into the {@code DataStructure}
     *
     * @param a Item to add
     * @return true if successful
     */
    public boolean add(A a);

    /**
     * Returns if the {@code DataStructure} is empty
     *
     * @return true if empty
     */
    public boolean isEmpty();

    /**
     * Returns if an item is within the {@code DataStructure}
     *
     * @param a Item to compare
     * @return true if in the {@code DataStructure}
     */
    public boolean contains(A a);
    
    /**
     * Returns a copy of the DataStructure
     * 
     * @return a copy
     */
    public DataStructure<A> copy();
}
