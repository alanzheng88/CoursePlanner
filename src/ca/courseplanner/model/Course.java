package ca.courseplanner.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Holds all course offerings and finds minimum and maximum years for a
 * particular course (e.g: CMPT 165)
 * 
 * @author Alan
 * 
 */
public class Course implements Iterable<Semester>, Comparable<Course> {

	private String catalogNumber; // 499 and below = undergrad && 500 and above = grad
	private Row rowInfo;
	private List<Semester> semesters;
	private int minimumYear;
	private int maximumYear;

	public Course(Row rowInfo) {
		this.catalogNumber = rowInfo.getCatalogNumber();
		this.rowInfo = rowInfo;
		semesters = new ArrayList<Semester>();
		minimumYear = Integer.MAX_VALUE;
		maximumYear = Integer.MIN_VALUE;
	}

	public String getCatalogNumber() {
		return catalogNumber;
	}

	public CourseOffered getCourseOffered() {
		return new CourseOffered(rowInfo);
	}

	public void addCourseOffered(CourseOffered courseOffered) {
		Row courseInfo = courseOffered.getCourseInfo();
		String strm = courseInfo.getStrm();
		String location = courseInfo.getLocation();
		String instructor = courseInfo.getInstructors();
		String yearToCheck = Year.convertToYear(strm);
		updateMinAndMaxYear(yearToCheck);
		Semester tempSemester = new Semester(strm, location, instructor);
		addNewSemester(tempSemester);
		addToExistingSemester(tempSemester, courseOffered);
	}

	public String getMinimumYear() {
		return String.valueOf(minimumYear);
	}

	public String getMaximumYear() {
		return String.valueOf(maximumYear);
	}

	private void updateMinAndMaxYear(String year) {
		int yearAsInt = Integer.parseInt(year);
		if (yearAsInt > maximumYear) {
			maximumYear = yearAsInt;
		}
		if (yearAsInt < minimumYear) {
			minimumYear = yearAsInt;
		}
	}

	private void addNewSemester(Semester tempSemester) {
		if (!semesters.contains(tempSemester)) {
			semesters.add(tempSemester);
		}
	}

	private void addToExistingSemester(Semester tempSemester,
			CourseOffered courseOffered) {
		int position = semesters.indexOf(tempSemester);
		Semester semesterToAddTo = semesters.get(position);
		semesterToAddTo.addToSectionType(courseOffered);
	}

	@Override
	public boolean equals(Object otherObject) {
		if (otherObject == this)
			return true;
		if (otherObject == null)
			return false;

		if (otherObject.getClass() != this.getClass()) {
			return false;
		}
		Course other = (Course) otherObject;
		// assumes catalogNumber is not null
		return (this.catalogNumber.equals(other.getCatalogNumber()));
	}

	@Override
	public int hashCode() {
		return catalogNumber.hashCode() * 11;
	}

	@Override
	public String toString() {
		return getClass().getName() 
				+ "[" 
				+ "catalogNumber=" + catalogNumber
				+ "]";
	}

	@Override
	public Iterator<Semester> iterator() {
		return Collections.unmodifiableList(semesters).iterator();
	}

	@Override
	public int compareTo(Course other) {
		return catalogNumber.compareTo(other.getCatalogNumber());
	}
}
