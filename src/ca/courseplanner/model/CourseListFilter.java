package ca.courseplanner.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Class adds all possible subjects to list and passes courses corresponding to
 * each subject to the Subject class
 * 
 * @author alanzheng
 * 
 */
public class CourseListFilter implements Iterable<Subject> {

	private List<Subject> subjects;
	private Filter currentFilter;
	private CoursePlanner coursePlanner;
	private boolean subjectInitialized = false;
	private final int MIN_GRAD_COURSE_NUMBER = 500;

	public enum Filter {
		GRAD, UNDERGRAD, BOTH, NONE
	}

	// need coursePlanner to hold access to iterator since it doesn't reset
	public CourseListFilter(CoursePlanner coursePlanner, Filter filter) {
		subjects = new ArrayList<Subject>();
		currentFilter = filter;
		this.coursePlanner = coursePlanner;
		updateFilter(currentFilter);
	}

	public void updateFilter(Filter filter) {
		boolean isCurrentFilter = currentFilter.equals(filter);
		if (!subjectInitialized) { // handles initial case where subject is not initialized
			addSubjectsToListAndSort(coursePlanner.iterator());
			subjectInitialized = true;
		} else if (!isCurrentFilter) {
			clearCourses();
			updateCurrentFilter(filter);
			addSubjectsToListAndSort(coursePlanner.iterator());
		}
	}

	private void addSubjectsToListAndSort(
			Iterator<CourseFileInfo> courseFileInfoIterator) {
		while (courseFileInfoIterator.hasNext()) {
			CourseFileInfo courseFileInfo = (CourseFileInfo) courseFileInfoIterator.next();
			Row rowInfo = courseFileInfo.getInfo();
			checkConstraintsAndAdd(rowInfo);
		}
		sortEachSubject();
	}

	private void checkConstraintsAndAdd(Row rowInfo) {
		String subjectName = rowInfo.getSubject();
		Subject currentSubject = new Subject(subjectName);
		String courseName = rowInfo.getCatalogNumber();
		addNewSubjects(currentSubject);
		if (!isConstraint(courseName)) {
			addCourseToSubject(currentSubject, rowInfo);
		}
	}

	private void addNewSubjects(Subject currentSubject) {
		if (!subjects.contains(currentSubject)) {
			subjects.add(currentSubject);
		}
	}

	private boolean isConstraint(String courseName) {
		boolean isRestriction = false;
		switch (currentFilter) {
		case BOTH:
			isRestriction = false;
			break;
		case GRAD:
			isRestriction = !isGradCourse(courseName);
			break;
		case UNDERGRAD:
			isRestriction = isGradCourse(courseName);
			break;
		case NONE:
			isRestriction = true;
			break;
		default:
			assert false;
		}
		return isRestriction;
	}

	// assume all course numbers are under 1000
	private boolean isGradCourse(String courseName) {
		assert courseName.length() >= 3;
		String threeCharacterCourseNumber = courseName.substring(0, 3);
		try {
			int courseNumberToCompare = Integer
					.parseInt(threeCharacterCourseNumber);
			return courseNumberToCompare >= MIN_GRAD_COURSE_NUMBER;
		} catch (NumberFormatException e) {
			// letters in subjectName for the first three characters indicates
			// that it is an undergrad course
			return false;
		}
	}

	private void addCourseToSubject(Subject tempSubject, Row rowInfo) {
		Course course = new Course(rowInfo);
		Subject subject = getSubjectFromList(tempSubject);
		subject.addCourse(course);
	}

	private Subject getSubjectFromList(Subject tempSubject) {
		int subjectPosition = subjects.indexOf(tempSubject);
		assert subjectPosition != -1;
		Subject subject = subjects.get(subjectPosition);
		return subject;
	}

	private void sortEachSubject() {
		Iterator<Subject> iterator = iterator();
		while (iterator.hasNext()) {
			Subject subject = iterator.next();
			subject.sortCoursesByCatalogNumber();
		}
	}

	public List<String> generateListOfYear(Subject subject, Course course) {
		List<String> yearList = new ArrayList<String>();
		Subject selectedSubject = getSubjectFromList(subject);
		String minYear = selectedSubject.getMinYearFromSelectedCourse(course);
		String maxYear = selectedSubject.getMaxYearFromSelectedCourse(course);
		int startYear = Integer.parseInt(minYear);
		int endYear = Integer.parseInt(maxYear);
		assert startYear <= endYear;
		addRangeOfYearsToList(yearList, startYear, endYear);
		return yearList;
	}

	private void addRangeOfYearsToList(List<String> yearList, int startYear,
			int endYear) {
		for (int i = startYear; i <= endYear; i++) {
			String yearToAdd = String.valueOf(i);
			yearList.add(yearToAdd);
		}
	}

	private void clearCourses() {
		for (Subject subject : subjects) {
			subject.clearCourses();
		}
	}

	private void updateCurrentFilter(Filter filter) {
		currentFilter = filter;
	}

	@Override
	public Iterator<Subject> iterator() {
		return Collections.unmodifiableList(subjects).iterator();
	}

	@Override
	public String toString() {
		return getClass().getName() 
				+ "[" 
				+ subjects.toString() 
				+ "]";
	}
}
