package ca.courseplanner.model;

/**
 * Class used to find location
 * of a particular description name.
 * 
 * @author alanzheng
 *
 */
public class ColumnSelector {

	private String[] columnList;
	
	public ColumnSelector(String line) {
		columnList = line.split(",");
	}
	
	public int findLocation(String description) {
		for(int position = 0 ; position < columnList.length ; position++) {
			String currentColumn = columnList[position];
			if(currentColumn.equals(description)) {
				return position;
			}
		}
		return -1;	// if not found
	}
	
	public int getColumnListLength() {
		return columnList.length;
	}
	
	@Override
	public String toString() {
		return getClass().getName()
				+ "["
				+ "columnList=" + columnList.toString()
				+ "]";
	}
}
