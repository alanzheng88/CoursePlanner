package ca.courseplanner.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Process course information to 
 * be ready to be displayed in UI
 * 
 * @author Alan
 * 
 */
public class CourseDetailsProcesser {

	private String fullCourseName;
	private String location;
	private List<Semester> filteredSemesterList;
	private List<String> sectionTypes;
	private List<String> enrollments;
	private String strm;
	private String instructor;
	
	public CourseDetailsProcesser(String fullCourseName, String location) {
		this.fullCourseName = fullCourseName;
		this.location = location;
		filteredSemesterList = new ArrayList<Semester>();
		sectionTypes = new ArrayList<String>();
		enrollments = new ArrayList<String>();
	}

	public void updateCourseInfo() {
		iterateOverSemester();
	}

	private void iterateOverSemester() {
		Iterator<Semester> semesterIterator = filteredSemesterList.iterator();
		while (semesterIterator.hasNext()) {
			Semester tempSemester = semesterIterator.next();
			updateStrmAndInstructor(tempSemester);
			iterateOverSectionType(tempSemester);
		}
	}

	private void updateStrmAndInstructor(Semester tempSemester) {
		strm = tempSemester.getStrm();
		instructor = tempSemester.getInstructor();
	}

	private void iterateOverSectionType(Semester tempSemester) {
		Iterator<SectionType> sectionTypeIterator = tempSemester.iterator();
		while (sectionTypeIterator.hasNext()) {
			SectionType tempSectionType = sectionTypeIterator.next();
			updateSectionType(tempSectionType);
			updateEnrollment(tempSectionType);
		}
	}

	private void updateSectionType(SectionType tempSectionType) {
		String sectionType = tempSectionType.getSectionType();
		sectionTypes.add(sectionType);
	}

	private void updateEnrollment(SectionType tempSectionType) {
		String enrollmentTotal = tempSectionType.getEnrollmentTotal();
		String enrollmentCap = tempSectionType.getEnrollmentCap();
		String enrollment = enrollmentTotal + " / " + enrollmentCap;
		enrollments.add(enrollment);
	}

	public void addToSemesterList(Semester semester) {
		filteredSemesterList.add(semester);
		updateCourseInfo();
	}

	public String getFullCourseNameAndLocation() {
		return fullCourseName + " - " + location;
	}

	public String getFullCourseName() {
		return fullCourseName;
	}

	public String getLocation() {
		return location;
	}

	public String getStrm() {
		return strm;
	}

	public String getInstructor() {
		return instructor;
	}

	public Iterator<String> getSectionTypes() {
		return Collections.unmodifiableList(sectionTypes).iterator();
	}

	public Iterator<String> getEnrollments() {
		return Collections.unmodifiableList(enrollments).iterator();
	}

	@Override
	public boolean equals(Object otherObject) {
		if (otherObject != this)
			return false;
		if (otherObject == null)
			return false;

		if (otherObject.getClass() != this.getClass()) {
			return false;
		}

		CourseDetailsProcesser other = (CourseDetailsProcesser) otherObject;
		return this.fullCourseName.equals(other.fullCourseName);
	}

	@Override
	public int hashCode() {
		return fullCourseName.hashCode() * 13;
	}
	
	@Override
	public String toString() {
		return getClass().getName()
				+ "["
				+ "fullCourseName=" + fullCourseName
				+ "location=" + location
				+ "filteredSemesterList=" + filteredSemesterList
				+ "sectionTypes=" + sectionTypes
				+ "enrollments=" + enrollments
				+ "strm=" + strm
				+ "instructor=" + instructor
				+ "]";
	}
}
