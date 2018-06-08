package ca.courseplanner.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.PatternSyntaxException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ca.courseplanner.model.CoursePlanner;

/**
 * Class dumps model and creates 
 * initial layout of course planner
 * 
 * @author Alan
 *
 */
public class CoursePlannerUI {

	private static final int WIDTH = 1200;
	private static final int HEIGHT = 660;
	private static final int TOP_GAP = 2;
	private static final int LEFT_GAP = 2;
	private static final int BOTTOM_GAP = 2;
	private static final int RIGHT_GAP = 2;
	private static final int SIDEBAR_VERTICAL_GAP = 3;
	private final String fileName = "data//course_data_2014.csv";
	private CoursePlanner coursePlanner;

	public CoursePlannerUI(CoursePlanner coursePlanner) {
		this.coursePlanner = coursePlanner;
	}

	public void runProgram() {
		addCourses();
		displayCoursePlanner();
	}

	private void addCourses() {
		File file = new File(fileName);
		try {
			coursePlanner.readFile(file);
//			coursePlanner.dumpModel();
		} catch (FileNotFoundException | PatternSyntaxException e) {
			String absolutePath = file.getAbsolutePath();
			String message = "Data file (" + absolutePath + ") not found";
			displayMessageBoxAndQuit(message);
		} 
	}

	private void displayMessageBoxAndQuit(String message) {
		JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
		System.exit(1);
	}

	private void displayCoursePlanner() {
		JFrame frame = new JFrame();
		frame.setTitle("FAS Course Planner");
		frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		frame.setLayout(new BorderLayout());
		frame.add(makeLeftSideBar(), BorderLayout.WEST);
		frame.add(makeCenterPanel(), BorderLayout.CENTER);
		frame.add(makeRightSideBar(), BorderLayout.EAST);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private JPanel makeLeftSideBar() {
		JPanel leftPanel = new JPanel();
		leftPanel.setBorder(BorderFactory.createEmptyBorder(
				TOP_GAP, LEFT_GAP, BOTTOM_GAP, RIGHT_GAP));
		leftPanel.setLayout(new BorderLayout());
		leftPanel.add(new CourseListFilterPanel(coursePlanner), BorderLayout.NORTH);
		leftPanel.add(new CourseListPanel(coursePlanner), BorderLayout.CENTER);
		return leftPanel;
	}

	private JPanel makeCenterPanel() {
		JPanel centerPanel = new JPanel();
		centerPanel.setBorder(BorderFactory.createEmptyBorder(
				TOP_GAP, LEFT_GAP, BOTTOM_GAP, RIGHT_GAP));
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));
		centerPanel.add(new CourseOfferingPanel(coursePlanner));
		return centerPanel;
	}

	private JPanel makeRightSideBar() {
		JPanel rightPanel = new JPanel();
		rightPanel.setBorder(BorderFactory.createEmptyBorder(
				TOP_GAP, LEFT_GAP, BOTTOM_GAP, RIGHT_GAP));
		rightPanel.setLayout(new BorderLayout());
		rightPanel.add(new StatisticsPanel(coursePlanner), BorderLayout.NORTH);
		rightPanel.add(new CourseOfferingDetailsPanel(coursePlanner), BorderLayout.CENTER);
		return rightPanel;
	}
	
	public static int getSidebarVerticalGap() {
		return SIDEBAR_VERTICAL_GAP;
	}
}
