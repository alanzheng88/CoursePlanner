package ca.courseplanner.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Holds information about subject and adds course to the class Course
 * 
 * @author Alan
 * 
 */
public class Subject implements Iterable<Course> {

	private String subject; // e.g: CMPT
	private List<Course> courses; // CMPT has a list of courses e.g: 125, 275

	public Subject(String subject) {
		this.subject = subject;
		courses = new ArrayList<Course>();
	}

	public void addCourse(Course course) {
		addNewCourseList(course);
		addCourseOfferedToExistingCourseList(course);
	}

	private void addNewCourseList(Course course) {
		if (!courses.contains(course)) {
			courses.add(course);
		}
	}

	private void addCourseOfferedToExistingCourseList(Course course) {
		Course existingCourse = getCourseFromList(course);
		existingCourse.addCourseOffered(course.getCourseOffered());
	}

	private Course getCourseFromList(Course courseToFind) {
		int coursePosition = courses.indexOf(courseToFind);
		assert coursePosition != -1;
		Course existingCourse = courses.get(coursePosition);
		return existingCourse;
	}

	public String getMinYearFromSelectedCourse(Course course) {
		Course selectedCourse = getCourseFromList(course);
		return selectedCourse.getMinimumYear();
	}

	public String getMaxYearFromSelectedCourse(Course course) {
		Course selectedCourse = getCourseFromList(course);
		return selectedCourse.getMaximumYear();
	}

	public Course getCourseFromName(String courseToFind) {
		for (Course course : courses) {
			String catalogNumber = course.getCatalogNumber();
			if (catalogNumber.equals(courseToFind)) {
				return course;
			}
		}
		assert false;
		return null;
	}

	public String getSubject() {
		return subject;
	}

	public void sortCoursesByCatalogNumber() {
		Collections.sort(courses);
	}

	public void clearCourses() {
		courses.clear();
	}

	@Override
	public boolean equals(Object otherObject) {
		if (otherObject == this) return true;
		if (otherObject == null) return false;

		if (otherObject.getClass() != this.getClass()) {
			return false;
		}
		Subject other = (Subject) otherObject;
		return (this.subject.equals(other.getSubject()));
	}

	@Override
	public int hashCode() {
		return subject.hashCode() * 13;
	}

	@Override
	public Iterator<Course> iterator() {
		return Collections.unmodifiableList(courses).iterator();
	}

	@Override
	public String toString() {
		return getClass().getName() 
				+ "[" 
				+ "subject=" + subject 
				+ "]";
	}
}
