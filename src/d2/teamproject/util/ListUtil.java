package d2.teamproject.util;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides some simple utilities to format and print lists nicely
 * @author Joseph Groocock
 */
public class ListUtil {
    /**
     * Formats a list into an array-style string
     * @param list list to format
     * @return formatted string of list
     */
    public static <T> String listToString(List<T> list) {
        return '[' + list.stream().map(T::toString).collect(Collectors.joining(", ")) + ']';
    }

    /**
     * Prints a list formatted with {@code listToString(List<T>  list)} to System.out
     * @param list list to format
     */
    public static <T> void printList(List<T> list) {
        System.out.println(listToString(list));
    }
}
