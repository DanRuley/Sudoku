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
import utilities.MoveType;
import utilities.SudokuGenerator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class SudokuView extends JFrame {

	private JPanel hostPanel;
	private JPanel gamePanel;
	private JPanel[][] gameBox;
	private Cell[][] cells;
	private Cell toggled;

	private HashMap<Integer, HashSet<Cell>> rows;
	private HashMap<Integer, HashSet<Cell>> cols;
	private HashMap<Integer, HashSet<Cell>> boxes;
	private Dimension size;
	private KeyHandler keyHandler;
	private MoveManager moveManager;

	private int[][] board;
	private boolean[][] initState;

	private static final long serialVersionUID = 1L;

	public SudokuView() {
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
		toggled = null;// new Cell(Constants.UNFILLED, Constants.UNFILLED, Constants.UNFILLED, false,
						// null, this);
		keyHandler = new KeyHandler(this);

		this.addKeyListener(keyHandler);

		boxes = new HashMap<Integer, HashSet<Cell>>();
		rows = new HashMap<Integer, HashSet<Cell>>();
		cols = new HashMap<Integer, HashSet<Cell>>();

		for (int i = 0; i < Constants.GRID_SIZE; i++) {
			boxes.put(i, new HashSet<Cell>());
			rows.put(i, new HashSet<Cell>());
			cols.put(i, new HashSet<Cell>());
		}

		initState = new boolean[Constants.GRID_SIZE][Constants.GRID_SIZE];
		board = new int[Constants.GRID_SIZE][Constants.GRID_SIZE];
		cells = new Cell[Constants.GRID_SIZE][Constants.GRID_SIZE];
		readGameBoard(4);

		for (int r = 0; r < board.length; r++)
			for (int c = 0; c < board[0].length; c++)
				System.out.print(initState[r][c] ? board[r][c] : ".");

		makeGameBoard();
		hostPanel.add(gamePanel, BorderLayout.CENTER);

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
						int box = getBoxNumber(row, col);

						Cell current = new Cell(row, col, board[row][col], initState[row][col], Constants.CELL_SIZE,
								this);

						cells[row][col] = current;
						rows.get(row).add(current);
						cols.get(col).add(current);
						boxes.get(box).add(current);

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

	private void checkAndUpdateConflicts() {

		HashSet<Cell> conflicts = new HashSet<Cell>();

		for (int i = 0; i < Constants.GRID_SIZE; i++) {
			conflicts.addAll(checkRowColOrBoxConflicts(rows.get(i)));
			conflicts.addAll(checkRowColOrBoxConflicts(cols.get(i)));
			conflicts.addAll(checkRowColOrBoxConflicts(boxes.get(i)));
		}

		for (Cell[] row : cells)
			for (Cell c : row)
				c.setConflicted(conflicts.contains(c));
	}

	private HashSet<Cell> checkRowColOrBoxConflicts(HashSet<Cell> cells) {
		HashSet<Cell> conflicts = new HashSet<Cell>();
		HashSet<Integer> digits = new HashSet<Integer>();
		HashSet<Integer> dupes = new HashSet<Integer>();

		for (Cell c : cells) {
			if (c.getDisplayNumber() < 0)
				continue;
			else if (!digits.add(c.getDisplayNumber())) {
				dupes.add(c.getDisplayNumber());
			}
		}
		for (Cell c : cells)
			if (dupes.contains(c.getDisplayNumber()))
				conflicts.add(c);

		return conflicts;
	}

	public void setToggled(int row, int col) {
		Cell c = cells[row][col];

		if (toggled != null)
			toggled.setDefaultBackground();

		toggled = c;
		c.setBackground(Constants.SELECTED_BG);
	}

	public void repaintBoard() {
		for (Cell[] row : cells) {
			for (Cell c : row) {
				c.repaint();
			}
		}
	}

	public Cell getToggled() {
		return toggled;
	}

	private static int getBoxNumber(int row, int col) {
		if (row >= 0 && row < 3)
			return col / 3;
		else if (row >= 3 && row < 6)
			return 3 + col / 3;
		else
			return 6 + col / 3;
	}

}
