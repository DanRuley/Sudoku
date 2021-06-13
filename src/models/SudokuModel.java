package models;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import constants.Constants;
import views.SudokuView;

public class SudokuModel {

	private int[][] board;
	private static Integer[] digits = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	private HashMap<Integer, HashSet<Integer>> boxes;
	private HashMap<Integer, HashSet<Integer>> rows;
	private HashMap<Integer, HashSet<Integer>> cols;

	public SudokuModel() {
		board = new int[Constants.GRID_SIZE][Constants.GRID_SIZE];

		boxes = new HashMap<Integer, HashSet<Integer>>();
		rows = new HashMap<Integer, HashSet<Integer>>();
		cols = new HashMap<Integer, HashSet<Integer>>();

		for (int i = 0; i < Constants.GRID_SIZE; i++) {
			boxes.put(i, new HashSet<Integer>());
			rows.put(i, new HashSet<Integer>());
			cols.put(i, new HashSet<Integer>());
		}
	}

	private void clear() {
		boxes = new HashMap<Integer, HashSet<Integer>>();
		rows = new HashMap<Integer, HashSet<Integer>>();
		cols = new HashMap<Integer, HashSet<Integer>>();

		for (int i = 0; i < Constants.GRID_SIZE; i++) {
			boxes.put(i, new HashSet<Integer>());
			rows.put(i, new HashSet<Integer>());
			cols.put(i, new HashSet<Integer>());
		}

		board = new int[Constants.GRID_SIZE][Constants.GRID_SIZE];
	}

	private static int getBoxNumber(int row, int col) {
		if (row >= 0 && row < 3)
			return col / 3;
		else if (row >= 3 && row < 6)
			return 3 + col / 3;
		else
			return 6 + col / 3;
	}

	/**
	 * def backtrack(candidate): if find_solution(candidate): output(candidate)
	 * return
	 * 
	 * # iterate all possible candidates. for next_candidate in list_of_candidates:
	 * if is_valid(next_candidate): # try this partial candidate solution
	 * place(next_candidate) # given the candidate, explore further.
	 * backtrack(next_candidate) # backtrack remove(next_candidate)
	 */
	public boolean backtrack(HashSet<Cell> disallowed) {

		if (completeBoard())
			return true;

		ArrayList<Integer> nums;

		outofloop: for (int r = 0; r < Constants.GRID_SIZE; r++) {
			for (int c = 0; c < Constants.GRID_SIZE; c++) {

				if (board[r][c] == 0) {
					int box = getBoxNumber(r, c);
					nums = new ArrayList<>();
					Collections.addAll(nums, digits);
					Collections.shuffle(nums);

					for (int n = 0; n < nums.size(); n++) {

						int candidate = nums.get(n);

						if (disallowed != null)
							if (disallowed.contains(new Cell(r, c, candidate)))
								continue;

						// Number is not already in box/row/col
						if (!(boxes.get(box).contains(candidate) || rows.get(r).contains(candidate)
								|| cols.get(c).contains(candidate))) {

							// place the number
							addCandidate(candidate, r, c, box);

							if (backtrack(disallowed))
								return true;

							// Candidate led to invalid board state ==> clear from board/sets
							clearCandidate(candidate, r, c, box);
						}
					}
					break outofloop;
				}
			}
		}
		return false;
	}

	private void printBoard() {
		for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++)
				System.out.print((board[r][c] == 0 ? " " : board[r][c]) + " ");
			System.out.println();
		}
		System.out.println();
	}

	private void addCandidate(int candidate, int r, int c, int box) {
		board[r][c] = candidate;
		boxes.get(box).add(candidate);
		rows.get(r).add(candidate);
		cols.get(c).add(candidate);
	}

	private void clearCandidate(int candidate, int r, int c, int box) {
		board[r][c] = 0;
		boxes.get(box).remove(candidate);
		rows.get(r).remove(candidate);
		cols.get(c).remove(candidate);
	}

	/*
	 * Assumes backtracking has been run once
	 */
	private boolean clearAndCheck(int toDel) {

		HashSet<Cell> cleared = new HashSet<Cell>();
		ArrayList<Cell> filled = new ArrayList<>();
		for (int r = 0; r < Constants.GRID_SIZE; r++)
			for (int c = 0; c < Constants.GRID_SIZE; c++)
				filled.add(new Cell(r, c, board[r][c]));

		Collections.shuffle(filled);
		for (int i = 0; i < toDel; i++) {
			Cell rngDelete = filled.remove(filled.size() - 1);
			cleared.add(rngDelete);
			clearCandidate(rngDelete.val, rngDelete.r, rngDelete.c, getBoxNumber(rngDelete.r, rngDelete.c));
			if (backtrack(cleared))
				return false;
		}

		return true;
	}

	private boolean completeBoard() {
		for (int i = 0; i < Constants.GRID_SIZE; i++) {
			if (!(boxes.get(i).size() == Constants.GRID_SIZE && rows.get(i).size() == Constants.GRID_SIZE
					&& cols.get(i).size() == Constants.GRID_SIZE))
				return false;
		}
		return true;
	}

	public static void generateAndWritePuzzles(int numPuzzles, String filePath) {
		try {
			File f = new File(filePath);
			FileWriter writer = new FileWriter(f);
			SudokuModel sm = new SudokuModel();
			int validPuzzles = 0;

			while (validPuzzles < numPuzzles) {
				sm.clear();
				sm.backtrack(null);
				int[][] completeBoard = new int[Constants.GRID_SIZE][Constants.GRID_SIZE];

				for (int r = 0; r < completeBoard.length; r++)
					completeBoard[r] = Arrays.copyOf(sm.board[r], Constants.GRID_SIZE);

				if (sm.clearAndCheck(45)) {
					int[][] puzzleBoard = sm.board;
					for (int r = 0; r < Constants.GRID_SIZE; r++) {
						for (int c = 0; c < Constants.GRID_SIZE; c++) {
							writer.write(completeBoard[r][c] + (puzzleBoard[r][c] == 0 ? "x" : "$"));
						}
						writer.write("\n");
					}
					writer.write("\n");
					validPuzzles++;
				} else
					System.out.println("bad");
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Error: file not found");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		generateAndWritePuzzles(25, "C:\\Users\\drslc\\OneDrive\\Documents\\GitHub\\Sudoku\\src\\puzzles\\test1.txt");
	}
}

class Cell {
	public int r;
	public int c;
	public int val;

	public Cell(int _r, int _c, int _val) {
		r = _r;
		c = _c;
		val = _val;
	}

	@Override
	public int hashCode() {
		return r * Constants.GRID_SIZE + c;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Cell))
			return false;

		Cell o = (Cell) other;
		return r == o.r && c == o.c && val == o.val;
	}

	@Override
	public String toString() {
		return "(" + r + ", " + c + "): " + val;
	}
}
