package ca.courseplanner.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ca.courseplanner.model.CourseListFilter.Filter;

/**
 * Class used to primarily set up
 * all functionality to be utilized
 * by the UI including adding and
 * notifying listeners
 * 
 * @author Alan
 *
 */
public class CoursePlanner implements Iterable<CourseFileInfo> {

	private List<CourseFileInfo> courseFileInfo;
	private CourseListFilter filteredCourseList;
	private Subject currentlySelectedSubject;
	private Course currentlySelectedCourse;
	private CourseDetailsProcesser currentlySelectedCourseOffering;
	private List<String> seasonList = new ArrayList<String>();
	private List<String> startingListOfYears = new ArrayList<String>();
	private List<ChangeListener> subjectSelectionListeners = new ArrayList<ChangeListener>();
	private List<ChangeListener> courseSelectionListeners = new ArrayList<ChangeListener>();
	private List<ChangeListener> courseOfferingSelectionListeners = new ArrayList<ChangeListener>();
	private CourseOfferingProcesser springOfferings;
	private CourseOfferingProcesser summerOfferings;
	private CourseOfferingProcesser fallOfferings;
	private int burnabyCampusOfferingCount;
	private int surreyCampusOfferingCount;
	private int vancouverCampusOfferingCount;
	private int otherCampusOfferingCount;

	public CoursePlanner() {
		courseFileInfo = new ArrayList<CourseFileInfo>();
		addSeasonsToList();
		createStartingListOfYears();
	}
	
	private void addSeasonsToList() {
		seasonList.add("SPRING");
		seasonList.add("SUMMER");
		seasonList.add("FALL");
	}
	
	private void createStartingListOfYears() {
		int startingYear = 2000;
		int endingYear = 2010;
		for(int year = startingYear; year <= endingYear; year++) {
			startingListOfYears.add(Integer.toString(year));
		}
	}
	
	public Iterator<String> getStartingListOfYears() {
		return Collections.unmodifiableList(startingListOfYears).iterator();
	}

	public Subject getCurrentlySelectedSubject() {
		return currentlySelectedSubject;
	}

	public Course getCurrentlySelectedCourse() {
		return currentlySelectedCourse;
	}

	public CourseDetailsProcesser getCurrentlySelectedCourseOffering() {
		return currentlySelectedCourseOffering;
	}

	public void addSubjectChangeListener(ChangeListener listener) {
		subjectSelectionListeners.add(listener);
	}

	public void addCourseChangeListener(ChangeListener listener) {
		courseSelectionListeners.add(listener);
	}

	public void addCourseOfferingSelectionListener(ChangeListener listener) {
		courseOfferingSelectionListeners.add(listener);
	}

	public void notifyAllSubjectListeners() {
		notifyListeners(subjectSelectionListeners);
	}

	public void notifyAllCourseListeners() {
		notifyListeners(courseSelectionListeners);
	}

	public void notifyAllCourseOfferingListeners() {
		notifyListeners(courseOfferingSelectionListeners);
	}

	private void notifyListeners(List<ChangeListener> listeners) {
		ChangeEvent event = new ChangeEvent(this);
		for (ChangeListener listener : listeners) {
			listener.stateChanged(event);
		}
	}

	public void readFile(File file) throws FileNotFoundException {
		Scanner fileScan = new Scanner(file);
		ColumnSelector selector = new ColumnSelector(fileScan.nextLine());
		processInformation(fileScan, selector);
	}

	private void processInformation(Scanner fileScan, ColumnSelector selector) {
		while (fileScan.hasNextLine()) {
			String lineToProcess = fileScan.nextLine();
			CourseFileInfo courseInfo = new CourseFileInfo(lineToProcess,
					selector, selector.getColumnListLength());
			courseFileInfo.add(courseInfo);
		}
		filterInitialCourseList();
	}
	
	private void filterInitialCourseList() {
		filteredCourseList = new CourseListFilter(this, Filter.UNDERGRAD); // if Filter.NONE is used then no courses will be initially displayed; changes made here must also be made to check-box
	}


