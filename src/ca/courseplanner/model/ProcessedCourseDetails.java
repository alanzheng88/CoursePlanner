package ca.courseplanner.model;

import java.util.ArrayList;
import java.util.List;

public class ProcessedCourseDetails {

	private String fullCourseName;
	private List<Semester> filteredSemesterList;
	
	public ProcessedCourseDetails(String fullCourseName) {
		this.fullCourseName = fullCourseName;
		filteredSemesterList = new ArrayList<Semester>();
	}
	
	public void addToSemesterList(Semester semester) {
		filteredSemesterList.add(semester);
	}
	
	@Override
	public boolean equals(Object otherObject) {
		if (otherObject != this) return false;
		if (otherObject == null) return false;
		
		if(otherObject.getClass() != this.getClass()) {
			return false;
		}
		
		ProcessedCourseDetails other = (ProcessedCourseDetails) otherObject;
		return this.fullCourseName.equals(other.fullCourseName);
	}
	
	@Override
	public int hashCode() {
		return fullCourseName.hashCode() * 13;
	}
}
