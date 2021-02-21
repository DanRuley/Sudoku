package sudoku;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

public class GameModel {
	private Cell[][] cells;
	private Cell toggled;
	private int toggledRow;
	private int toggledCol;
	private boolean ctrlToggled;
	private int keyLock;
	
	int[][] board = { { 4, 9, 3, 7, 2, 6, 8, 5, 1 }, { 7, 1, 8, 3, 4, 5, 9, 2, 6 }, { 5, 6, 2, 8, 9, 1, 4, 7, 3 },
			{ 8, 3, 6, 2, 5, 4, 1, 9, 7 }, { 1, 5, 9, 6, 8, 7, 3, 4, 2 }, { 2, 7, 4, 9, 1, 3, 5, 6, 8 },
			{ 3, 2, 1, 4, 7, 9, 6, 8, 5 }, { 9, 8, 5, 1, 6, 2, 7, 3, 4 }, { 6, 4, 7, 5, 3, 8, 2, 1, 9 } };
	int[][] initState = { { 1, 1, 0, 1, 0, 0, 0, 1, 1 }, { 1, 1, 0, 1, 1, 0, 1, 1, 0 }, { 0, 1, 0, 0, 0, 0, 1, 0, 1 },
			{ 1, 0, 0, 1, 0, 0, 1, 0, 0 }, { 0, 0, 0, 0, 0, 0, 1, 1, 0 }, { 0, 1, 0, 0, 0, 0, 0, 1, 1 },
			{ 1, 0, 0, 0, 0, 1, 1, 0, 1 }, { 0, 0, 0, 0, 1, 1, 0, 0, 1 }, { 0, 0, 0, 0, 0, 1, 0, 0, 0 } };
	
	public GameModel() {
		toggled = null;

		cells = new Cell[Constants.GRID_SIZE][Constants.GRID_SIZE];
		
		keyLock = -1;
		ctrlToggled = false;
	}
	
	public Cell[][] getCells() {
		return cells;
	}
	
	public int[][] getBoard(){
		return board;
	}
	
	public int[][] getInitState() {
		return initState;
	}
	
	public Cell initCell(int row, int col, JFrame board) {
		cells[row][col].setEditable(false);
		cells[row][col].addActionListener((ActionListener) board);
		cells[row][col].addMouseListener((MouseListener) board);
		cells[row][col].addKeyListener((KeyListener) board);
		cells[row][col].setBackground(Constants.UNSELECTED_BG);
	}
}
