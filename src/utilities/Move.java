package utilities;

public class Move {

	private Cell location;
	private MoveType type;

	public Move(Cell _location, MoveType _type) {
		location = _location;
		type = _type;
	}

	public Cell getLocation() {
		return location;
	}

	public MoveType getType() {
		return type;
	}
}