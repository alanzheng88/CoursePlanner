package ca.courseplanner.ui;

import ca.courseplanner.model.CoursePlanner;

/**
 * Class passes model to UI to
 * be used
 * 
 * @author Alan
 *
 */
public class Main {

	public static void main(String[] args) {
		CoursePlanner coursePlanner = new CoursePlanner();
		CoursePlannerUI ui = new CoursePlannerUI(coursePlanner);
		ui.runProgram();
	}
}