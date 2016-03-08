package d2.teamproject.algorithm.sorting;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import d2.teamproject.algorithm.AlgoStream;

/**
 * 
 *@author Otonye
 *@class BubbleSort
 */

public class BubbleSort<E extends Comparable<E>>{
	// TODO: Convert to SortStream for use with PlanetSorting
	 /**
	  * sort 
	  * @param items
	  * @return items
	  */
	public List<E> sort(List<E> items){
		for(int i = 0; i < items.size() - 1; i++){
			for(int k = items.size() - 1; k > i; k--){
				if(items.get(i).compareTo(items.get(k)) > 0){
					E store = items.get(i);
					items.set(i, items.get(k));
					items.set(k, store);
				}
			}
		}
		return items;
	}
	public static void main(String[] args) {
        //List<Integer> list = new Random().ints(20, 0, 100).boxed().collect(Collectors.toList());
		List<Integer> list= new ArrayList<>();
    	list.add(2);
    	list.add(6);
    	list.add(3);
    	list.add(8);
    	list.add(9);
    	list.add(4);
    	list.add(1);
    	
        System.out.println(list.stream().map(Object::toString).collect(Collectors.joining(", ")));
        System.out.println(new BubbleSort<Integer>()
        		.sort(list)
        		.stream()
        		.map(Object::toString)
        		.collect(Collectors.joining(", ")));
    }
	
}
