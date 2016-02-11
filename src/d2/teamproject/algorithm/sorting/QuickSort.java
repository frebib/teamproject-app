package d2.teamproject.algorithm.sorting;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class QuickSort<E extends Comparable<E>> {
    private List<E> list;

    public QuickSort(List<E> list) {
        this.list = list;
    }

    public List<E> sort() {
        if (list == null || list.size() < 1) return null;
        quickSort(0, list.size() - 1);
        return list;
    }

    private void quickSort(int lowerIndex, int higherIndex) {
        int i = lowerIndex;
        int j = higherIndex;
        // calculate pivot number, I am taking pivot as middle index number
        E pivot = list.get(lowerIndex + (higherIndex - lowerIndex) / 2);
        // Divide into two arrays
        while (i <= j) {
            while (list.get(i).compareTo(pivot) < 0) i++;
            while (list.get(j).compareTo(pivot) > 0) j--;

            if (i <= j)
                // Swap values then move index
                // to next position on both sides
                exchangeNumbers(i++, j--);
        }
        // call quickSort() method recursively
        if (lowerIndex < j)
            quickSort(lowerIndex, j);
        if (i < higherIndex)
            quickSort(i, higherIndex);
    }

    private void exchangeNumbers(int i, int j) {
        E temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
    public static void main(String[] args) {
        List<Integer> list = new Random().ints(20, 0, 100).boxed().collect(Collectors.toList());
        System.out.println(list.stream().map(Object::toString).collect(Collectors.joining(", ")));
        QuickSort<Integer> sort = new QuickSort<>(list);
        System.out.println(sort.sort().stream().map(Object::toString).collect(Collectors.joining(", ")));
    }
}