package utilities;

import java.util.ArrayList;

import views.Cell;

public class Move {

	private Cell location;
	private MoveType type;
	private Integer value;

	/*
	 * Stores the old notes overwritten by a move (e.g. delete/place digit) notate
	 * moves don't really need to store this, you can just call the setNote Cell
	 * method when undoing.
	 */
	private boolean[] notes;

	/*
	 * Only really relevant for placing digits - this will store a list of
	 * coordinates that contain a note that see the given Cell. So when a digit is
	 * placed, these notes are deleted but their locations are stored here. That
	 * way, if the move is undone, these note deletions can be reverted.
	 */
	private ArrayList<Integer> affectedNoteCells;

	public Move(Cell _location, MoveType _type, Integer _value) {
		location = _location;
		type = _type;
		value = _value;
	}

	public Cell getLocation() {
		return location;
	}

	public MoveType getType() {
		return type;
	}

	public Integer getValue() {
		return value;
	}

	public boolean[] getNotes() {
		return notes;
	}

	public void setNotes(boolean[] notes) {
		this.notes = notes;
	}
}