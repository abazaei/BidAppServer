package com.example.auctionapplication.search;

import java.util.ArrayList;
import java.util.Collection;

public final class CollectionUtils {
	/**
	 * This method takes a collection of any time and a
	 * predicate of the same type and filtered the list
	 * by that predicate's criteria.
	 * 
	 * The resulting collection contains only the items that
	 * passed the test.
	 * 
	 * @param items
	 * @param test
	 * @return
	 */
	public static <T> Collection<T> filter(Collection<T> items, Predicate<T> test) {
		Collection<T> filtered = new ArrayList<T>();
		for ( T t : items ) {
			if ( test.test(t) ) {
				filtered.add(t);
			}
		}
		return filtered;
	}
}