package utilities;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import constants.Constants;
import views.SudokuGame;
import views.Cell;

/**
 * Handles keyboard inputs
 * 
 * @author drslc
 *
 */
public class KeyHandler implements KeyListener {

	private SudokuGame game;
	private HashMap<Character, Integer> noteKeys;
	private int keyLock;
	private MoveManager moveManager;

	public KeyHandler(SudokuGame _game, MoveManager _moveManager) {
		game = _game;
		keyLock = Constants.UNFILLED;
		noteKeys = new HashMap<>();
		moveManager = _moveManager;

		int n = 1;
		for (Character c : new char[] { '!', '@', '#', '$', '%', '^', '&', '*', '(' }) {
			noteKeys.put(c, n++);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {

		int keyCode = e.getKeyCode();
		char key = e.getKeyChar();

		if (keyLock == keyCode)
			return;

		Cell toggled = game.getToggled();

		// The key event is not necessarily an actual move (e.g. arrows or other
		// irrelevant keys)
		Move move = null;

		if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_RIGHT
				|| keyCode == KeyEvent.VK_LEFT) {
			handleArrows(keyCode, toggled);
		} else if (toggled == null) {
			return;
		} else if (Character.isDigit(key)) {
			key = (char) (key - '0');
			toggled.setDigit(key);
			move = new Move(toggled, MoveType.place, (int) key);
		} else if (noteKeys.containsKey(key)) {
			toggled.setNote(noteKeys.get(key));
			move = new Move(toggled, MoveType.notate, noteKeys.get(key));
		} else if (keyCode == KeyEvent.VK_BACK_SPACE || keyCode == KeyEvent.VK_DELETE) {
			toggled.clearDigit();
			move = new Move(toggled, MoveType.delete, null);
		} else {
			return;
		}

		keyLock = keyCode;
		// Prevents Windows alert ding
		e.consume();

		if (move != null)
			moveManager.processMove(move);

		game.processMove();
	}

	// arrow handling merely changes toggled keys, no need to invoke a move
	private void handleArrows(int code, Cell toggled) {

		int toggledRow, toggledCol;

		// if no cell is selected, begin at (0,0)
		if (toggled == null) {
			toggledRow = 0;
			toggledCol = 0;
		}

		// Otherwise, adjust toggled position accordingly.
		else {
			toggledRow = toggled.getRow();
			toggledCol = toggled.getCol();
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

		game.setToggled(toggledRow, toggledCol);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keyLock = Constants.UNFILLED;

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

}
