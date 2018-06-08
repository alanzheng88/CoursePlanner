package ca.courseplanner.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ca.courseplanner.model.Histogram;
import ca.courseplanner.model.HistogramIcon;

/**
 * Class is used to create an
 * initial layout with a title,
 * histogram and legend given a
 * specified width and height
 * 
 * @author Alan
 *
 */
@SuppressWarnings("serial")
public class HistogramPanel extends JPanel {

	private static final int OVERHEAD_SPACE = 15;
	private Histogram histogram;
	
	public HistogramPanel(Histogram histogram, String title, 
			String legend, int width, int height) {
		this.histogram = histogram;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(Box.createRigidArea(new Dimension(0, OVERHEAD_SPACE)));
		add(makeLabel(title, Color.black));
		add(makeHistogram(width, height));
		add(makeLabel(legend, Color.black));
	}
	
	private JLabel makeHistogram(int width, int height) {
		Icon icon = new HistogramIcon(histogram, width, height);
		JLabel label = new JLabel(icon);
		return label;
	}

	private JLabel makeLabel(String text, Color fontColor) {
		JLabel label = new JLabel(text);
		label.setForeground(fontColor);
		return label;
	}
}
