package com.example.auctionapplication.search;

/**
 * Takes two predicates and returns true only if both predicates
 * provided return true.
 * 
 * This class is effective at representing an AND boolean expression
 * via the Predicate pattern. It provides short-circuiting just like the
 * typical && in an if statement.
 * 
 * Take special note that this class enables you to combine
 * predicates into a compound boolean expression like
 * 
 *     test#1 OR test#2
 *     
 * can be expressed by:
 * 
 *     OrPredicate o = new OrPredicate(test#1, test#2);
 *     o.test(candidate); // this test will pass if test#1 or test#2 passes
 *     
 * @author jzheaux
 *
 * @param <T> - The type of the passed predicates, e.g. the type
 * of the item in the collection you are trying to filter, etc.
 */
public class OrPredicate<T> implements Predicate<T> {
	/* Notice these are both final. Immutability is desireable. */
	private final Predicate<T> left;
	private final Predicate<T> right;
	
	public OrPredicate(Predicate<T> left, Predicate<T> right) {
		this.left = left;
		this.right = right;
	}
	
	@Override
	public boolean test(T itemToBeTested) {
		return left.test(itemToBeTested) || right.test(itemToBeTested);
	}
}