	/**
	 * Used for drop-down box
	 * 
	 * @return vector containing strings
	 */
	public Vector<String> getSubjectList() {
		Vector<String> tempSubjectList = new Vector<String>();
		Iterator<Subject> subjectIterator = filteredCourseList.iterator();
		while (subjectIterator.hasNext()) {
			Subject subject = subjectIterator.next();
			String subjectName = subject.getSubject();
			tempSubjectList.add(subjectName);
		}
		return tempSubjectList;
	}

	/**
	 * Used to filter course when a checkbox is selected
	 * 
	 * @param filter
	 */
	public void updateCourseFilter(Filter filter) {
		filteredCourseList.updateFilter(filter);
	}

	/**
	 * Used to update when client selects a subject
	 * 
	 * @param subjectName
	 */
	public void updateSelectedSubject(String subjectName) {
		this.currentlySelectedSubject = getSubjectFromName(subjectName);
	}

	private Subject getSubjectFromName(String subjectNameToFind) {
		Iterator<Subject> subjectIterator = filteredCourseList.iterator();
		while (subjectIterator.hasNext()) {
			Subject subject = subjectIterator.next();
			String subjectName = subject.getSubject();
			if (subjectName.equals(subjectNameToFind)) {
				return subject;
			}
		}
		assert false;
		return null;
	}

	/**
	 * Used after client selects a subject
	 * 
	 * @return a vector containing strings (used to make a DefaultListModel)
	 */
	public Vector<String> getCourseList() {
		Vector<String> tempCourseList = new Vector<String>();
		if (currentlySelectedSubject != null) {
			String subjectName = currentlySelectedSubject.getSubject();
			Iterator<Course> courseIterator = currentlySelectedSubject.iterator();
			String courseName;
			Course course;
			while (courseIterator.hasNext()) {
				course = courseIterator.next();
				String courseNumber = course.getCatalogNumber();
				courseName = subjectName + " " + courseNumber;
				tempCourseList.add(courseName);
			}
		}
		return tempCourseList;
	}

	/**
	 * Used when client selects a course from the course list
	 * 
	 * @param currentlySelectedCourse
	 */
	public void updateSelectedCourse(String currentlySelectedCourse) {
		this.currentlySelectedCourse = getCourseFromFullCourseName(currentlySelectedCourse);
		processCourseOfferingByYear();
		notifyAllCourseListeners();
	}

	private Course getCourseFromFullCourseName(String currentlySelectedCourse) {
		String subject = currentlySelectedSubject.getSubject();
		String parsedSelectedCourse = currentlySelectedCourse.replace(subject + " ", "");
		Course courseSelected = currentlySelectedSubject.getCourseFromName(parsedSelectedCourse);
		return courseSelected;
	}

	/**
	 * Used after client selects a course and updates 
	 * all offerings for Spring, Summer and Fall
	 */
	public void processCourseOfferingByYear() {
		springOfferings = new CourseOfferingProcesser(currentlySelectedSubject,
				currentlySelectedCourse, getListOfYears(), Semester.getSpring());
		summerOfferings = new CourseOfferingProcesser(currentlySelectedSubject,
				currentlySelectedCourse, getListOfYears(), Semester.getSummer());
		fallOfferings = new CourseOfferingProcesser(currentlySelectedSubject,
				currentlySelectedCourse, getListOfYears(), Semester.getFall());
	}
	
	/**
	 * Used for determining year range for year labels 
	 * and requires that currentlySelectedSubject 
	 * and currentlySelectedCourse be updated beforehand
	 * 
	 * @return an iterator for a list of years
	 */
	public Iterator<String> getListOfYears() {
		List<String> yearList = filteredCourseList.generateListOfYear(
				currentlySelectedSubject, currentlySelectedCourse);
		return Collections.unmodifiableList(yearList).iterator();
	}
	
