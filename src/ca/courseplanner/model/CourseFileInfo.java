package ca.courseplanner.model;


/**
 * Holds all information for a particular
 * course
 * 
 * @author Alan
 *
 */
public class CourseFileInfo {

	private String[] fileInfo;
	private Row rowInfo;
	private ColumnSelector selector;
	
	public CourseFileInfo(String courseInfo, ColumnSelector selector, int columnLength) {
		this.selector = selector;
		fileInfo = parseInfo(courseInfo, columnLength);
		generateRow(fileInfo);
	}

	private void generateRow(String[] fileInfo) {
		rowInfo = new Row(fileInfo, selector);
	}

	// assumes that last item added are names
	private String[] parseInfo(String courseInfo, int columnLength) {
		String[] tempInfo = courseInfo.split(",", columnLength);
		return tempInfo;
	}

	public Row getInfo() {
		return rowInfo;
	}
	
	@Override
	public String toString() {
		return getClass().getName()
				+ "["
				+ rowInfo.toString()
				+ "]";
	}

}
