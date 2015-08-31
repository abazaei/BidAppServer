package com.example.auctionapplication.search;

/**
 * A typed version of the Predicate interface. This way, it can be
 * used more flexibly in order to describe tests based on any data
 * type.
 * 
 * @author jzheaux
 *
 * @param <T>
 */
public interface Predicate<T> {
	boolean test(T itemToBeTested);
}
