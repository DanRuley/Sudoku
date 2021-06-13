package constants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public class Constants {
	public static final int CELL_PIXELS = 60;
	public static final Dimension CELL_SIZE = new Dimension(CELL_PIXELS, CELL_PIXELS);
	public static final int GRID_SIZE = 9;
	public static final int BOX_SIZE = 3;
	public static final Font NUM_FONT = new Font("Monospaced", Font.PLAIN, 40);
	public static final Font NOTE_FONT = new Font("Monospaced", Font.PLAIN, 15);
	public static final Font SELECTED_NUM_FONT = new Font("Monospaced", Font.BOLD, 40);
	public static final Font SELECTED_NOTE_FONT = new Font("Monospaced", Font.BOLD, 15);
	public static final Color UNSELECTED_BG = new Color(245, 245, 245);
	public static final Color UNSELECTED_BG_INITIAL = new Color(225, 225, 225);
	public static final Color SELECTED_BG = new Color(166, 231, 255);
	public static final Color HIGHLIGHTED_BG = new Color(166, 231, 255);
	public static final int NOTE_MAGIC_VAL = 4;
	public static final int UNFILLED = -1;
}
