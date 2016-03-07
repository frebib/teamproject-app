package d2.teamproject.algorithm.sorting;

import d2.teamproject.algorithm.AlgoStream;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 
 *@author Otonye
 *@class BubbleSortStream
 */

public class BubbleSortStream<E extends Comparable<E>> implements AlgoStream<SortState<E>> {
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

	@Override
	public AlgoStream<SortState<E>> initialise() {
		return null;
	}

	@Override
	public SortState<E> getNext() {
		return null;
	}

	@Override
	public SortState<E> getPrevious() {
		return null;
	}

	@Override
	public SortState<E> getNth(int n) throws UnsupportedOperationException {
		return null;
	}

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public boolean hasPrevious() {
		return false;
	}

	@Override
	public boolean hasNth(int n) {
		return false;
	}

	@Override
	public List<SortState<E>> getAll() {
		return null;
	}
}
