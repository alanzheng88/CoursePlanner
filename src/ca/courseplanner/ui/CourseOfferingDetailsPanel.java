package ca.courseplanner.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ca.courseplanner.model.CourseDetailsProcesser;
import ca.courseplanner.model.CoursePlanner;

/**
 * Class draws a box consisting
 * of all information for a particular
 * course when that course is selected 
 * from courseOfferingPanel.
 * It also updates initial display
 * when a subject is selected from
 * drop-down box.
 * 
 * @author Alan
 *
 */
@SuppressWarnings("serial")
public class CourseOfferingDetailsPanel extends CoursePlannerPanel {

	private static final int LEFT_SIDE_RIGHT_OFFSET = 18;
	private static final int ENROLLMENT_LEFT_OFFSET = 20;
	private JPanel bodyPanel;

	public CourseOfferingDetailsPanel(CoursePlanner coursePlanner) {
		super(coursePlanner);
		setDefaultPanelSize(SIDE_PANEL_WIDTH, Integer.MAX_VALUE);
		add(makeHeader("Details of Course Offering", Color.BLUE), BorderLayout.NORTH);
		add(makeBody(SIDE_PANEL_WIDTH, Integer.MAX_VALUE, null), BorderLayout.CENTER);
		subscribeToDropdownBoxOffering();
		subscribeToCourseOffering();
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
		setupLeftAndRightSide();
		return bodyPanel;
	}
	
	private void setupLeftAndRightSide() {
		bodyPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		makeInitialLeftSide(c);
		makeInitialRightSide(" ", " ", " ", " ", c);
		createBottomGlue(c);
	}

	private void subscribeToDropdownBoxOffering() {
		getCoursePlanner().addSubjectChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e)	{
				removeUpdateUi(bodyPanel);
				setupLeftAndRightSide();
				updateUi(bodyPanel);
			}
		});
	}

	private void subscribeToCourseOffering() {
		getCoursePlanner().addCourseOfferingSelectionListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				removeUpdateUi(bodyPanel);
				CourseDetailsProcesser courseDetails = getCoursePlanner().getCurrentlySelectedCourseOffering();
				displayCourseOfferingDetails(courseDetails);
				updateUi(bodyPanel);
			}
		});
	}

	private void displayCourseOfferingDetails(CourseDetailsProcesser courseDetails) {
		String fullCourseName = courseDetails.getFullCourseName();
		String location = courseDetails.getLocation();
		String semester = courseDetails.getStrm();
		String instructors = courseDetails.getInstructor();
		Iterator<String> sectionIterator = courseDetails.getSectionTypes();
		Iterator<String> enrollmentIterator = courseDetails.getEnrollments();
		if(instructors.length() == 0) {
			instructors = " ";
		}
		bodyPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		makeLeftSide(sectionIterator, c);
		makeRightSide(fullCourseName, location, semester, instructors,
				enrollmentIterator, c);
		createBottomGlue(c);
	}

	private void makeLeftSide(Iterator<String> sectionIterator, GridBagConstraints c) {
		makeInitialLeftSide(c);
		bodyPanel.add(makeBoldLabels("Section Type"), c);
		addSectionTypes(sectionIterator, c);
	}

	private void makeInitialLeftSide(GridBagConstraints c) {
		setLeftSideConstraints(c);
		c.gridx = 0;
		c.gridy = 0;
		bodyPanel.add(makeBoldLabels("Course Name:"), c);
		c.gridx = 0;
		c.gridy = 1;
		bodyPanel.add(makeBoldLabels("Location:"), c);
		c.gridx = 0;
		c.gridy = 2;
		bodyPanel.add(makeBoldLabels("Semester:"), c);
		c.gridx = 0;
		c.gridy = 3;
		bodyPanel.add(makeBoldLabels("Instructors:"), c);
		c.gridx = 0;
		c.gridy = 4;
	}
	
	private JLabel makeBoldLabels(String text) {
		JLabel titleLabel = super.makeLabel(text);
		titleLabel.setFont(setFont(Font.BOLD));
		return titleLabel;
	}

	private void setLeftSideConstraints(GridBagConstraints c) {
		c.weightx = 0;
		c.weighty = 0;
		c.ipadx = 0;
		c.ipady = 0;
		c.insets = new Insets(0, 0, 0, LEFT_SIDE_RIGHT_OFFSET);
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.NORTHWEST;
	}
	
	private void addSectionTypes(Iterator<String> sectionIterator, GridBagConstraints c) {
		c.gridx = 0;
		c.gridy = 5;
		while(sectionIterator.hasNext()) {
			String section = sectionIterator.next();
			bodyPanel.add(makeBoldLabels(section), c);
			c.gridy++;
		}
	}
	
	private void makeRightSide(String fullCourseName, String location,
			String semester, String instructors,
			Iterator<String> enrollmentIterator, GridBagConstraints c) {
		makeInitialRightSide(fullCourseName, location, semester, instructors, c);
		JLabel enrollmentLabel = makeBoldLabels("Enrollment (filled/cap)");
		makeEnrollmentLabelLeftSpacing(enrollmentLabel);
		bodyPanel.add(enrollmentLabel, c);
		addEnrollmentTypes(enrollmentIterator, c);
	}

	private void addEnrollmentTypes(Iterator<String> enrollmentIterator, GridBagConstraints c) {
		c.gridx = 1;
		c.gridy = 5;
		while(enrollmentIterator.hasNext()) {
			String enrollment = enrollmentIterator.next();
			JLabel enrollmentLabel = makeBoldLabels(enrollment);
			makeEnrollmentLabelLeftSpacing(enrollmentLabel);
			bodyPanel.add(enrollmentLabel, c);
			c.gridy++;
		}
	}
	
	private void makeInitialRightSide(String fullCourseName, String location,
			String semester, String instructors, GridBagConstraints c) {
		setRightSideConstraints(c);
		c.gridx = 1;
		c.gridy = 0;
		bodyPanel.add(makePlainLabels(fullCourseName), c);
		c.gridx = 1;
		c.gridy = 1;
		bodyPanel.add(makePlainLabels(location), c);
		c.gridx = 1;
		c.gridy = 2;
		bodyPanel.add(makePlainLabels(semester), c);
		c.gridx = 1;
		c.gridy = 3;
		bodyPanel.add(makePlainLabels(instructors), c);
		c.gridx = 1;
		c.gridy = 4;
	}
	
	private JLabel makePlainLabels(String text) {
		JLabel descriptionLabel = super.makeLabel(text);
		descriptionLabel.setFont(setFont(Font.PLAIN));
		descriptionLabel.setBorder(BorderFactory.createLineBorder(Color.cyan));
		return descriptionLabel;
	}
	
	private void setRightSideConstraints(GridBagConstraints c) {
		c.weightx = 0;
		c.weighty = 0;
		c.ipadx = 0;
		c.ipady = 3;
		c.insets = new Insets(0, 0, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.EAST;
	}
	
	private void makeEnrollmentLabelLeftSpacing(JLabel enrollmentLabel) {
		enrollmentLabel.setBorder(BorderFactory.createEmptyBorder(0, ENROLLMENT_LEFT_OFFSET, 0, 0));
	}
	
	// pushes all components above it up
	private void createBottomGlue(GridBagConstraints c) {
		c.gridx = 0;
		c.gridwidth = 2;
		c.gridy++;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		bodyPanel.add(Box.createGlue(), c);
	}
}
