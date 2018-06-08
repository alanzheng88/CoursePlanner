package ca.courseplanner.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Class groups semester by using 
 * strm and location 
 * 
 * @author Alan
 *
 */
public class Semester implements Iterable<SectionType> {

	private static final String SPRING = "1";
	private static final String SUMMER = "4";
	private static final String FALL = "7";
	private String strm;
	private String location;
	private String instructors;
	private String season;
	private List<SectionType> sectionTypes;

	public Semester(String strm, String location, String instructors) {
		this.strm = strm;
		this.location = location;
		this.instructors = removeQuotations(instructors);
		season = convertToSeason(strm);
		sectionTypes = new ArrayList<SectionType>();
	}
	
	private String removeQuotations(String tempInstructors) {
		if(tempInstructors.startsWith("\"")) {
			return tempInstructors.replace("\"", "");
		}
		return tempInstructors;
	}
	
	public static String convertToSeason(String strm) {
		String endValue = strm.substring(strm.length() - 1);
		switch (endValue) {
		case SPRING:
			return "SPRING";
		case SUMMER:
			return "SUMMER";
		case FALL:
			return "FALL";
		}
		assert false;
		return null;
	}
	
	public static String convertToSeasonNumber(String season) {
		if(season.equals("SPRING")) {
			return getSpringNum();
		} else if(season.equals("SUMMER")) {
			return getSummerNum();
		} else if(season.equals("FALL")) {
			return getFallNum();
		} else {
			return null;
		}
	}

	public void addToSectionType(CourseOffered courseOffered) {
		Row rowInfo = courseOffered.getCourseInfo();
		String ssrComponent = rowInfo.getSsrComponent();
		SectionType sectionType = new SectionType(ssrComponent);
		updateInstructors(rowInfo.getInstructors());
		addNewSectionType(sectionType);
		addToExistingSectionType(sectionType, rowInfo);
	}

	private void updateInstructors(String instructors) {
		String tempInstructors = removeQuotations(instructors);
		String[] instructorsToPossiblyAdd = tempInstructors.split(",");
		addInstructors(instructorsToPossiblyAdd);
	}
	
	private void addInstructors(String[] instructorsToPossiblyAdd) {
		for(String instructor : instructorsToPossiblyAdd) {
			if(!instructors.contains(instructor)) {
				boolean isOneOrMoreInstructors = instructors.split(",").length >= 1;
				String comma = isOneOrMoreInstructors ? ", " : "";
				instructors += comma + instructor;
			}
		}
	}
	
	private void addNewSectionType(SectionType sectionType) {
		if(!sectionTypes.contains(sectionType)) {
			sectionTypes.add(sectionType);
		}
	}
	
	private void addToExistingSectionType(SectionType sectionType, Row rowInfo) {
		int position = sectionTypes.indexOf(sectionType);
		SectionType sectionToAddTo = sectionTypes.get(position); 
		sectionToAddTo.updateEnrollmentInfo(rowInfo);
	}
	
	public String getStrm() {
		return strm;
	}
	
	public String getLocation() {
		return location;
	}
	
	public String getInstructor() {
		return instructors;
	}
	
	public String getSeason() {
		return season;
	}
	
	public static String getSpringNum() {
		return SPRING;
	}

	public static String getSummerNum() {
		return SUMMER;
	}

	public static String getFallNum() {
		return FALL;
	}
	
	public static String getSpring() {
		return "SPRING";
	}
	
	public static String getSummer() {
		return "SUMMER";
	}
	
	public static String getFall() {
		return "FALL";
	}

	@Override
	public Iterator<SectionType> iterator() {
		return Collections.unmodifiableList(sectionTypes).iterator();
	}

	@Override
	public boolean equals(Object otherObject) {
		if(otherObject == this) return true;
		if(otherObject == null) return false;
		
		if(otherObject.getClass() != this.getClass()) {
			return false;
		}
		Semester other = (Semester) otherObject;
		return (this.strm.equals(other.strm) 
				&& this.location.equals(other.location));
	}
	
	@Override
	public int hashCode() {
		return strm.hashCode() * 11
				+ location.hashCode() * 13;

	}
	
	@Override
	public String toString() {
		return getClass().getName()
				+ "["
				+ "strm=" + strm
				+ "location=" + location
				+ "instructor=" + instructors
				+ "season=" + season
				+ "sectionTypes" + sectionTypes.toString()
				+ "]";
	}
}
