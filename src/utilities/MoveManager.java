package utilities;

import java.util.Stack;

import views.Cell;

public class MoveManager {

	private Stack<Move> undoStack;

	private Stack<Move> redoStack;

	public MoveManager() {
		undoStack = new Stack<Move>();
		redoStack = new Stack<Move>();
	}                                                       

	public void processMove(Move move) {
		
	}
	
	public Move undo() {
		if (undoStack.size() == 0)
			return null;

		redoStack.push(undoStack.peek());
		return undoStack.pop();
	}

	public Move redo() {
		if (redoStack.size() == 0)
			return null;

		undoStack.push(redoStack.peek());
		return redoStack.pop();
	}

	
	/*
	 * Inverses of various moves:
	 * 
	 * cell state       move type		inverse
	 * empty            place		   	delete
	 * empty			delete			n/a
	 * empty            notate			delete
	 * user digit       place           place old digit
	 * user digit       delete          place old digit
	 * user digit       notate          place old digit
	 * note             place           place old NOTES
	 * note             delete          place old NOTES
	 * note             notate          place old NOTES
	 * 
	 */
	public void makeMove(Cell toggled, MoveType type, Integer value) {
		switch (type) {
		case delete:
			toggled.clearDigit();
			break;
		case notate:
			toggled.setNote(value);
			break;
		case place:
			toggled.setDigit(value);
			break;
		default:
			break;

		}
	}
}
