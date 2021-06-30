package views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;

import constants.Constants;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Cell extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int row;
	private int col;
	private int displayNumber;
	private boolean isInitialNumber;
	private boolean[] notes;
	private Dimension size;
	private SudokuGame game;
	private Color numColor;
	// private boolean conflicted;

	public Cell(int _row, int _col, int actualNumber, boolean _init, Dimension _size, SudokuGame _game) {

		row = _row;
		col = _col;
		size = _size;
		isInitialNumber = _init;
		game = _game;
		// conflicted = false;

		if (isInitialNumber) {
			displayNumber = actualNumber;
			numColor = Color.black;
		} else {
			displayNumber = Constants.UNFILLED;
			numColor = Constants.FILLED_NUM;
		}

		this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		this.setPreferredSize(size);
		this.setDefaultBackground();

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				game.setToggled(row, col);
				game.repaintBoard();
			}

			// Can use this to implement multiple note sets:
//			@Override
//			public void mouseEntered(MouseEvent e) {
//				System.out.println("dragged over (" + row + ", " + col + ")");
//			}
		});

		notes = new boolean[10];
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public void setDigit(int num) {
		// Cannot change a given digit cell.
		if (isInitialNumber)
			return;

		// faster than allocating new arr
		for (int i = 0; i < notes.length; i++)
			notes[i] = false;

		this.displayNumber = num;
		repaint();
	}

	public void setNote(int num) {
		// Cannot change a given digit cell
		if (isInitialNumber)
			return;

		displayNumber = Constants.UNFILLED;
		;
		if (notes[num]) {
			notes[num] = false;
		} else {
			notes[0] = true;
			notes[num] = true;
		}
		repaint();
	}

	public void clearDigit() {

		// Deletes on a given digit should have no effect
		if (isInitialNumber)
			return;

		for (int i = 0; i < notes.length; i++)
			notes[i] = false;

		displayNumber = Constants.UNFILLED;

		repaint();
	}

	@Override
	public Dimension getPreferredSize() {
		return size;
	}

	public void setDefaultBackground() {
		this.setBackground(Constants.UNSELECTED_BG);
	}

	public boolean containsDigit(int num) {
		return this.displayNumber == num || this.notes[num];
	}

	public int getDisplayNumber() {
		return displayNumber;
	}

	@Override
	public String toString() {
		return "row: " + row + ", col: " + col + ", digit: " + displayNumber;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Cell))
			return false;
		Cell o = (Cell) other;
		return this.row == o.row && this.col == o.col && this.displayNumber == o.displayNumber;
	}

	@Override
	public int hashCode() {
		return this.row * Constants.GRID_SIZE + this.col;
	}

//	public void setConflicted(boolean conflict) {
//		if (conflict)
//			numColor = Color.red;
//		else
//			numColor = isInitialNumber ? Color.black : Constants.FILLED_NUM;
//	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		if (Constants.DESKTOP_HINTS != null) {
			g2d.setRenderingHints(Constants.DESKTOP_HINTS);
		}

		if (notes[0])
			drawNotes(g2d);
		else if (displayNumber > 0)
			drawNumber(g2d);
	}

	private void drawNumber(Graphics2D g) {

		if (game.getToggled() != null && this.displayNumber == game.getToggled().displayNumber) {
			g.setFont(Constants.SELECTED_NUM_FONT);
			if (this != game.getToggled())
				this.setBackground(Constants.SAME_NUM_BG);
		} else {
			g.setFont(Constants.NUM_FONT);
			this.setBackground(Constants.UNSELECTED_BG);
		}

		if (game.isConflicted(this))
			g.setColor(Color.red);
		else
			g.setColor(numColor);

		Rectangle2D strBounds = g.getFontMetrics().getStringBounds("" + displayNumber, g);
		int x = (int) (this.getWidth() / 2 - strBounds.getWidth() / 2);
		int y = (int) ((this.getHeight() / 2) + (strBounds.getHeight() / 3));
		g.drawString("" + displayNumber, x, y);
	}

	private void drawNotes(Graphics2D g) {

		for (int i = 1; i < 10; i++) {
			if (!notes[i])
				continue;

			if (i == game.getToggled().displayNumber)
				g.setFont(Constants.SELECTED_NOTE_FONT);
			else
				g.setFont(Constants.NOTE_FONT);

			int row = (i - 1) / 3;
			int col = (i - 1) % 3;

			col = this.getWidth() / Constants.NOTE_MAGIC_VAL + (col * this.getWidth() / Constants.NOTE_MAGIC_VAL)
					- Constants.NOTE_MAGIC_VAL - 1;
			row = this.getHeight() / Constants.NOTE_MAGIC_VAL + (row * this.getHeight() / Constants.NOTE_MAGIC_VAL)
					+ Constants.NOTE_MAGIC_VAL + 2;

			g.drawString("" + i, col, row);
		}
	}

}
