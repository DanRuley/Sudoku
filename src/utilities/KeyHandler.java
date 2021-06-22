package utilities;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import constants.Constants;
import views.SudokuView;
import views.Cell;

/**
 * Handles keyboard inputs
 * 
 * @author drslc
 *
 */
public class KeyHandler implements KeyListener {

	private SudokuView game;
	private HashMap<Character, Integer> noteKeys;
	private int keyLock;

	public KeyHandler(SudokuView _game) {
		game = _game;
		keyLock = Constants.UNFILLED;
		noteKeys = new HashMap<>();

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

		if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_RIGHT
				|| keyCode == KeyEvent.VK_LEFT) {
			handleArrows(keyCode, toggled);
		} else if (toggled == null) {
			return;
		} else if (Character.isDigit(key)) {
			key = (char) (key - '0');
			// moveManager.makeMove(toggled, MoveType.place, key);
			toggled.setDigit(key);
		} else if (noteKeys.containsKey(key)) {
			toggled.setNote(noteKeys.get(key));
		} else if (keyCode == KeyEvent.VK_BACK_SPACE || keyCode == KeyEvent.VK_DELETE) {
			toggled.clearDigit();
		} else {
			return;
		}

		keyLock = keyCode;
		// Prevents Windows alert ding
		e.consume();

		game.processMove();
	}

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
