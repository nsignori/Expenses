package budget;

import java.util.Comparator;

public class ComparatorDate implements Comparator<Purchase> {
	public int compare(Purchase a, Purchase b) {
		return a.getDate().compareTo(b.getDate());
	}
}