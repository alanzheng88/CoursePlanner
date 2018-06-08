package ca.courseplanner.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ca.courseplanner.model.CoursePlanner;

@SuppressWarnings("serial")
public class CourseListPanel extends CoursePlannerPanel {

	private static final int HEIGHT = 90;
	private JList<String> list;

	public CourseListPanel(CoursePlanner coursePlanner) {
		super(coursePlanner);
		setDefaultPanelSize(SIDE_PANEL_WIDTH, Integer.MAX_VALUE);
		add(makeHeader("Course List", Color.BLUE), BorderLayout.NORTH);
		add(makeBody(SIDE_PANEL_WIDTH, HEIGHT, Color.WHITE), BorderLayout.CENTER);
		subscribeToDropdownBoxUpdate();
		subscribeToListSelectionUpdate();
	}

	@Override
	protected JLabel makeHeader(String headerName, Color color) {
		JLabel header = super.makeHeader(headerName, color);
		setHeaderFont(header);
		return header;
	}

	@Override
	protected JPanel makeBody(int width, int height, Color backgroundColor) {
		JPanel bodyPanel = super.makeBody(width, height, backgroundColor);
		bodyPanel.setLayout(new BorderLayout());
		bodyPanel.add(makeScrollPane(), BorderLayout.CENTER);
		return bodyPanel;
	}

	private void subscribeToDropdownBoxUpdate() {
		getCoursePlanner().addSubjectChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				DefaultListModel<String> listModel = new DefaultListModel<String>();
				Vector<String> courseList = getCoursePlanner().getCourseList();
				for (String course : courseList) {
					listModel.addElement(course);
				}
				list.setModel(listModel); // updates made to listModel
			}
		});
	}

	private void subscribeToListSelectionUpdate() {
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(!e.getValueIsAdjusting()) { // only do something after selecting an item
					String selectedItem = list.getSelectedValue();
					if(selectedItem != null) {	// null occurs when nothing is selected
						String selectedCourse = selectedItem.toString();	// in the form: CMPT125
						handleCourseSelection(selectedCourse);
					}
				}
			}
			
			private void handleCourseSelection(String selectedCourse) {
				getCoursePlanner().updateSelectedCourse(selectedCourse);
			}
		});
	}
	
	private JScrollPane makeScrollPane() {
		list = new JList<String>();
		list.setVisibleRowCount(-1);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setFixedCellWidth(SIDE_PANEL_WIDTH / 3);
		JScrollPane listScroller = new JScrollPane(list);
		return listScroller;
	}
}
