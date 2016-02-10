package d2.teamproject.module.algorithm.sorting;

import java.util.List;
/**
 * 
 * @author Otonye
 *
 * @param <E>
 */

public class QuickSort<E extends Comparable<E>> {
    private List<E> list;

    public QuickSort(List<E> list) {
        this.list = list;
    }

    /**
     * sort: Applies quick sort to a list of Generic objects
     * @return
     */
    public List<E> sort() {
        if (list == null || list.size() < 1) return null;
        quickSort(0, list.size() - 1);
        return list;
    }
    /**
     * @param lowerIndex
     * @param higherIndex
     */
    
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
    /**
     * exchangeNumber: Use to swap numbers at specific positions
     * @param i
     * @param j
     */
    private void exchangeNumbers(int i, int j) {
        E temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
}