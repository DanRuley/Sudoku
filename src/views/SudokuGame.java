package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import constants.Constants;
import utilities.KeyHandler;
import utilities.MoveManager;
import utilities.SudokuGenerator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class SudokuGame extends JFrame {

	private JPanel hostPanel;
	private JPanel gamePanel;
	private JPanel[][] gameBox;
	private Cell[][] cells;
	private Cell toggled;

	private HashSet<Cell> conflicts;
	private Dimension size;
	private KeyHandler keyHandler;
	private MoveManager moveManager;

	private int[][] board;
	private boolean[][] initState;

	private static final long serialVersionUID = 1L;

	public SudokuGame() {
		init();
	}

	private void init() {
		moveManager = new MoveManager();
		size = new Dimension(Constants.CELL_PIXELS * Constants.GRID_SIZE, Constants.CELL_PIXELS * Constants.GRID_SIZE);
		this.setSize(size);
		this.setMaximumSize(size);
		this.setMinimumSize(size);
		this.setBackground(Color.BLACK);
		hostPanel = new JPanel();
		hostPanel.setLayout(new BorderLayout(5, 5));
		toggled = null;

		keyHandler = new KeyHandler(this, moveManager);
		conflicts = new HashSet<>();

		this.addKeyListener(keyHandler);

		initState = new boolean[Constants.GRID_SIZE][Constants.GRID_SIZE];
		board = new int[Constants.GRID_SIZE][Constants.GRID_SIZE];
		cells = new Cell[Constants.GRID_SIZE][Constants.GRID_SIZE];
		readGameBoard(3);

		for (int r = 0; r < board.length; r++)
			for (int c = 0; c < board[0].length; c++)
				System.out.print(initState[r][c] ? board[r][c] : ".");

		makeGameBoard();
		hostPanel.add(gamePanel, BorderLayout.CENTER);

		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(hostPanel);
		this.pack();
	}

	private void readGameBoard(int difficulty) {

		try {
			Random rng = new Random();
			FileInputStream f = new FileInputStream(
					"C:\\Users\\drslc\\OneDrive\\Documents\\GitHub\\Sudoku\\src\\puzzles\\rated" + difficulty + ".txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(f));

			int lineCount = Integer.parseInt(br.readLine());
			int randomLine = rng.nextInt(lineCount) + 1;

			for (int i = 1; i < randomLine; i++)
				br.readLine();

			String puzzleStr = br.readLine();
			int i = 0;

			for (int r = 0; r < Constants.GRID_SIZE; r++) {
				for (int c = 0; c < Constants.GRID_SIZE; c++) {
					if (puzzleStr.charAt(i) == '.') {
						board[r][c] = 0;
						initState[r][c] = false;
					} else {
						board[r][c] = puzzleStr.charAt(i) - '0';
						initState[r][c] = true;
					}
					i++;
				}
			}
			SudokuGenerator solver = new SudokuGenerator(board);
			board = solver.solve();

		} catch (FileNotFoundException e) {
			System.out.println("Error: file not found.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error: problem reading file.");
			e.printStackTrace();
		}

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
						int row = i * Constants.BOX_SIZE + ii;
						int col = j * Constants.BOX_SIZE + jj;

						Cell current = new Cell(row, col, board[row][col], initState[row][col], Constants.CELL_SIZE,
								this);

						cells[row][col] = current;

						gameBox[i][j].add(current);

						if (initState[row][col])
							current.setDigit(board[row][col]);
					}
				}

				gameBox[i][j].setBorder(new LineBorder(Color.BLACK, 1, false));
				gamePanel.add(gameBox[i][j]);
			}
		}

	}

	public void processMove() {
		checkAndUpdateConflicts();
		repaintBoard();
	}

	/**
	 * A given cell is conflicted if it contains the same digit as another cell in
	 * its row, column or box. This method checks the status of the Cells in the
	 * conflicted set to see if they are still in conflict. It also checks the
	 * status of the toggled cell (which would have been affected by the previous
	 * move) and adds any conflicts to the set.
	 */
	private void checkAndUpdateConflicts() {

		HashSet<Cell> stillConflicted = new HashSet<Cell>();

		// Process conflicted cells from last turn and keep the ones that are still in
		// conflict
		for (Cell c : conflicts) {

			if (stillConflicted.contains(c))
				continue;

			stillConflicted.addAll(getRowColBoxConflicts(c));
		}

		// Check the current toggled cell for conflicts
		stillConflicted.addAll(getRowColBoxConflicts(toggled));

		conflicts = stillConflicted;
	}

	/**
	 * Slightly unwieldy method to check conflicts with the provided Cell. This
	 * method finds any Cells with the same digit as the provided Cell and adds
	 * them, along with the given Cell to a set.
	 * 
	 * @param toCheck - Cell to check
	 * @return dupes - set of Cells which are in conflict (contain the same digit
	 *         and are in the same row, column, or box).
	 */
	private HashSet<Cell> getRowColBoxConflicts(Cell toCheck) {

		HashSet<Cell> dupes = new HashSet<>();
		int row = toCheck.getRow();
		int col = toCheck.getCol();
		int box = getBoxNumber(toCheck.getRow(), toCheck.getCol());
		int boxRowStart = (box / Constants.BOX_SIZE) * Constants.BOX_SIZE;
		int boxColStart = (box % Constants.BOX_SIZE) * Constants.BOX_SIZE;
		int digit;

		// check row
		for (int i = 0; i < Constants.GRID_SIZE; i++) {
			digit = cells[row][i].getDisplayNumber();
			if (cells[row][i] != toCheck && digit != Constants.UNFILLED && digit == toCheck.getDisplayNumber())
				dupes.add(cells[row][i]);
		}

		// check col
		for (int i = 0; i < Constants.GRID_SIZE; i++) {
			digit = cells[i][col].getDisplayNumber();
			if (cells[i][col] != toCheck && digit != Constants.UNFILLED && digit == toCheck.getDisplayNumber())
				dupes.add(cells[i][col]);
		}

		// check box
		for (int i = boxRowStart; i < boxRowStart + Constants.BOX_SIZE; i++)
			for (int j = boxColStart; j < boxColStart + Constants.BOX_SIZE; j++) {
				digit = cells[i][j].getDisplayNumber();
				if (cells[i][j] != toCheck && digit != Constants.UNFILLED && digit == toCheck.getDisplayNumber())
					dupes.add(cells[i][j]);
			}

		// If any conflicts were found, we also need to add the provided cell :)
		if (dupes.size() > 0)
			dupes.add(toCheck);

		return dupes;
	}

	/**
	 * Reset the background of the old toggled cell. Then, set the toggled cell to
	 * the one at the provided (row,col) position. Then set its background to
	 * selected.
	 * 
	 * @param row - new toggled cell row
	 * @param col - new toggled cell column
	 */
	public void setToggled(int row, int col) {
		Cell c = cells[row][col];

		if (toggled != null)
			toggled.setDefaultBackground();

		toggled = c;
		c.setBackground(Constants.SELECTED_BG);
	}

	/**
	 * Tell of the cells in the board to repaint.
	 */
	public void repaintBoard() {
		for (Cell[] row : cells) {
			for (Cell c : row) {
				c.repaint();
			}
		}
	}

	/**
	 * Get the currently toggled cell (This is the cell which will be affected by
	 * any moves)
	 * 
	 * @return the toggled cell
	 */
	public Cell getToggled() {
		return toggled;
	}

	/**
	 * Translate a (row, col) coordinate to its given box in the grid
	 * 
	 * @param row
	 * @param col
	 * @return The box that the given (row, col) resides in.
	 */
	private static int getBoxNumber(int row, int col) {
		if (row >= 0 && row < 3)
			return col / 3;
		else if (row >= 3 && row < 6)
			return 3 + col / 3;
		else
			return 6 + col / 3;
	}

	/**
	 * Returns the conflict status of a given cell
	 * 
	 * @param cell - the cell to check
	 * @return true if the cell is in conflict with another cell, false otherwise
	 */
	public boolean isConflicted(Cell cell) {
		return conflicts.contains(cell);
	}
}
