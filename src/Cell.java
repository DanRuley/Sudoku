import java.awt.Font;
import java.util.TreeSet;

import javax.swing.JTextField;

public class Cell extends JTextField {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int row;
	private int col;
	private TreeSet<Integer> notes;
	private int filledNumber;
	private int actualNumber;
	
	public Cell(int row, int col, int actualNumber, Font textFont) {
		this.row = row;
		this.col = col;
		this.actualNumber = actualNumber;
		
		this.setFont(textFont);
		this.setHorizontalAlignment(JTextField.CENTER);
	}
	
	public void setDigit(int digit) {
		this.setText(digit + "");
	}
	
	public void clearDigit() {
		this.setText("");
	}
	
	public int getRow() {
		return row;
	}
	public int getCol() {
		return col;
	}

	
}
