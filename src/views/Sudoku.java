package views;

import javax.swing.SwingUtilities;

public class Sudoku {
	
	/**
	 * Main entry point for the application
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				SudokuView s = new SudokuView();
				s.setVisible(true);
			}
		});
	}
}
