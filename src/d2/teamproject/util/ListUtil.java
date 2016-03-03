package d2.teamproject.util;

import java.util.List;
import java.util.stream.Collectors;

public class ListUtil {
    public static <T> String listToString(List<T> list) {
        return '[' + list.stream().map(T::toString).collect(Collectors.joining(", ")) + ']';
    }
    public static <T> void printList(List<T> list) {
        System.out.println(listToString(list));
    }
}
