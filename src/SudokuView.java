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

	public static final int CELL_SIZE = 60;
	public static final int GRID_SIZE = 9;
	public static final int BOX_SIZE = 3;
	public static final Font NUM_FONT = new Font("Monospaced", Font.BOLD, 25);
	public static final Color UNSELECTED_BG = new Color(245, 245, 245);

	private JPanel hostPanel;
	private JPanel gamePanel;
	private JPanel[][] gameBox;
	private Cell[][] cells;
	private Cell toggled;
	private int toggledRow;
	private int toggledCol;
	private boolean ctrlToggled;

	int[][] board = { { 4, 9, 3, 7, 2, 6, 8, 5, 1 }, { 7, 1, 8, 3, 4, 5, 9, 2, 6 }, { 5, 6, 2, 8, 9, 1, 4, 7, 3 },
			{ 8, 3, 6, 2, 5, 4, 1, 9, 7 }, { 1, 5, 9, 6, 8, 7, 3, 4, 2 }, { 2, 7, 4, 9, 1, 3, 5, 6, 8 },
			{ 3, 2, 1, 4, 7, 9, 6, 8, 5 }, { 9, 8, 5, 1, 6, 2, 7, 3, 4 }, { 6, 4, 7, 5, 3, 8, 2, 1, 9 } };
	int[][] initState = { { 1, 1, 0, 1, 0, 0, 0, 1, 1 }, { 1, 1, 0, 1, 1, 0, 1, 1, 0 }, { 0, 1, 0, 0, 0, 0, 1, 0, 1 },
			{ 1, 0, 0, 1, 0, 0, 1, 0, 0 }, { 0, 0, 0, 0, 0, 0, 1, 1, 0 }, { 0, 1, 0, 0, 0, 0, 0, 1, 1 },
			{ 1, 0, 0, 0, 0, 1, 1, 0, 1 }, { 0, 0, 0, 0, 1, 1, 0, 0, 1 }, { 0, 0, 0, 0, 0, 1, 0, 0, 0 } };
	
	private int keyLock;

	private static final long serialVersionUID = 1L;

	public SudokuView() {
		Dimension size = new Dimension(CELL_SIZE * GRID_SIZE, CELL_SIZE * GRID_SIZE);
		this.setSize(size);
		this.setMaximumSize(size);
		this.setMinimumSize(size);
		this.setBackground(Color.BLACK);
		hostPanel = new JPanel();
		hostPanel.setLayout(new BorderLayout(5, 5));
		toggled = null;

		cells = new Cell[GRID_SIZE][GRID_SIZE];

		makeGameBoard();
		hostPanel.add(gamePanel, BorderLayout.CENTER);
		keyLock = -1;
		ctrlToggled = false;
		
		this.setContentPane(hostPanel);
		this.pack();
	}

	private void makeGameBoard() {
		gamePanel = new JPanel();
		//gameBox = new JPanel[BOX_SIZE][BOX_SIZE];
		GridLayout g = new GridLayout(GRID_SIZE, GRID_SIZE);
		gamePanel.setLayout(g);
		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				//gameBox[i][j] = new JPanel();
				//gameBox[i][j].setLayout(g);
				for (int ii = 0; ii < BOX_SIZE; ii++) {
					for (int jj = 0; jj < BOX_SIZE; jj++) {
						int row = i * 3 + ii;
						int col = j * 3 + jj;
						cells[row][col] = new Cell(row, col, board[row][col], NUM_FONT);
						cells[row][col].setEditable(false);
						cells[row][col].addActionListener(this);
						cells[row][col].addMouseListener(this);
						cells[row][col].addKeyListener(this);
						cells[row][col].setBackground(UNSELECTED_BG);
						gameBox[i][j].add(cells[row][col]);

						if (initState[row][col] > 0)
							cells[row][col].setDigit(board[row][col]);
					}
				}

				gameBox[i][j].setBorder(new LineBorder(Color.BLACK, 3, false));
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
				toggled.setBackground(UNSELECTED_BG);
			}
			toggled = c;
			toggledRow = c.getRow();
			toggledCol = c.getCol();
			System.out.println(toggledRow + ", " + toggledCol);
			c.setBackground(Color.yellow);
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
			toggled.setBackground(Color.yellow);
			oldToggled.setBackground(UNSELECTED_BG);
			keyLock = keyCode;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		System.out.println(e.getKeyCode());
		keyLock = -1;
	}

}
