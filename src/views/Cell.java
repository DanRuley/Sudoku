package views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
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
	private boolean initialNumber;
	private HashSet<Integer> notes;
	private Dimension size;
	private SudokuView game;

	public Cell(int row, int col, int actualNumber, boolean init, Dimension size, SudokuView game) {

		this.row = row;
		this.col = col;
		this.size = size;
		this.initialNumber = init;
		this.game = game;

		this.displayNumber = init ? actualNumber : Constants.UNFILLED;

		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.setPreferredSize(size);
		this.setDefaultBackground();

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				game.setToggled(row, col);
			}
		});

		notes = new HashSet<>();
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public void setDigit(int num) {
		// Cannot change a given digit cell.
		if (initialNumber)
			return;

		notes = new HashSet<>();
		this.displayNumber = num;
		repaint();
	}

	public void setNote(int num) {
		// Cannot change a given digit cell
		if (initialNumber)
			return;

		displayNumber = Constants.UNFILLED;
		;
		if (notes.contains(num)) {
			notes.remove(num);
		} else {
			notes.add(num);
		}
		repaint();
	}

	public void clearDigit() {

		// Deletes on a given digit should have no effect
		if (initialNumber)
			return;

		notes = new HashSet<>();
		displayNumber = Constants.UNFILLED;
		;

		repaint();
	}

	@Override
	public Dimension getPreferredSize() {
		return size;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (notes.size() > 0)
			drawNotes(g);
		else if (displayNumber > 0)
			drawNumber(g);
	}

	private void drawNumber(Graphics g) {

		if (this.displayNumber == game.getToggled().displayNumber)
			g.setFont(Constants.SELECTED_NUM_FONT);
		else
			g.setFont(Constants.NUM_FONT);

		Rectangle2D strBounds = g.getFontMetrics().getStringBounds("" + displayNumber, g);
		int x = (int) (this.getWidth() / 2 - strBounds.getWidth() / 2);
		int y = (int) ((this.getHeight() / 2) + (strBounds.getHeight() / 4));
		g.drawString("" + displayNumber, x, y);
	}

	private void drawNotes(Graphics g) {
		for (int i : notes) {

			if (i == game.getToggled().displayNumber)
				g.setFont(Constants.SELECTED_NOTE_FONT);
			else
				g.setFont(Constants.NOTE_FONT);

			int row = (i - 1) / 3;
			int col = (i - 1) % 3;

			col = this.getWidth() / Constants.NOTE_MAGIC_VAL + (col * this.getWidth() / Constants.NOTE_MAGIC_VAL)
					- Constants.NOTE_MAGIC_VAL;
			row = this.getHeight() / Constants.NOTE_MAGIC_VAL + (row * this.getHeight() / Constants.NOTE_MAGIC_VAL)
					+ Constants.NOTE_MAGIC_VAL;

			g.drawString("" + i, col, row);
		}
	}

	public void setDefaultBackground() {
		if (initialNumber)
			this.setBackground(Constants.UNSELECTED_BG_INITIAL);
		else
			this.setBackground(Constants.UNSELECTED_BG);
	}

	public boolean containsDigit(int num) {
		return this.displayNumber == num || this.notes.contains(num);
	}

	public int getDisplayNumber() {
		return displayNumber;
	}
}
