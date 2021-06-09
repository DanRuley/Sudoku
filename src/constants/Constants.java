package constants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public class Constants {
	public static final int CELL_PIXELS = 60;
	public static final Dimension CELL_SIZE = new Dimension(CELL_PIXELS, CELL_PIXELS);
	public static final int GRID_SIZE = 9;
	public static final int BOX_SIZE = 3;
	public static final Font NUM_FONT = new Font("Monospaced", Font.BOLD, 25);
	public static final Font NOTE_FONT = new Font("Monospaced", Font.PLAIN, 12);
	public static final Color UNSELECTED_BG = new Color(245, 245, 245);
	public static final Color SELECTED_BG = new Color(128, 238, 255);
	public static final Color HIGHLIGHTED_BG = new Color(212, 247, 255);

}
