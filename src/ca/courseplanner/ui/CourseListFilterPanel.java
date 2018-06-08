package ca.courseplanner.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ca.courseplanner.model.CoursePlanner;
import ca.courseplanner.model.CourseListFilter.Filter;

/**
 * Class draws drop-down box,
 * checkboxes and update button.
 * It also makes a call to CoursePlanner's
 * notify method when the update button
 * is pressed to update all listeners
 * which have subscribed to drop-down box
 * updates
 * 
 * @author Alan
 *
 */
@SuppressWarnings("serial")
public class CourseListFilterPanel extends CoursePlannerPanel {

	private final int DROPDOWN_BOX_LEFT_SPACING = 3;
	private final String BUTTON_FONT = "Calibri";
	private final int BUTTON_FONT_SIZE = 12;
	private final int HEIGHT = 130;
	private Vector<String> options;
	private JComboBox<String> dropdownBox;
	private List<JCheckBox> checkBoxList = new ArrayList<JCheckBox>();

	public CourseListFilterPanel(CoursePlanner coursePlanner) {
		super(coursePlanner);
		setDefaultPanelSize(SIDE_PANEL_WIDTH, HEIGHT);
		setBorder(BorderFactory.createEmptyBorder(0, 0,
				CoursePlannerUI.getSidebarVerticalGap(), 0));
		add(makeHeader("Course List Filter", Color.BLUE), BorderLayout.NORTH);
		add(makeBody(SIDE_PANEL_WIDTH, HEIGHT, null), BorderLayout.CENTER);
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
		CoursePlanner coursePlanner = getCoursePlanner();
		options = coursePlanner.getSubjectList();
		bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.PAGE_AXIS));
		bodyPanel.add(makeDropdownBoxLayer());
		bodyPanel.add(makeUndergradLayer());
		bodyPanel.add(makeGradLayer());
		bodyPanel.add(makeButtonPanel("Update Course List"));
		bodyPanel.add(Box.createVerticalGlue());
		return bodyPanel;
	}

	protected JPanel makeButtonPanel(String text) {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(makeButton(text));
		return buttonPanel;
	}

	@Override
	protected JButton makeButton(String text) {
		JButton button = super.makeButton(text);
		button.setFont(new Font(BUTTON_FONT, Font.BOLD, BUTTON_FONT_SIZE));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getCoursePlanner().notifyAllSubjectListeners();
			}
		});
		return button;
	}

	private JPanel makeDropdownBoxLayer() {
		JPanel dropdownBoxPanel = new JPanel();
		dropdownBoxPanel.setLayout(new BoxLayout(dropdownBoxPanel,
				BoxLayout.LINE_AXIS));
		dropdownBoxPanel.add(makeLabel("Department"));
		dropdownBoxPanel.add(makeDropdownBox());
		return dropdownBoxPanel;
	}

	private JComboBox<String> makeDropdownBox() {
		dropdownBox = new JComboBox<String>(options);
		dropdownBox.setBorder(BorderFactory.createEmptyBorder(0, DROPDOWN_BOX_LEFT_SPACING, 0, 0));
		dropdownBox.setSelectedIndex(-1);	// -1 indicates no selection
		dropdownBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<String> selectedBox = (JComboBox<String>) e.getSource();
				String subject = (String) selectedBox.getSelectedItem();
				getCoursePlanner().updateSelectedSubject(subject);
			}
		});
		return dropdownBox;
	}

	private JPanel makeUndergradLayer() {
		JPanel undergradPanel = new JPanel();
		undergradPanel.setLayout(new BoxLayout(undergradPanel,
				BoxLayout.LINE_AXIS));
		undergradPanel.add(makeCheckBox("Include undergrad courses", true));
		undergradPanel.add(Box.createHorizontalGlue());
		return undergradPanel;
	}

	private JPanel makeGradLayer() {
		JPanel gradPanel = new JPanel();
		gradPanel.setLayout(new BoxLayout(gradPanel, BoxLayout.LINE_AXIS));
		gradPanel.add(makeCheckBox("Include grad courses", false));
		gradPanel.add(Box.createHorizontalGlue());
		return gradPanel;
	}

	private JCheckBox makeCheckBox(String label, boolean checked) {
		JCheckBox checkBox = new JCheckBox(label, checked);
		checkBoxList.add(checkBox);
		checkBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				JCheckBox undergrad = checkBoxList.get(0);
				JCheckBox grad = checkBoxList.get(1);
				CoursePlanner coursePlanner = getCoursePlanner();
				if (undergrad.isSelected() && grad.isSelected()) {
					System.out.println("both selected");
					coursePlanner.updateCourseFilter(Filter.BOTH);
				} else if (undergrad.isSelected()) {
					System.out.println("undergrad selected");
					coursePlanner.updateCourseFilter(Filter.UNDERGRAD);
				} else if (grad.isSelected()) {
					System.out.println("grad selected");
					coursePlanner.updateCourseFilter(Filter.GRAD);
				} else {
					System.out.println("none selected");
					coursePlanner.updateCourseFilter(Filter.NONE);
				}
			}
		});
		return checkBox;
	}
}
