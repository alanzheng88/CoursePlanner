package ca.courseplanner.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Class used by CourseOfferingProcesser 
 * to group semester offerings by year
 * 
 * @author Alan
 *
 */
public class Year implements Iterable<Semester> {
	
	private static final int YEAR_OFFSET = 19;
	private String year;
	private ArrayList<Semester> semesters;
	
	public Year(String year) {
		this.year = year;
		semesters = new ArrayList<Semester>();
	}

	public static String convertToYear(String strm) {
		assert strm.length() == 4;
		String centuryStr = strm.substring(0,1);
		String decadeYearStr = strm.substring(1,3);
		int centuryDigit = Integer.parseInt(centuryStr) + YEAR_OFFSET;
		String century = Integer.toString(centuryDigit);
		String tempYear = century + decadeYearStr;
		return tempYear;
	}
	
	public void addSemester(Semester semester) {
		semesters.add(semester);
	}
	
	@Override
	public Iterator<Semester> iterator() {
		return Collections.unmodifiableList(semesters).iterator();
	}
	
	@Override
	public boolean equals(Object otherObject) {
		if(otherObject == this) return true;
		if(otherObject == null) return false;
		
		if(otherObject.getClass() != this.getClass()) {
			return false;
		}
		Year other = (Year) otherObject;
		return (this.year.equals(other.year));
	}
	
	@Override
	public int hashCode() {
		return year.hashCode() * 11;
	}
	
	@Override
	public String toString() {
		return getClass().getName()
				+ "["
				+ "year=" + year
				+ "coursesOffered=" + semesters.toString()
				+ "]";
	}
}
