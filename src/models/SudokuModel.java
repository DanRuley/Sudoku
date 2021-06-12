package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import constants.Constants;
import views.SudokuView;

public class SudokuModel {

	private int[][] board;
	private boolean[][] givens;
	private static Integer[] digits = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	private HashMap<Integer, HashSet<Integer>> boxes;
	private HashMap<Integer, HashSet<Integer>> rows;
	private HashMap<Integer, HashSet<Integer>> cols;
	private Random rng;
	private static int opCount = 0;
	private SudokuView v;

	public SudokuModel(SudokuView _v) {
		board = new int[Constants.GRID_SIZE][Constants.GRID_SIZE];
		givens = new boolean[Constants.GRID_SIZE][Constants.GRID_SIZE];

		boxes = new HashMap<Integer, HashSet<Integer>>();
		rows = new HashMap<Integer, HashSet<Integer>>();
		cols = new HashMap<Integer, HashSet<Integer>>();

		for (int i = 0; i < Constants.GRID_SIZE; i++) {
			boxes.put(i, new HashSet<Integer>());
			rows.put(i, new HashSet<Integer>());
			cols.put(i, new HashSet<Integer>());
		}

		v = _v;
		rng = new Random();
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
	public boolean backtrack() throws InterruptedException {

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

						// Number is not already in box/row/col
						if (!(boxes.get(box).contains(candidate) || rows.get(r).contains(candidate)
								|| cols.get(c).contains(candidate))) {

							// place the number
							addCandidate(candidate, r, c, box);
							v.setDigit(r, c, candidate);
							Thread.sleep(100);

							if (backtrack())
								return true;

							// Candidate led to invalid board state ==> clear from board/sets
							clearCandidate(candidate, r, c, box);
							v.clearDigit(r, c);
							Thread.sleep(100);
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
				System.out.print(board[r][c] + " ");
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

	private boolean completeBoard() {
		for (int i = 0; i < Constants.GRID_SIZE; i++) {
			if (!(boxes.get(i).size() == Constants.GRID_SIZE && rows.get(i).size() == Constants.GRID_SIZE
					&& cols.get(i).size() == Constants.GRID_SIZE))
				return false;
		}
		return true;
	}

	private boolean generateSmoothBrainSudoku() {

		int placed = 0;
		Integer[] digits = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		ArrayList<Integer> copy;
		clear();

		for (int r = 0; r < Constants.GRID_SIZE; r++) {
			for (int c = 0; c < Constants.GRID_SIZE; c++) {
				int box = getBoxNumber(r, c);
				copy = new ArrayList<>();
				Collections.addAll(copy, digits);

				int candidate = copy.get(rng.nextInt(copy.size()));
				opCount++;
				// The sieve
				while (boxes.get(box).contains(candidate) || rows.get(r).contains(candidate)
						|| cols.get(c).contains(candidate)) {
					opCount++;
					copy.remove((Integer) candidate);
					try {
						candidate = copy.get(rng.nextInt(copy.size()));
					} catch (Exception e) {
						return false;
					}
				}

				// We're through the sieve, the candidate is valid in this (r,c)
				boxes.get(box).add(candidate);
				rows.get(r).add(candidate);
				cols.get(c).add(candidate);
				board[r][c] = candidate;
			}
		}

		return true;
	}

	public static void main(String[] args) throws InterruptedException {
//
//		int[][] x = new int[9][9];
//		for (int i = 0; i < 81; i++) {
//			x[i / 9][i % 9] = 1;
//		}
//
//		for (int i = 0; i < 10; i++) {
//			boolean b = s.generateSmoothBrainSudoku();
//			while (!b) {
//				b = s.generateSmoothBrainSudoku();
//				System.out.println(opCount);
//			}
//			SudokuView sv = new SudokuView(s.board, x);
//			sv.setVisible(true);
//			Thread.sleep(500);
//		}
	}
}
