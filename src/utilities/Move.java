package utilities;

import java.util.HashSet;

import views.Cell;
import views.Cell.state;

public class Move {

	private Cell location;
	private MoveType type;
	private Integer digit;
	private state oldState;

	/*
	 * Stores the old notes overwritten by a move (e.g. delete/place digit) notate
	 * moves don't really need to store this, you can just call the setNote Cell
	 * method when undoing.
	 */
	private boolean[] oldNotes;

	private Integer oldDigit;

	/*
	 * Only really relevant for placing digits - this will store a list of
	 * coordinates that contain a note that see the given Cell. So when a digit is
	 * placed, these notes are deleted but their locations are stored here. That
	 * way, if the move is undone, these note deletions can be reverted.
	 */
	private HashSet<Integer> affectedNoteCells;

	public Move(Cell _location, MoveType _type, Integer _value) {
		location = _location;
		type = _type;
		digit = _value;
	}

	public Cell getLocation() {
		return location;
	}

	public MoveType getType() {
		return type;
	}

	public Integer getDigit() {
		return digit;
	}

	public Integer getOldDigit() {
		return oldDigit;
	}

	public void setOldDigit(Integer oldDigit) {
		this.oldDigit = oldDigit;
	}

	public boolean[] getOldNotes() {
		return oldNotes;
	}

	public void setOldNotes(boolean[] oldNotes) {
		this.oldNotes = new boolean[10];
		for (int i = 0; i < oldNotes.length; i++)
			this.oldNotes[i] = oldNotes[i];
	}

	public HashSet<Integer> getAffectedNoteCells() {
		return affectedNoteCells;
	}

	public void setAffectedNoteCells(HashSet<Integer> affectedNoteCells) {
		this.affectedNoteCells = affectedNoteCells;
	}

	public state getOldState() {
		return oldState;
	}

	public void setOldState(state oldState) {
		this.oldState = oldState;
	}

	@Override
	public String toString() {
		return "[Type: " + type + ", Old State: " + oldState + ", value: " + digit + "]";
	}
}