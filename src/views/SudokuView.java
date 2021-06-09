package views;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import constants.Constants;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class SudokuView extends JFrame implements KeyListener {

	private JPanel hostPanel;
	private JPanel gamePanel;
	private JPanel[][] gameBox;
	private Cell[][] cells;
	private Cell toggled;
	private int toggledRow;
	private int toggledCol;
	private HashMap<Character, Integer> noteKeys;
	private Dimension size;

	int[][] board = { { 4, 9, 3, 7, 2, 6, 8, 5, 1 }, { 7, 1, 8, 3, 4, 5, 9, 2, 6 }, { 5, 6, 2, 8, 9, 1, 4, 7, 3 },
			{ 8, 3, 6, 2, 5, 4, 1, 9, 7 }, { 1, 5, 9, 6, 8, 7, 3, 4, 2 }, { 2, 7, 4, 9, 1, 3, 5, 6, 8 },
			{ 3, 2, 1, 4, 7, 9, 6, 8, 5 }, { 9, 8, 5, 1, 6, 2, 7, 3, 4 }, { 6, 4, 7, 5, 3, 8, 2, 1, 9 } };

	int[][] initState = { { 1, 1, 0, 1, 0, 0, 0, 1, 1 }, { 1, 1, 0, 1, 1, 0, 1, 1, 0 }, { 0, 1, 0, 0, 0, 0, 1, 0, 1 },
			{ 1, 0, 0, 1, 0, 0, 1, 0, 0 }, { 0, 0, 0, 0, 0, 0, 1, 1, 0 }, { 0, 1, 0, 0, 0, 0, 0, 1, 1 },
			{ 1, 0, 0, 0, 0, 1, 1, 0, 1 }, { 0, 0, 0, 0, 1, 1, 0, 0, 1 }, { 0, 0, 0, 0, 0, 1, 0, 0, 0 } };

	private int keyLock;

	private static final long serialVersionUID = 1L;

	public SudokuView() {
		size = new Dimension(Constants.CELL_PIXELS * Constants.GRID_SIZE, Constants.CELL_PIXELS * Constants.GRID_SIZE);
		this.setSize(size);
		this.setMaximumSize(size);
		this.setMinimumSize(size);
		this.setBackground(Color.BLACK);
		hostPanel = new JPanel();
		hostPanel.setLayout(new BorderLayout(5, 5));
		toggled = null;

		noteKeys = new HashMap<>();
		int i = 1;
		for (Character c : new char[] { '!', '@', '#', '$', '%', '^', '&', '*', '(' }) {
			noteKeys.put(c, i++);
		}

		cells = new Cell[Constants.GRID_SIZE][Constants.GRID_SIZE];

		makeGameBoard();
		hostPanel.add(gamePanel, BorderLayout.CENTER);
		keyLock = -1;

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(hostPanel);
		this.pack();
	}

	private void makeGameBoard() {
		gamePanel = new JPanel();
		gameBox = new JPanel[Constants.BOX_SIZE][Constants.BOX_SIZE];
		gamePanel.setLayout(new GridLayout(Constants.BOX_SIZE, Constants.BOX_SIZE));

		gamePanel = new JPanel();
		gameBox = new JPanel[Constants.BOX_SIZE][Constants.BOX_SIZE];
		GridLayout g = new GridLayout(Constants.BOX_SIZE, Constants.BOX_SIZE);
		gamePanel.setLayout(g);
		for (int i = 0; i < Constants.BOX_SIZE; i++) {
			for (int j = 0; j < Constants.BOX_SIZE; j++) {
				gameBox[i][j] = new JPanel();
				gameBox[i][j].setLayout(g);
				for (int ii = 0; ii < Constants.BOX_SIZE; ii++) {
					for (int jj = 0; jj < Constants.BOX_SIZE; jj++) {
						int row = i * 3 + ii;
						int col = j * 3 + jj;
						cells[row][col] = new Cell(row, col, board[row][col], initState[row][col] > 0,
								Constants.CELL_SIZE, this);
						cells[row][col].addKeyListener(this);
						
						gameBox[i][j].add(cells[row][col]);

						if (initState[row][col] > 0)
							cells[row][col].setDigit(board[row][col]);
					}
				}

				gameBox[i][j].setBorder(new LineBorder(Color.BLACK, 1, false));
				gamePanel.add(gameBox[i][j]);
			}
		}

	}

	public static void main(String[] args) {
		SudokuView s = new SudokuView();
		s.setVisible(true);
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

		int keyCode = e.getKeyCode();
		char key = e.getKeyChar();
		System.out.println(key);
		System.out.println("This ran");

		System.out.println(e.isShiftDown());

		if (keyLock == keyCode)
			return;

		if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_RIGHT
				|| keyCode == KeyEvent.VK_LEFT) {
			handleArrows(keyCode);
		} else if (toggled == null) {
			;
		} else if (Character.isDigit(key)) {
			key = (char) (key - '0');
			toggled.setDigit(key);
		} else if (noteKeys.containsKey(key)) {
			toggled.setNote(noteKeys.get(key));
		} else if (keyCode == KeyEvent.VK_BACK_SPACE || keyCode == KeyEvent.VK_DELETE) {
			toggled.clearDigit();
		}

		keyLock = keyCode;

		// Prevents Windows alert ding
		e.consume();
	}

	public void handleArrows(int code) {

		// if no cell is selected, begin at (0,0)
		if (toggled == null) {
			toggledRow = 0;
			toggledCol = 0;
		}
		// Otherwise, adjust toggled position accordingly.
		else {
			switch (code) {
			case KeyEvent.VK_UP:
				toggledRow = toggledRow - 1 < 0 ? 8 : toggledRow - 1;
				break;
			case KeyEvent.VK_DOWN:
				toggledRow = toggledRow + 1 > 8 ? 0 : toggledRow + 1;
				break;
			case KeyEvent.VK_LEFT:
				toggledCol = toggledCol - 1 < 0 ? 8 : toggledCol - 1;
				break;
			case KeyEvent.VK_RIGHT:
				toggledCol = toggledCol + 1 > 8 ? 0 : toggledCol + 1;
				break;
			}

			// clear old toggled bg
			toggled.setBackground(Constants.UNSELECTED_BG);
		}

		// set new toggled bg
		toggled = cells[toggledRow][toggledCol];
		toggled.setBackground(Constants.SELECTED_BG);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		System.out.println(e.getKeyCode());
		keyLock = -1;
	}

	public void setToggled(int row, int col) {
		Cell c = cells[row][col];

		if (toggled != null) {
			toggled.setBackground(Constants.UNSELECTED_BG);
		}
		toggled = c;
		toggledRow = c.getRow();
		toggledCol = c.getCol();
		c.setBackground(Constants.SELECTED_BG);
	}

}
