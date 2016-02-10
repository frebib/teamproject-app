package d2.teamproject.module.algorithm.sorting;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 
 *@author Otonye
 *@class BubbleSort
 */

public class BubbleSort<E extends Comparable<E>>{
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
        List<Integer> list = new Random().ints(20, 0, 100).boxed().collect(Collectors.toList());
        System.out.println(list.stream().map(Object::toString).collect(Collectors.joining(", ")));
        System.out.println(new BubbleSort<Integer>()
        		.sort(list)
        		.stream()
        		.map(Object::toString)
        		.collect(Collectors.joining(", ")));
    }
	
}
