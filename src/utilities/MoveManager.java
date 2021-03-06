package utilities;

import java.util.HashSet;
import java.util.Stack;

import views.Cell;
import views.Cell.state;
import views.SudokuGame;

public class MoveManager {

	private Stack<Move> undoStack;

	private Stack<Move> redoStack;

	private SudokuGame game;

	public MoveManager(SudokuGame _game) {
		undoStack = new Stack<Move>();
		redoStack = new Stack<Move>();
		game = _game;
	}

	public void processMove(Move move) {

		Cell target = move.getLocation();
		Cell.state oldState = target.getState();

		// moves on given digits have no effect.
		if (oldState == Cell.state.given)
			return;

		move.setOldState(oldState);

		switch (move.getType()) {

		// need to store any digit or notes deleted by this
		case delete:

			// nothing to do when deleting an empty cell
			if (oldState == state.empty)
				return;
			else if (oldState == state.filled)
				move.setOldDigit(target.getDigit());
			else if (oldState == state.notated)
				move.setOldNotes(target.getNotes());

			target.clearCell();
			break;

		// need to store any digit replaced by this
		case notate:
			if (oldState == state.filled)
				move.setOldDigit(target.getDigit());
			else if (oldState == state.notated)
				move.setOldNotes(target.getNotes());

			target.setNote(move.getDigit());
			break;

		// need to store notes replaced in the cell, as well as the notes in cells that
		// will be deleted.
		case place:

			// placing redundant numbers does nothing
			if (oldState == state.filled) {

				if (target.getDigit() == move.getDigit())
					return;

				move.setOldDigit(target.getDigit());
			} else if (oldState == state.notated) {
				move.setOldNotes(target.getNotes());
			}

			HashSet<Integer> sideEffects = game.getListOfAffectedNoteCells(target, move.getDigit());
			move.setAffectedNoteCells(sideEffects);

			target.setDigit(move.getDigit());
			game.setNotesInSet(sideEffects, move.getDigit());

			break;

		default:
			return;
		}

		undoStack.push(move);
		game.processMove(target);
	}

	public void undo() {
		System.out.println("undo");
		if (undoStack.size() == 0)
			return;

		Move move = undoStack.pop();
		Cell target = move.getLocation();
		state restoreState = move.getOldState();

		switch (restoreState) {
		case empty:
			target.clearCell();
			break;
		case filled:
			target.setDigit(move.getOldDigit());
			break;
		case notated:
			if (move.getOldNotes() == null)
				System.out.println("uh oh");
			target.setNotes(move.getOldNotes());
			break;
		default:
			break;
		}

		if (move.getType() == MoveType.place && move.getAffectedNoteCells() != null)
			game.setNotesInSet(move.getAffectedNoteCells(), move.getDigit());

		redoStack.push(move);
		game.processMove(target);
	}

	public void redo() {
		System.out.println("redo");

		if (redoStack.size() == 0)
			return;

		Move move = redoStack.pop();

		processMove(move);
	}

}
