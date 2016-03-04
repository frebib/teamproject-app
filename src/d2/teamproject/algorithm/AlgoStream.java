package d2.teamproject.algorithm;

import java.util.List;

/**
 * Provides a common set of functions to generate a stream of states
 * to represent a given algorithm.
 * An algorithm can either generate states as requested or all at once when required by {@code initialise()}
 * @param <T> type of state that the algorithm generates.
 */
public interface AlgoStream<T> {

    /**
     * Prepares the algorithm for generation of states
     * Optionally can pre-generate the whole stream; is
     * dependant on the implementation of the algorithm
     * @return the {@code AlgoStream} object
     */
    AlgoStream<T> initialise();

    /**
     * Gets the next state in the stream sequence
     * @return the next state or null if there is no next state determined by {@code hasNext()}
     */
    T getNext();

    /**
     * Gets the previous state in the stream sequence
     * @return the previous state or null if there is no previous state determined by {@code hasPrevious()}
     */
    T getPrevious();

    /**
     * Optionally can be used if supported by the stream implementation
     * Gets a state at the {@code n}'th location in the stream
     * @param n index to get state at
     * @return the state at index {@code n}
     * @throws UnsupportedOperationException
     */
    T getNth(int n) throws UnsupportedOperationException;

    /**
     * Gets whether the stream contains a next state
     * @return true if there is another state
     */
    boolean hasNext();

    /**
     * Gets whether the stream contains a previous state
     * @return true if there is preceding state
     */
    boolean hasPrevious();

    /**
     * Optionally can be used if supported by the stream implementation
     * Gets whether the stream has a state at index {@code n}
     * @param n the index to find a state at
     * @return false if not supported by the implementation otherwise true if a state else false
     */
    boolean hasNth(int n);

    /**
     * Gets the entire list of states
     * Operation of this will change dependant on the implementation of the algorithm
     */
    List<T> getAll();
}