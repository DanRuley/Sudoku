package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;

import constants.Constants;
import models.SudokuModel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class SudokuView extends JFrame implements KeyListener {

	public static void main(String[] args) throws InterruptedException {
		SudokuView s = new SudokuView();
		s.setVisible(true);
	}

	private SudokuModel model;
	private JPanel hostPanel;
	private JPanel gamePanel;
	private JPanel[][] gameBox;
	private Cell[][] cells;
	private Cell toggled;
	private int toggledRow;
	private int toggledCol;
	private HashMap<Character, Integer> noteKeys;
	private Dimension size;

	private int[][] board;
	private boolean[][] initState;
	private int keyLock;

	private static final long serialVersionUID = 1L;

	public SudokuView() {
		init();
	}

//	public SudokuView(int[][] board, int[][] initState) {
//		this.board = board;
//		this.boolean = initState;
//		init();
//	}

	private void init() {
		size = new Dimension(Constants.CELL_PIXELS * Constants.GRID_SIZE, Constants.CELL_PIXELS * Constants.GRID_SIZE);
		this.setSize(size);
		this.setMaximumSize(size);
		this.setMinimumSize(size);
		this.setBackground(Color.BLACK);
		hostPanel = new JPanel();
		hostPanel.setLayout(new BorderLayout(5, 5));
		toggled = null;
		this.addKeyListener(this);
		noteKeys = new HashMap<>();
		int i = 1;
		for (Character c : new char[] { '!', '@', '#', '$', '%', '^', '&', '*', '(' }) {
			noteKeys.put(c, i++);
		}

		initState = new boolean[Constants.GRID_SIZE][Constants.GRID_SIZE];
		board = new int[Constants.GRID_SIZE][Constants.GRID_SIZE];
		cells = new Cell[Constants.GRID_SIZE][Constants.GRID_SIZE];
		readGameBoard("C:\\Users\\drslc\\OneDrive\\Documents\\GitHub\\Sudoku\\src\\puzzles\\test1.txt");

		// model = new SudokuModel(this);
		makeGameBoard();
		hostPanel.add(gamePanel, BorderLayout.CENTER);
		keyLock = -1;

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(hostPanel);
		this.pack();
	}

	private void readGameBoard(String filePath) {
		File f = new File(filePath);
		int r, c;
		r = c = 0;
		try {
			Scanner scn = new Scanner(f);
			for (int i = 0; i < Constants.GRID_SIZE; i++) {
				String lineString = scn.nextLine();
				for (int j = 0; j < lineString.length(); j += 2) {
					board[r][c] = (lineString.charAt(j) - '0');
					initState[r][c++] = lineString.charAt(j + 1) == '$';
				}
				c = 0;
				r++;
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error: file not found.");
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
						int row = i * 3 + ii;
						int col = j * 3 + jj;
						cells[row][col] = new Cell(row, col, board[row][col], initState[row][col], Constants.CELL_SIZE,
								this);

						gameBox[i][j].add(cells[row][col]);

						if (initState[row][col])
							cells[row][col].setDigit(board[row][col]);
					}
				}

				gameBox[i][j].setBorder(new LineBorder(Color.BLACK, 1, false));
				gamePanel.add(gameBox[i][j]);
			}
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

		int keyCode = e.getKeyCode();
		char key = e.getKeyChar();

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

		}

		setToggled(toggledRow, toggledCol);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		System.out.println(e.getKeyCode());
		keyLock = -1;
	}

	public void setDigit(int row, int col, int num) {
		cells[row][col].setDigit(num);
	}

	public void clearDigit(int row, int col) {
		cells[row][col].clearDigit();
	}

	public void setToggled(int row, int col) {
		Cell c = cells[row][col];

		if (toggled != null) {
			toggled.setDefaultBackground();
		}

		toggled = c;
		toggledRow = c.getRow();
		toggledCol = c.getCol();
		c.setBackground(Constants.SELECTED_BG);
	}

}
