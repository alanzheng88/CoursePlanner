package ca.courseplanner.model;

/**
 * Class holds information about courses offered 
 * in a particular semester for the section type
 * and groups sections accordingly
 * 
 * @author Alan
 * 
 */
public class SectionType {

	private String sectionType;
	private int enrollmentTotal;
	private int enrollmentCap;

	public SectionType(String ssrComponent) {
		this.sectionType = ssrComponent;
		enrollmentTotal = 0;
		enrollmentCap = 0;
	}

	public void updateEnrollmentInfo(Row courseInfo) {
		updateEnrollmentTotal(courseInfo.getEnrollmentTotal());
		updateEnrollmentCap(courseInfo.getEnrollmentCap());
	}

	private void updateEnrollmentTotal(String totalForEnrollment) {
		int valueToAddAsInt = Integer.parseInt(totalForEnrollment);
		enrollmentTotal += valueToAddAsInt;
	}

	private void updateEnrollmentCap(String capForEnrollment) {
		int valueToAddAsInt = Integer.parseInt(capForEnrollment);
		enrollmentCap += valueToAddAsInt;
	}
	
	public String getSectionType() {
		return sectionType;
	}
	
	public String getEnrollmentTotal() {
		return Integer.toString(enrollmentTotal);
	}
	
	public String getEnrollmentCap() {
		return Integer.toString(enrollmentCap);
	}

	@Override
	public boolean equals(Object otherObject) {
		if(otherObject == this) return true;
		if(otherObject == null) return false;
		
		if(otherObject.getClass() != this.getClass()) {
			return false;
		}

		SectionType other = (SectionType) otherObject;
		String otherSectionType = other.getSectionType();
		return (this.sectionType.equals(otherSectionType));
	}
	
	@Override
	public int hashCode() {
		return sectionType.hashCode() * 11;
	}
	
	@Override
	public String toString() {
		return getClass().getName()
				+ "["
				+ "sectionType=" + sectionType
				+ "enrollmentTotal=" + enrollmentTotal
				+ "enrollmentCap=" + enrollmentCap
				+ "]";
	}

}
