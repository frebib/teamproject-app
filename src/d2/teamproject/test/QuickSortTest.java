package d2.teamproject.test;

import d2.teamproject.algorithm.sorting.CompareSortState;
import d2.teamproject.algorithm.sorting.ListSortState;
import d2.teamproject.algorithm.sorting.QuickSortStream;
import d2.teamproject.algorithm.sorting.SortState;
import org.junit.Assert;
import org.junit.Test;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class QuickSortTest {

    @Test
    public void quickSortTest() {
        List<Integer> sortedList = IntStream.range(0, 20).boxed().collect(Collectors.toList());
        List<Integer> shuffledList = new ArrayList<>(sortedList);
        Collections.shuffle(shuffledList);

//        System.out.printf("Before: [%s]\n", listToString(shuffledList));
        QuickSortStream<Integer> sorter = new QuickSortStream<>(shuffledList, Integer::compare);
        sorter.initialise();
//        System.out.printf("After:  [%s]\n", listToString(shuffledList));
        Assert.assertTrue(listEqual(sortedList, sorter.getSortedList()));
    }

    /**
     * Ensures no comparisons are made on the same element
     */
    @Test
    public void sameComparisonTest() {
        List<Integer> shuffled = IntStream.range(0, 20).boxed().collect(Collectors.toList());
        Collections.shuffle(shuffled);

        QuickSortStream<Integer> sorter = new QuickSortStream<>(shuffled, Integer::compare);
        sorter.initialise();

        boolean noSameCompare = true;
        SortState<Integer> state;
        while ((state = sorter.getNext()) != null) {
            if (state instanceof CompareSortState) {
                Point compares = ((CompareSortState) state).getCompares();
                noSameCompare &= compares.getX() != compares.getY();
            }
        }

        Assert.assertTrue(noSameCompare);
    }

    @Test
    public void listStateCorrectness() {
        List<Integer> shuffled = IntStream.range(0, 20).boxed().collect(Collectors.toList());
        Collections.shuffle(shuffled);

        QuickSortStream<Integer> sorter = new QuickSortStream<>(shuffled, Integer::compare);
        sorter.initialise();

        while(sorter.hasNext()) {
            SortState<Integer> state = sorter.getNext();
            if (state instanceof CompareSortState) {
                CompareSortState<Integer> csstate = (CompareSortState<Integer>) state;
                if (!csstate.isSwap()) continue;

                SortState<Integer> newState = sorter.getNext();
                Assert.assertTrue(newState instanceof ListSortState);

                List<Integer> newList = newState.getList();
                List<Integer> oldList = state.getList();

                // Swap elements in old list- they should then be equal
                Point p = csstate.getCompares();
                int temp = oldList.get(p.x);
                oldList.set(p.x, oldList.get(p.y));
                oldList.set(p.y, temp);

//                System.out.println("Old: " + listToString(oldList));
//                System.out.println("New: " + listToString(newList));

                Assert.assertTrue(listEqual(oldList, newList));
            }
        }
    }
//
//    private <T> String listToString(List<T> list) {
//        return list.stream().map(T::toString).collect(Collectors.joining(", "));
//    }

    private <E extends Comparable<E>> boolean listEqual(List<E> a, List<E> b) {
        if (a.size() != b.size())
            return false;
        for (int i = 0; i < a.size(); i++)
            if (!a.get(i).equals(b.get(i)))
                return false;
        return true;
    }
}
