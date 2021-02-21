package sudoku;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class SudokuView extends JFrame implements ActionListener, MouseListener, KeyListener {

	private Cell[][] cells;
	private JPanel hostPanel;
	private JPanel gamePanel;
	private JPanel[][] gameBox;
	private GameModel model;

	private static final long serialVersionUID = 1L;

	public SudokuView() {
		Dimension size = new Dimension(Constants.CELL_SIZE * Constants.GRID_SIZE,
				Constants.CELL_SIZE * Constants.GRID_SIZE);
		this.setMaximumSize(size);
		this.setMinimumSize(size);
		this.setResizable(false);
		this.setBackground(Color.BLACK);
		hostPanel = new JPanel();
		hostPanel.setLayout(new BorderLayout(5, 5));

		model = new GameModel();
		cells = model.getCells();

		makeGameBoard();
		hostPanel.add(gamePanel, BorderLayout.CENTER);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(hostPanel);
		this.pack();
	}

	private void makeGameBoard() {
		gamePanel = new JPanel();
		gameBox = new JPanel[Constants.BOX_SIZE][Constants.BOX_SIZE];
		GridLayout g = new GridLayout(Constants.BOX_SIZE, Constants.BOX_SIZE);
		gamePanel.setLayout(g);
		for (int i = 0; i < Constants.BOX_SIZE; i++) {
			for (int j = 0; j < Constants.BOX_SIZE; j++) {
				gameBox[i][j] = new JPanel();
				gameBox[i][j].setLayout(g);
				for (int ii = 0; ii < Constants.BOX_SIZE; ii++) {
					for (int jj = 0; jj < Constants.BOX_SIZE; jj++) {
						int row = i * 3 + ii;
						int col = j * 3 + jj;
						cells[row][col] = new Cell(row, col, model.getBoard()[row][col], Constants.NUM_FONT);

						gamePanel.add(cells[row][col]);

						gameBox[i][j].add(cells[row][col]);

						if (initState[row][col] > 0)
							cells[row][col].setDigit(board[row][col]);
					}
				}
				gameBox[i][j].setBorder(new LineBorder(Color.BLACK, 2, false));
				gamePanel.add(gameBox[i][j]);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

	public static void main(String[] args) {
		SudokuView s = new SudokuView();
		s.setVisible(true);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {

		if (e.getSource() instanceof Cell) {
			Cell c = (Cell) e.getSource();
			if (toggled != null) {
				toggled.setBackground(Constants.UNSELECTED_BG);
			}
			toggled = c;
			toggledRow = c.getRow();
			toggledCol = c.getCol();
			System.out.println(toggledRow + ", " + toggledCol);
			c.setBackground(Constants.SELECTED_BG);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

		Cell oldToggled = toggled;
		int keyCode = e.getKeyCode();
		System.out.println(keyCode);

		if (keyLock == keyCode || toggled == null && e.getSource() instanceof Cell)
			return;

		char key = e.getKeyChar();
		if (Character.isDigit(key)) {
			key = (char) (key - '0');
			if (key > 0 && initState[toggledRow][toggledCol] == 0)
				toggled.setDigit((int) key);

			return;
		}

		if (keyCode == KeyEvent.VK_BACK_SPACE || keyCode == KeyEvent.VK_DELETE) {
			toggled.clearDigit();

			// Prevents Windows alert ding
			e.consume();
			return;
		}
		if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_RIGHT
				|| keyCode == KeyEvent.VK_LEFT) {
			switch (keyCode) {
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
			default:
				break;
			}

			toggled = cells[toggledRow][toggledCol];
			toggled.setBackground(Constants.SELECTED_BG);
			oldToggled.setBackground(Constants.UNSELECTED_BG);
			keyLock = keyCode;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		System.out.println(e.getKeyCode());
		keyLock = -1;
	}

}
