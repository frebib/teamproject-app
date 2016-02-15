package d2.teamproject.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import d2.teamproject.algorithm.sorting.BubbleSort;

public class BubbleSortTest {

	@Test
	public void test() {
		List<Integer> list= new ArrayList<>();
    	list.add(2);
    	list.add(6);
    	list.add(3);
    	list.add(8);
    	list.add(9);
    	list.add(4);
    	list.add(1);
    	
        list.stream().map(Object::toString).collect(Collectors.joining(", "));
        String result = new BubbleSort<Integer>()
        		.sort(list)
        		.stream()
        		.map(Object::toString)
        		.collect(Collectors.joining(", "));
        assertArrayEquals("1 2 3 4 6 8 9", result);
	}

	private void assertArrayEquals(String string, String result) {
		// TODO Auto-generated method stub
		
	}

}
