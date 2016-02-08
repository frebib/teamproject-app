package d2.teamproject.module.algorithm.sorting;

public class BubbleSort{
	public static int [] sort(int[] items){
		for(int i = 0; i < items.length - 1; i++){
			for(int k = items.length - 1; k > i; k--){
				if(items[i] > items[k]){
					int store = items[i];
					items[i]  = items[k];
					items[k] = store;
				}
			}
		}
		return items;
	}
	
}
