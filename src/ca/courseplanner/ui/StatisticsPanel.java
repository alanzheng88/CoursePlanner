package ca.courseplanner.ui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ca.courseplanner.model.CoursePlanner;
import ca.courseplanner.model.Histogram;

/**
 * Class used to construct
 * and update histogram for
 * semester offerings count 
 * and locations count 
 * @author alanzheng
 *
 */
@SuppressWarnings("serial")
public class StatisticsPanel extends CoursePlannerPanel {

	private static final int NUM_OF_CAMPUS_OFFERING_BARS = 4;
	private static final int NUMBER_OF_SEMESTER_BARS = 3;
	private static final int STATISTICS_PANEL_HEIGHT = 410;
	private static final int HISTOGRAM_HEIGHT = 130;
	private JPanel bodyPanel;

	public StatisticsPanel(CoursePlanner coursePlanner) {
		super(coursePlanner);
		setDefaultPanelSize(SIDE_PANEL_WIDTH, STATISTICS_PANEL_HEIGHT);
		add(makeHeader("Statistics", Color.BLUE), BorderLayout.NORTH);
		add(makeBody(SIDE_PANEL_WIDTH, STATISTICS_PANEL_HEIGHT, null), BorderLayout.CENTER);
		setBorder(BorderFactory.createEmptyBorder(0, 0, CoursePlannerUI.getSidebarVerticalGap(), 0));
		subscribeToDropdownBoxUpdate();
		subscribeToCourseUpdate();
		subscribeToCourseOfferingUpdate();
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
		bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.PAGE_AXIS));
		makeInitialHistogramPanel(null, bodyPanel);
		return bodyPanel;
	}

	private void makeInitialHistogramPanel(Histogram histogram, JPanel bodyPanel) {
		makeSemesterOfferingHistogram(histogram, bodyPanel);
		makeCampusOfferingHistogram(histogram, bodyPanel);
	}

	private void makeSemesterOfferingHistogram(Histogram histogram, JPanel bodyPanel) {
		String legend = "(0=Spring, 1=Summer, 2=Fall)";
		bodyPanel.add(new HistogramPanel(histogram, "Semester Offerings", legend, SIDE_PANEL_WIDTH, HISTOGRAM_HEIGHT));
	}
	
	private void makeCampusOfferingHistogram(Histogram histogram, JPanel bodyPanel) {
		String legend = "(0=Bby, 1=Sry, 2=Van, 3=Other)";
		bodyPanel.add(new HistogramPanel(histogram, "Campus Offerings", legend, SIDE_PANEL_WIDTH, HISTOGRAM_HEIGHT));
	}
	
	private void subscribeToDropdownBoxUpdate() {
		getCoursePlanner().addSubjectChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				removeUpdateUi(bodyPanel);
				bodyPanel.add(makeLabel("Course: "));
				makeInitialHistogramPanel(null, bodyPanel);
				updateUi(bodyPanel);
			}
		});
	}
	
	private void subscribeToCourseUpdate() {
		getCoursePlanner().addCourseChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				update();
			}
		});
	}
	
	private void subscribeToCourseOfferingUpdate() {
		getCoursePlanner().addCourseOfferingSelectionListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				update();
			}
		});
	}

	private void update() {
		removeUpdateUi(bodyPanel);
		displayCourseName();
		displayHistogram();
		updateUi(bodyPanel);
	}
	
	protected void displayCourseName() {
		CoursePlanner coursePlanner = getCoursePlanner();
		String selectedCourse = coursePlanner.getFullCourseName();
		bodyPanel.add(makeLabel("Course: " + selectedCourse));
	}

	private void displayHistogram() {
		CoursePlanner coursePlanner = getCoursePlanner();
		int[] semestersCounts = coursePlanner.getSeasonCountList();
		Histogram semesterOfferingHistogram = new Histogram(semestersCounts, NUMBER_OF_SEMESTER_BARS);
		makeSemesterOfferingHistogram(semesterOfferingHistogram, bodyPanel);
		int[] campusCounts = coursePlanner.getCampusOfferingsCountList();
		Histogram campusOfferingHistogram = new Histogram(campusCounts, NUM_OF_CAMPUS_OFFERING_BARS);
		makeCampusOfferingHistogram(campusOfferingHistogram, bodyPanel);
	}
}
