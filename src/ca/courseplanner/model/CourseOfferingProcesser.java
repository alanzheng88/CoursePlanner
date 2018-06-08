package ca.courseplanner.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Class used to find necessary information 
 * to display on course offerings UI for a
 * particular season
 * 
 * @author Alan
 * 
 */
public class CourseOfferingProcesser {

	private List<Year> years;
	private String seasonNum;
	private String subject;
	private String course;

	public CourseOfferingProcesser(Subject currentlySelectedSubject,
			Course currentlySelectedCourse, Iterator<String> listOfYears,
			String season) {
		years = new ArrayList<Year>();
		addYearsToList(listOfYears);
		seasonNum = Semester.convertToSeasonNumber(season);
		iterateOverSemester(currentlySelectedCourse);
		subject = currentlySelectedSubject.getSubject();
		course = currentlySelectedCourse.getCatalogNumber();
	}

	private void addYearsToList(Iterator<String> listOfYears) {
		while (listOfYears.hasNext()) {
			String year = listOfYears.next();
			years.add(new Year(year));
		}
	}

	private void iterateOverSemester(Course currentlySelectedCourse) {
		Iterator<Semester> semesterIterator = currentlySelectedCourse.iterator();
		while (semesterIterator.hasNext()) {
			Semester semester = semesterIterator.next();
			addSemesterToYear(semester);
		}
	}

	private void addSemesterToYear(Semester semester) {
		String strm = semester.getStrm();
		Year yearToAddTo = getYearFromStrm(strm);
		int positionOfYearInList = years.indexOf(yearToAddTo);
		assert positionOfYearInList != -1;
		Year year = years.get(positionOfYearInList);
		year.addSemester(semester);
	}

	private Year getYearFromStrm(String strm) {
		String yearConvertedFromStrm = Year.convertToYear(strm);
		Year year = new Year(yearConvertedFromStrm);
		return year;
	}

	public List<CourseDetailsProcesser> getBriefCourseInfoForYear(String yearToFind) {
		List<CourseDetailsProcesser> briefCourseInfoList = new ArrayList<CourseDetailsProcesser>();
		String fullCourseName;
		Iterator<Semester> semesterIterator = getSemesterOfferingsByYear(yearToFind);
		while (semesterIterator.hasNext()) {
			fullCourseName = getFullCourseName();
			Semester semester = semesterIterator.next();
			String location = semester.getLocation();
			CourseDetailsProcesser tempProcesser = new CourseDetailsProcesser(fullCourseName, location);
			tempProcesser.addToSemesterList(semester);
			briefCourseInfoList.add(tempProcesser);
		}
		return briefCourseInfoList;
	}

	public String getFullCourseName() {
		return subject + " " + course;
	}

	public Iterator<Semester> getSemesterOfferingsByYear(String yearToFind) {
		List<Semester> semesters = new ArrayList<Semester>();
		Year tempYear = new Year(yearToFind);
		int positionOfYearInList = years.indexOf(tempYear);
		assert positionOfYearInList != -1;
		Year yearOfCoursesOffered = years.get(positionOfYearInList);
		iterateOverSemester(semesters, yearOfCoursesOffered);
		return Collections.unmodifiableList(semesters).iterator();
	}

	private void iterateOverSemester(List<Semester> semesters,
			Year yearOfCoursesOffered) {
		Iterator<Semester> semesterIterator = yearOfCoursesOffered.iterator();
		while (semesterIterator.hasNext()) {
			Semester semester = semesterIterator.next();
			String strm = semester.getStrm();
			addToSemesters(semesters, semester, strm);
		}
	}

	private void addToSemesters(List<Semester> semesters, Semester semester,
			String strm) {
		if (strm.endsWith(seasonNum)) {
			semesters.add(semester);
		}
	}
}
