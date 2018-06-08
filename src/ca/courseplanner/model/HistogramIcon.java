package ca.courseplanner.model;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import javax.swing.Icon;

import ca.courseplanner.model.Histogram.Bar;

/**
 * Draws histogram with a particular width
 * and height
 * 
 * @author Alan
 *
 */
public class HistogramIcon implements Icon {

	private final Histogram histogram;
	private final int barSpacing = 5;
	private final int margin = 20;
	private final int textBarCountOffset = 8;
	private final int barCountTextSize = 12;
	private final int textIntervalOffset = 15;
	private final int intervalTextHeight = 12;
	private int horizontalTextOffsetError = 8;
	private final int width;
	private final int height;
	private int verticalAxisLength;
	private int horizontalAxisLength;
	private int distanceToHorizontalAxis;
	private int distanceToVerticalAxis;
	
	public HistogramIcon(Histogram histogram, int width, int height) {
		this.histogram = histogram;
		this.width = width;
		this.height = height;
	}
	
	private double getBarWidth() {
		assert(histogram.getNumberBars() > 0);
		return getTotalGraphWithSpacing() / histogram.getNumberBars();
	}
	
	private double getTotalGraphWithSpacing() {
		double totalGraphWidthWithSpacing = getHorizontalAxisLength() - getTotalBarSpacing();
		return totalGraphWidthWithSpacing;
	}
	
	private double getTotalBarSpacing() {
		return barSpacing * histogram.getNumberBars();
	}
	
	private int getHorizontalAxisLength() {
		assert(margin >= 0);
		int totalHorizontalMargin = 2 * margin;
		return width - totalHorizontalMargin;
	}
	
	private double getScaledBarHeight(int barHeight) {
		return barHeight * getMultiplier();
	}
	
	private double getMultiplier() {
		// determines how much height needs to be resized by
		assert(histogram.getMaxBarCount() > 0);
		return getVerticalAxisLength() / histogram.getMaxBarCount();
	}
	
	private int getVerticalAxisLength() {
		int totalTextHeight = 2 * intervalTextHeight;
		assert(getMarginlessHeight() >= totalTextHeight);
		return getMarginlessHeight() - totalTextHeight;
	}

	private int getMarginlessHeight() {
		int totalVerticalMargin = 2 * margin;
		assert(height >= totalVerticalMargin);
		return height - totalVerticalMargin;
	}

	@Override
	public int getIconWidth() {
		return width;
	}

	@Override
	public int getIconHeight() {
		return height;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int width, int height) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.RED);
		drawBackground(g2d);
		g2d.setColor(Color.BLACK);
		drawAxis(g2d);
		if (histogram != null) {
			drawGraph(g2d);
		}
	}

	private void drawBackground(Graphics2D g2d) {
		Shape rectangle = new Rectangle2D.Double(0, 0, width-1, height-1);

		g2d.setColor(Color.WHITE);
		g2d.fill(rectangle);
	}

	private void drawAxis(Graphics2D g2d) {
		computeAxisLength();
		computeInitialAxisPosition();
		drawHorizontalAxis(g2d);
		drawVerticalAxis(g2d);
	}
	
	private void computeAxisLength() {
		horizontalAxisLength = getHorizontalAxisLength();
		verticalAxisLength = getVerticalAxisLength();
	}
	
	private void computeInitialAxisPosition() {
		distanceToVerticalAxis = margin;
		distanceToHorizontalAxis = margin + verticalAxisLength;
	}

	private void drawHorizontalAxis(Graphics2D g2d) {
		Point2D p1 = new Point2D.Double(margin, distanceToHorizontalAxis);
		Point2D p2 = new Point2D.Double(margin + horizontalAxisLength, distanceToHorizontalAxis);
		Line2D horizontalLine = new Line2D.Double(p1, p2);
		
		g2d.draw(horizontalLine);
	}

	private void drawVerticalAxis(Graphics2D g2d) {
		Point2D p1 = new Point2D.Double(margin, margin);
		Point2D p2 = new Point2D.Double(margin, distanceToHorizontalAxis);
		Line2D verticalLine = new Line2D.Double(p1, p2);
		
		g2d.draw(verticalLine);
	}
	
	private void drawGraph(Graphics2D g2d) {
		Iterator<Histogram.Bar> iterator = histogram.iterator();
		double barWidth = getBarWidth();
		double xLocation = distanceToVerticalAxis;
		int intervalCount = 0;
		double yLocation;
		double barHeight;
		Bar bar;
		while(iterator.hasNext()) {
			bar = iterator.next();
			xLocation += barSpacing;
			barHeight = getScaledBarHeight(bar.getCount());
			yLocation = distanceToHorizontalAxis - barHeight;
			drawBar(g2d, xLocation, yLocation, barWidth, barHeight);
			drawCount(g2d, xLocation, yLocation, bar.getCount());
			drawInterval(g2d, xLocation, distanceToHorizontalAxis, intervalCount, bar.getRangeMin());
			intervalCount++;
			xLocation += barWidth;
		}
	}

	private void drawCount(Graphics2D g2d, double xLocation, double yLocation,
			int barCount) {
		int xLocationCentreOfBar = (int) (xLocation + ( (getBarWidth() - horizontalTextOffsetError) / 2));
		int offsetFromTopOfBar = (int) (yLocation - textBarCountOffset);
		String count = Integer.toString(barCount);
		g2d.setColor(Color.BLACK);
		g2d.setFont(new Font("Calibri", Font.BOLD, barCountTextSize));
		g2d.drawString(count, xLocationCentreOfBar, offsetFromTopOfBar);
	}

	private void drawBar(Graphics2D g2d, double xLocation, double yLocation, double barWidth, double barHeight) {
		Shape rectangle = new Rectangle2D.Double(xLocation, yLocation, barWidth, barHeight);
		
		g2d.setColor(Color.BLUE);
		g2d.fill(rectangle);
	}

	private void drawInterval(Graphics2D g2d, double xLocation, int yLocation, int intervalCount, int rangeMin) {
		int xLocationCentreOfBar = (int) (xLocation + ( (getBarWidth() - horizontalTextOffsetError) / 2));
		int offsetFromBottomOfBar = (int) (yLocation + textIntervalOffset);
		int textSize = intervalTextHeight;
		String textIntervalValue = Integer.toString(rangeMin);
		boolean isOddIntervalCount = intervalCount % 2 != 0;
		if(isOddIntervalCount) {
			offsetFromBottomOfBar += textSize;
		}
		
		g2d.setFont(new Font("Calibri", Font.BOLD, textSize));
		g2d.setColor(Color.BLACK);
		g2d.drawString(textIntervalValue, xLocationCentreOfBar, offsetFromBottomOfBar);
	}
}
