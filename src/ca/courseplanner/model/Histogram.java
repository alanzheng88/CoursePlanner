package ca.courseplanner.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Sets up histogram for a list of numbers
 * 
 * @author Alan
 *
 */
public class Histogram implements Iterable<Histogram.Bar>{

	private int[] integerList;
	private int numberBars = 1;
	private Histogram.Bar[] barList;
	
	public Histogram(int[] integerList, int numberOfBars) {
		setData(integerList);
		setNumberBars(numberOfBars);
	}
	
	public void setData(int[] integerList) {
		if(integerList == null) {
			throw new IllegalArgumentException("integerList is null");
		}
		checkData(integerList);
		int[] tempIntegerList = Arrays.copyOf(integerList, integerList.length);
		Arrays.sort(tempIntegerList);
		this.integerList = tempIntegerList;
		createBarList();
	}
	
	private void checkData(int[] integerList) {
		for(int integer : integerList) {
			if(integer < 0) {
				throw new IllegalArgumentException("Invalid data in integerList");
			}
		}
	}

	public void setNumberBars(int numberOfBars) {
		if(numberOfBars < 0) {
			throw new IllegalArgumentException("Number of Bars is less than 0");
		}
		this.numberBars = numberOfBars;
		createBarList();
	}
	
	private void createBarList() {
		barList = new Bar[numberBars];
		int barRange = computeIntervalLength();
		int[] integerCountList = new int[barList.length];
		addIntegerCountToList(integerCountList, barRange);
		addToBarList(integerCountList, barRange);
	}

	// assumes integer list is sorted
	private int computeIntervalLength() {
		boolean emptyList = integerList.length == 0;
		if(emptyList) {
			return 1;
		}
		int maxDataValue = integerList[integerList.length-1];
		assert numberBars > 0;
		// add 1 to include starting 0
		int barRange = (maxDataValue / numberBars) + 1;
		return barRange;
	}

	private void addIntegerCountToList(int[] integerCountList, int barRange) {
		for(int integer : integerList) {
			// needs to adjust integer according to how many parts each bar is divided into
			int barLocation = (int) Math.floor(integer / barRange);
			assert barLocation < integerCountList.length;
			integerCountList[barLocation]++;
		}
	}

	private void addToBarList(int[] integerCountList, int barRange) {
		int rangeMin = 0;
		int rangeMax = barRange - 1;
		for(int i = 0; i < numberBars; i++) {
			int count = integerCountList[i];
			barList[i] = new Bar(rangeMin, rangeMax, count);
			rangeMin += barRange;
			rangeMax += barRange;
		}
	}
	
	public int getNumberBars() {
		return numberBars;
	}
	
	public Iterator<Histogram.Bar> iterator() {
		ArrayList<Bar> tempBarList = new ArrayList<Bar>();
		for(Histogram.Bar bar : barList) {
			tempBarList.add(bar);
		}
		return Collections.unmodifiableList(tempBarList).iterator();
	}
	
	public int getMaxBarCount() {
		Iterator<Histogram.Bar> iterator = iterator();
		Bar bar;
		int nextCount;
		int maxCount = Integer.MIN_VALUE;
		while(iterator.hasNext()) {
			bar = iterator.next();
			nextCount = bar.getCount();
			if(nextCount > maxCount) {
				maxCount = nextCount;
			}
		}
		return maxCount;
	}
	
	public Iterator<Integer> getIntegerListIterator() {
		List<Integer> list = new ArrayList<Integer>();
		for(int integer : integerList) {
			list.add(integer);
		}
		return list.iterator();
	}
	
	public class Bar {
		private final int rangeMin;
		private final int rangeMax;
		private final int count;
		
		public Bar(int rangeMin, int rangeMax, int count) {
			if(rangeMin < 0 || rangeMax < 0 || count < 0) {
				throw new IllegalArgumentException("rangeMin, rangeMax and count cannot be "
						+ "less than 0");
			}
			this.rangeMin = rangeMin;
			this.rangeMax = rangeMax;
			this.count = count;
		}
		
		public String toString() {
			String description = "Bar[" + rangeMin + "," + rangeMax + "] = " + count;
			return description;
		}

		public int getRangeMin() {
			return rangeMin;
		}
		
		public int getRangeMax() {
			return rangeMax;
		}
		
		public int getCount() {
			return count;
		}
	}

}
