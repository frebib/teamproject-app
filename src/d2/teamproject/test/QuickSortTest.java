package d2.teamproject.test;

import d2.teamproject.algorithm.sorting.QuickSortStream;
import org.junit.Assert;
import org.junit.Test;

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

        QuickSortStream<Integer> sorter = new QuickSortStream<>(shuffledList, Integer::compare);
        sorter.initialise();
        Assert.assertTrue(listEqual(sortedList, sorter.getSortedList()));
    }

    private <E extends Comparable<E>> boolean listEqual(List<E> a, List<E> b) {
        if (a.size() != b.size())
            return false;
        for (int i = 0; i < a.size(); i++)
            if (!a.get(i).equals(b.get(i)))
                return false;
        return true;
    }
}
