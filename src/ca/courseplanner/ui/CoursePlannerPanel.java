package ca.courseplanner.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import ca.courseplanner.model.CoursePlanner;

/**
 * Used to design common basic layout
 * for all derived classes
 * 
 * @author Alan
 *
 */
@SuppressWarnings("serial")
public abstract class CoursePlannerPanel extends JPanel {

	private CoursePlanner coursePlanner;
	private final int headerSize = 15;
	private final int DETAILS_FONT_SIZE = 14;
	protected static final int SIDE_PANEL_WIDTH = 270;

	public CoursePlannerPanel(CoursePlanner coursePlanner) {
		this.coursePlanner = coursePlanner;
		setLayout(new BorderLayout());
	}

	protected void setDefaultPanelSize(int width, int height) {
		setPreferredSize(new Dimension(width, height));
	}

	protected JLabel makeHeader(String headerName, Color fontColor) {
		JLabel header = new JLabel();
		header.setForeground(fontColor);
		header.setText(headerName);
		return header;
	}

	protected JPanel makeBody(int width, int height, Color backgroundColor) {
		JPanel bodyPanel = new JPanel();
		setBackgroundColor(backgroundColor, bodyPanel);
		bodyPanel.setPreferredSize(new Dimension(width, height));
		bodyPanel.setBorder(BorderFactory.createBevelBorder(
				BevelBorder.LOWERED, Color.black, Color.gray));
		return bodyPanel;
	}

	protected JButton makeButton(String text) {
		JButton button = new JButton(text);
		return button;
	}

	protected JLabel makeLabel(String text) {
		JLabel label = new JLabel(text);
		return label;
	}

	protected CoursePlanner getCoursePlanner() {
		return coursePlanner;
	}
	
	protected void setHeaderFont(JLabel header) {
		header.setFont(new Font("Calibri", Font.BOLD, headerSize));
	}
	
	private void setBackgroundColor(Color backgroundColor, JPanel bodyPanel) {
		if (backgroundColor != null) {
			bodyPanel.setBackground(backgroundColor);
		}
	}
	
	protected Font setFont(int fontType) {
		return new Font("Calibri", fontType, DETAILS_FONT_SIZE);
	}
	
	protected void removeUpdateUi(JPanel panel) {
		panel.removeAll();
		panel.revalidate();
	}
	
	protected void updateUi(JPanel panel) {
		panel.revalidate();
		panel.repaint();
	}
}
