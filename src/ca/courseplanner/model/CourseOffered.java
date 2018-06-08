package ca.courseplanner.model;

/**
 * Class used to hold course info
 * and provide functionality to
 * check if two courses offered are
 * equal by comparing location and 
 * instructor
 * 
 * @author Alan
 *
 */
public class CourseOffered {

	private Row courseInfo;

	public CourseOffered(Row rowInfo) {
		courseInfo = rowInfo;
	}

	public Row getCourseInfo() {
		return courseInfo;
	}

	@Override
	public boolean equals(Object otherObject) {
		if (otherObject == this) return true;
		if (otherObject == null) return false;
		
		if (otherObject.getClass() != this.getClass()) {
			return false;
		}
		CourseOffered other = ((CourseOffered) otherObject);
		Row otherCourseInfo = other.getCourseInfo();
		String location = courseInfo.getLocation();
		String instructor = courseInfo.getInstructors();
		String otherLocation = otherCourseInfo.getLocation();
		String otherInstructor = otherCourseInfo.getInstructors();
		return (location.equals(otherLocation) 
				&& instructor.equals(otherInstructor));
	}
	
	@Override
	public int hashCode() {
		String location = courseInfo.getLocation();
		String instructor = courseInfo.getInstructors();
		return location.hashCode() * 11
				+ instructor.hashCode() * 13;
	}
	
	@Override
	public String toString() {
		return getClass().getName()
				+ "["
				+ courseInfo.toString()
				+ "]";
	}
}
