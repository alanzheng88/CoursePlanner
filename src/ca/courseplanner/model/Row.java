package ca.courseplanner.model;

/**
 * Class holds all information for
 * a row taken from file
 * 
 * @author Alan
 *
 */
public class Row {

	public static final String STRM = "STRM";
	public static final String SUBJECT = "SUBJECT";
	public static final String CATALOG_NBR = "CATALOG_NBR";
	public static final String LOCATION = "LOCATION";
	public static final String ENRL_CAP = "ENRL_CAP";
	public static final String SSR_COMPONENT = "SSR_COMPONENT";
	public static final String ENRL_TOT = "ENRL_TOT";
	public static final String INSTRUCTORS = "INSTRUCTORS";
	private String strm;
	private String subject;
	private String catalogNumber;
	private String location;
	private String enrollmentCap;
	private String ssrComponent;
	private String enrollmentTotal;
	private String instructors;

	public Row(String[] fileInfo, ColumnSelector selector) {
		// STRM,SUBJECT,CATALOG_NBR,LOCATION,ENRL_CAP,SSR_COMPONENT,ENRL_TOT,INSTRUCTORS
		int strmPosition = selector.findLocation(STRM);
		int subjectPosition = selector.findLocation(SUBJECT);
		int catalogNumberPosition = selector.findLocation(CATALOG_NBR);
		int locationPosition = selector.findLocation(LOCATION);
		int enrollmentCapPosition = selector.findLocation(ENRL_CAP);
		int ssrComponentPosition = selector.findLocation(SSR_COMPONENT);
		int enrollmentTotalPosition = selector.findLocation(ENRL_TOT);
		int instructorsPosition = selector.findLocation(INSTRUCTORS);

		strm = fileInfo[strmPosition];
		subject = fileInfo[subjectPosition];
		catalogNumber = fileInfo[catalogNumberPosition];
		location = fileInfo[locationPosition];
		enrollmentCap = fileInfo[enrollmentCapPosition];
		ssrComponent = fileInfo[ssrComponentPosition];
		enrollmentTotal = fileInfo[enrollmentTotalPosition];
		instructors = fileInfo[instructorsPosition];
	}
	
	@Override
	public boolean equals(Object otherObject) {
		if(otherObject == this) return true;
		if(otherObject == null) return false;
		
		if(otherObject.getClass() != this.getClass()) {
			return false;
		}
		Row other = (Row) otherObject;
		return (this.strm.equals(other.strm) && this.subject.equals(other.subject)
			&& this.catalogNumber.equals(other.catalogNumber) && this.location.equals(other.location)
			&& this.enrollmentCap.equals(other.enrollmentCap) && this.ssrComponent.equals(other.ssrComponent)
			&& this.enrollmentTotal.equals(other.enrollmentTotal) && this.instructors.equals(other.instructors));
				
	}

	@Override
	public int hashCode() {
		return strm.hashCode() * 11
				+ subject.hashCode() * 13
				+ catalogNumber.hashCode() * 17
				+ location.hashCode() * 19
				+ enrollmentCap.hashCode() * 23
				+ ssrComponent.hashCode() * 29
				+ enrollmentTotal.hashCode() * 31
				+ instructors.hashCode() * 37;
	}
	
	@Override
	public String toString() {
		return getClass().getName()
				+ "["
				+ "strm=" + strm
				+ ",subject=" + subject
				+ ",catalogNumber=" + catalogNumber
				+ ",location=" + location 
				+ ",enrollmentCap=" + enrollmentCap 
				+ ",ssrComponent=" + ssrComponent
				+ ",enrollmentTotal=" + enrollmentTotal
				+ ",instructors=" + instructors
				+ "]";
	}
	
	public String getStrm() {
		return strm;
	}

	public String getSubject() {
		return subject;
	}

	public String getCatalogNumber() {
		return catalogNumber;
	}

	public String getLocation() {
		return location;
	}

	public String getEnrollmentCap() {
		return enrollmentCap;
	}

	public String getSsrComponent() {
		return ssrComponent;
	}

	public String getEnrollmentTotal() {
		return enrollmentTotal;
	}

	public String getInstructors() {
		return instructors;
	}
}
