package ca.courseplanner.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ca.courseplanner.model.CourseDetailsProcesser;
import ca.courseplanner.model.CoursePlanner;

/**
 * Class draws all courseOfferings
 * in the form of a grid grouped
 * by both year and season for a 
 * particular subject and course.
 * An empty grid is shown when
 * drop-down box is updated and
 * a populated grid after course 
 * selection.
 * 
 * @author Alan
 *
 */
@SuppressWarnings("serial")
public class CourseOfferingPanel extends CoursePlannerPanel {

	private final int YEAR_AXIS_SPACING_X = 15;
	private final int SEASON_AXIS_SPACING_X = 40;
	private final int SEASON_AXIS_SPACING_Y = 5;
	private final int HEIGHT = 500;
	private final int WIDTH = 500;
	private JPanel bodyPanel;

	public CourseOfferingPanel(CoursePlanner coursePlanner) {
		super(coursePlanner);
		setDefaultPanelSize(WIDTH, HEIGHT);
		add(makeHeader("Course Offerings By Semester", Color.BLUE),
				BorderLayout.NORTH);
		add(makeBody(Integer.MAX_VALUE, Integer.MAX_VALUE, Color.white),
				BorderLayout.CENTER);
		subscribeToCourseUpdate();
		subscribeToDropdownBoxUpdate();
	}

	@Override
	protected JLabel makeHeader(String headerName, Color color) {
		JLabel header = super.makeHeader(headerName, color);
		setHeaderFont(header);
		return header;
	}
	
	@Override
	protected JPanel makeBody(int width, int height, Color backgroundColor) {
		bodyPanel = super.makeBody(width, height, backgroundColor);
		bodyPanel.setLayout(new BorderLayout());
		JLabel informationLabel = makeLabel("Use a filter to select a course.");
		informationLabel.setHorizontalAlignment(JLabel.CENTER);
		informationLabel.setVerticalAlignment(JLabel.CENTER);
		bodyPanel.add(informationLabel);
		return bodyPanel;
	}
	
	private void subscribeToDropdownBoxUpdate() {
		getCoursePlanner().addSubjectChangeListener(new ChangeListener() {
			
			public void stateChanged(ChangeEvent e) {
				removeUpdateUi(bodyPanel);
				GridBagConstraints c = setUpInitialLayout();
				makeSeasonAxis(c);
				Iterator<String> startingListOfYears = getCoursePlanner().getStartingListOfYears();
				makeYearAxis(c, startingListOfYears);
				makeEmptyCourseGrid(c);
				updateUi(bodyPanel);
			}
			
		});
	}
	
	private void makeEmptyCourseGrid(GridBagConstraints c) {
		setCourseConstraints(c);
		Iterator<String> seasonIterator = getCoursePlanner().getSeasonIterator();
		c.gridx = 1;
		c.gridy = 1;
		while(seasonIterator.hasNext()) {
			seasonIterator.next();
			c.gridy = 1;
			Iterator<String> startingListOfYears = getCoursePlanner().getStartingListOfYears();
			while(startingListOfYears.hasNext()) {
				startingListOfYears.next();
				JPanel emptyPanel = makeEmptyPanel();
				bodyPanel.add(emptyPanel, c);
				c.gridy++;
			}
			c.gridx++;
		}
	}