	/**
	 * Used to get subject and location to put on button 
	 * for courseListPanel
	 * 
	 * @param season
	 * @param year
	 * @return iterator for a list with element each 
	 * consisting of all course details
	 */
	public Iterator<CourseDetailsProcesser> getProcessedCourseDetails(
			String season, String year) {
		CourseOfferingProcesser selected = getSelectedSeasonOffering(season);
		List<CourseDetailsProcesser> briefInfoList = selected.getBriefCourseInfoForYear(year);
		return Collections.unmodifiableList(briefInfoList).iterator();
	}

	private CourseOfferingProcesser getSelectedSeasonOffering(String season) {
		if (season.equals(Semester.getSpring())) {
			return springOfferings;
		} else if (season.equals(Semester.getSummer())) {
			return summerOfferings;
		} else if (season.equals(Semester.getFall())) {
			return fallOfferings;
		} else {
			assert false;
			return null;
		}
	}

	/**
	 * Method is used when client clicks on a courseOffering button which
	 * updates currently selected course offering
	 * 
	 * @param selectedCourseOffering
	 */
	public void updateSelectedCourseOffering(
			CourseDetailsProcesser selectedCourseOffering) {
		currentlySelectedCourseOffering = selectedCourseOffering;
	}

	/**
	 * Used to display season count on season histogram
	 * 
	 * @return a formatted list of integers
	 */
	public int[] getSeasonCountList() {
		List<Integer> integerList = new ArrayList<Integer>();
		final int springCount = getSeasonCount(springOfferings);
		final int summerCount = getSeasonCount(summerOfferings);
		final int fallCount = getSeasonCount(fallOfferings);
		final int springConstant = 0;
		final int summerConstant = 1;
		final int fallConstant = 2;
		addToList(springCount, springConstant, integerList);
		addToList(summerCount, summerConstant, integerList);
		addToList(fallCount, fallConstant, integerList);
		int[] convertedIntList = convertListToIntType(integerList);
		return convertedIntList;
	}

	private int getSeasonCount(CourseOfferingProcesser courseOfferingProcesser) {
		int count = 0;
		Iterator<String> yearIterator = getListOfYears();
		while (yearIterator.hasNext()) {
			String year = yearIterator.next();
			Iterator<Semester> semester = courseOfferingProcesser.getSemesterOfferingsByYear(year);
			count = countSemesters(count, semester);
		}
		return count;
	}

	private int countSemesters(int count, Iterator<Semester> semester) {
		while (semester.hasNext()) {
			semester.next();
			count++;
		}
		return count;
	}

	/**
	 * @return a list of integers formatted to be used to display campus
	 *         offerings on histogram
	 */
	public int[] getCampusOfferingsCountList() {
		resetCampusOfferingCount();
		List<Integer> integerList = new ArrayList<Integer>();
		addToCampusCountFromSeasonOfferings();
		final int burnabyConstant = 0;
		final int surreyConstant = 1;
		final int vancouverConstant = 2;
		final int otherConstant = 3;
		addToList(burnabyCampusOfferingCount, burnabyConstant, integerList);
		addToList(surreyCampusOfferingCount, surreyConstant, integerList);
		addToList(vancouverCampusOfferingCount, vancouverConstant, integerList);
		addToList(otherCampusOfferingCount, otherConstant, integerList);
		int[] convertedIntList = convertListToIntType(integerList);
		return convertedIntList;
	}

	private void resetCampusOfferingCount() {
		burnabyCampusOfferingCount = 0;
		surreyCampusOfferingCount = 0;
		vancouverCampusOfferingCount = 0;
		otherCampusOfferingCount = 0;
	}
	
	private void addToCampusCountFromSeasonOfferings() {
		addFromSpringOfferings();
		addFromSummerOfferings();
		addFromFallOfferings();
	}

	private void addFromSpringOfferings() {
		iterateOverYear(springOfferings);
	}

	private void addFromSummerOfferings() {
		iterateOverYear(fallOfferings);
	}