	private JPanel makeEmptyPanel() {
		JPanel emptyButtonPanel = new JPanel();
		emptyButtonPanel.setLayout(new BoxLayout(emptyButtonPanel, BoxLayout.PAGE_AXIS));
		emptyButtonPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.black, Color.black));
		return emptyButtonPanel;
	}
	
	private void subscribeToCourseUpdate() {
		getCoursePlanner().addCourseChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
//				testProcessCourseOffering();
				makeGrid();
			}
		});
	}

	private void makeGrid() {
		removeUpdateUi(bodyPanel);
		GridBagConstraints c = setUpInitialLayout();
		makeSeasonAxis(c);
		Iterator<String> yearIterator = getCoursePlanner().getListOfYears();
		makeYearAxis(c, yearIterator);
		addCourses(c);
		updateUi(bodyPanel);
	}

	private GridBagConstraints setUpInitialLayout() {
		bodyPanel.setLayout(new GridBagLayout());
		bodyPanel.setBackground(Color.white);
		GridBagConstraints c = new GridBagConstraints();
		return c;
	}

	private void makeSeasonAxis(GridBagConstraints c) {
		setSeasonAxisConstraints(c);
		c.gridx = 1;
		c.gridy = 0;
		addSeasonsToPanel(c);
	}
	
	private void setSeasonAxisConstraints(GridBagConstraints c) {
		c.ipadx = SEASON_AXIS_SPACING_X;
		c.ipady = SEASON_AXIS_SPACING_Y;
		c.weightx = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTH;
	}
	
	private void addSeasonsToPanel(GridBagConstraints c) {
		Iterator<String> seasonList = getCoursePlanner().getSeasonIterator();
		while(seasonList.hasNext()) {
			String season = seasonList.next();
			bodyPanel.add(makeLabel(season), c);
			c.gridx++;
		}
	}

	private void makeYearAxis(GridBagConstraints c, Iterator<String> yearList) {
		setYearAxisConstraints(c);
		Iterator<String> yearIterator = yearList;
		c.gridx = 0;
		c.gridy = 1;
		while(yearIterator.hasNext()) {
			String year = yearIterator.next();
			bodyPanel.add(makeLabel(year), c);
			c.gridy++;
		}
	}
	
	private void setYearAxisConstraints(GridBagConstraints c) {
		c.ipadx = YEAR_AXIS_SPACING_X;
		c.weightx = 0;
		c.weighty = 1;
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.NONE;
	}
	
	private void addCourses(GridBagConstraints c) {
		setCourseConstraints(c);
		CoursePlanner coursePlanner = getCoursePlanner();
		Iterator<String> seasonIterator = coursePlanner.getSeasonIterator();
		c.gridx = 1;
		c.gridy = 1;
		while(seasonIterator.hasNext()) {
			String season = seasonIterator.next();
			iterateOverYear(c, coursePlanner, season);
		}
	}
	
	private void setCourseConstraints(GridBagConstraints c) {
		c.ipadx = SEASON_AXIS_SPACING_X;
		c.ipady = 2;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
	}

	private void iterateOverYear(GridBagConstraints c, CoursePlanner coursePlanner, String season) {
		Iterator<String> yearIterator = coursePlanner.getListOfYears();
		c.gridy = 1;
		while(yearIterator.hasNext()) {
			String year = yearIterator.next();
//			System.out.println("\t" + season + " " + year);
			Iterator<CourseDetailsProcesser> courseInfoIterator = coursePlanner.getProcessedCourseDetails(season, year);
			bodyPanel.add(makeButtonPanel(courseInfoIterator), c);
			c.gridy++;
		}
		c.gridx++;
	}
	
	private JPanel makeButtonPanel(Iterator<CourseDetailsProcesser> courseInfoIterator) {
		JPanel buttonPanel = makeEmptyPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
		while(courseInfoIterator.hasNext()) {
			CourseDetailsProcesser courseDetails = courseInfoIterator.next();
			String courseInfo = courseDetails.getFullCourseNameAndLocation();
			buttonPanel.add(makeRowPanel(courseDetails, courseInfo));
			buttonPanel.add(Box.createVerticalGlue());
		}
		return buttonPanel;
	}

	// use BorderLayout to stretch button
	private JPanel makeRowPanel(CourseDetailsProcesser courseDetails, String courseInfo) {
		JPanel rowPanel = new JPanel();
		rowPanel.setLayout(new BorderLayout());
		if(courseInfo.length() != 0) {
//			System.out.println("\t" + courseInfo);
			JButton courseOfferingButton = makeButton(courseInfo);
			addListener(courseDetails, courseOfferingButton);
			rowPanel.add(courseOfferingButton, BorderLayout.NORTH);
		}
		return rowPanel;
	}

	private void addListener(final CourseDetailsProcesser courseDetails, JButton makeCourseOfferingButton) {
		makeCourseOfferingButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				CoursePlanner coursePlanner = getCoursePlanner();
				coursePlanner.updateSelectedCourseOffering(courseDetails);
				coursePlanner.notifyAllCourseOfferingListeners();
			}
			
		});
	}
	
	@Override
	protected JButton makeButton(String text) {
		JButton button = super.makeButton(text);
		return button;		
	}

//	private void testProcessCourseOffering() {
//		CoursePlanner coursePlanner = getCoursePlanner();
//		Iterator<String> yearIterator = coursePlanner.getListOfYears();
//		System.out.println("selectedCourse: "
//				+ coursePlanner.getCurrentlySelectedCourse());
//
//		coursePlanner.processCourseOfferingByYear();
//		
//		while (yearIterator.hasNext()) {
//			String year = yearIterator.next();
//			Iterator<String> briefInfoList = coursePlanner
//					.getFullCourseNameAndLocation("SPRING", year);
//			while (briefInfoList.hasNext()) {
//				System.out.print("testing the following line: ");
//				System.out.println(briefInfoList.next());
//			}
//
//			Iterator<Semester> semesterIterator = getCoursePlanner().getCourseOfferingByYear("SPRING", year);
//			while (semesterIterator.hasNext()) {
//				System.out.print(year + " ");
//				Semester semester = semesterIterator.next();
//				String semesterName = semester.getStrm();
//				String location = semester.getLocation();
//				String instructor = semester.getInstructor();
//				System.out.printf("\t%s in %s by %s%n", semesterName, location,
//						instructor);
//				coursePlanner.iterateOverSectionType(semester);
//			}
//
//		}
//
//		System.out.println("\n");
//		System.out.println("finished");
//	}
}