	private void addFromFallOfferings() {
		iterateOverYear(summerOfferings);
	}

	private void iterateOverYear(CourseOfferingProcesser courseDetailsProcesser) {
		Iterator<String> yearIterator = getListOfYears();
		while (yearIterator.hasNext()) {
			String year = yearIterator.next();
			iterateOverSemester(courseDetailsProcesser, year);
		}
	}

	private void iterateOverSemester(CourseOfferingProcesser courseDetailsProcesser, String year) {
		Iterator<Semester> semesterIterator = courseDetailsProcesser.getSemesterOfferingsByYear(year);
		while (semesterIterator.hasNext()) {
			Semester semester = semesterIterator.next();
			String locationToIncreaseCount = semester.getLocation();
			addToCampusCount(locationToIncreaseCount);
		}
	}

	private void addToCampusCount(String locationToIncreaseCount) {
		if (locationToIncreaseCount.equals("BURNABY")) {
			burnabyCampusOfferingCount++;
		} else if (locationToIncreaseCount.equals("SURREY")) {
			surreyCampusOfferingCount++;
		} else if (locationToIncreaseCount.equals("HRBRCNTR")) {
			vancouverCampusOfferingCount++;
		} else {
			otherCampusOfferingCount++;
		}
	}

	private void addToList(int numberOfTimesToAdd, int constantToAdd,
			List<Integer> intList) {
		for (int count = 0; count < numberOfTimesToAdd; count++) {
			intList.add(constantToAdd);
		}
	}

	private int[] convertListToIntType(List<Integer> integerList) {
		int[] tempIntArr = new int[integerList.size()];
		int position = 0;
		for (Integer integer : integerList) {
			tempIntArr[position] = integer.intValue();
			position++;
		}
		return tempIntArr;
	}

	public void dumpModel() {
		updateCourseFilter(Filter.BOTH);
		iterateOverSubject();
	}

	private void iterateOverSubject() {
		Iterator<Subject> subjectIterator = filteredCourseList.iterator();
		while (subjectIterator.hasNext()) {
			Subject subject = subjectIterator.next();
			iterateOverCourse(subject, subject.getSubject());
		}
	}

	private void iterateOverCourse(Subject subject, String subjectName) {
		Iterator<Course> courseIterator = subject.iterator();
		while (courseIterator.hasNext()) {
			Course course = courseIterator.next();
			System.out.println(subjectName + " " + course.getCatalogNumber());
			iterateOverSemester(course);
		}
	}

	private void iterateOverSemester(Course course) {
		Iterator<Semester> semesterIterator = course.iterator();
		while (semesterIterator.hasNext()) {
			Semester semester = semesterIterator.next();
			String semesterName = semester.getStrm();
			String location = semester.getLocation();
			String instructor = semester.getInstructor();
			System.out.printf("\t%s in %s by %s%n", semesterName, location,
					instructor);
			iterateOverSectionType(semester);
		}
	}

	public void iterateOverSectionType(Semester semester) {
		Iterator<SectionType> sectionTypeIterator = semester.iterator();
		while (sectionTypeIterator.hasNext()) {
			SectionType sectionType = sectionTypeIterator.next();
			String sectionName = sectionType.getSectionType();
			String enrollmentTotal = sectionType.getEnrollmentTotal();
			String enrollmentCap = sectionType.getEnrollmentCap();
			System.out.printf("\t\tType=%s, Enrollment=%s/%s%n", sectionName,
					enrollmentTotal, enrollmentCap);
		}
	}

	public Iterator<CourseFileInfo> iterator() {
		return Collections.unmodifiableList(courseFileInfo).iterator();
	}

	public Iterator<String> getSeasonIterator() {
		return Collections.unmodifiableList(seasonList).iterator();
	}

	public String getFullCourseName() {
		String currentSubject = currentlySelectedSubject.getSubject();
		String currentCourse = currentlySelectedCourse.getCatalogNumber();
		return currentSubject + " " + currentCourse;	
	}
}
