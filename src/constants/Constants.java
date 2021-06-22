package constants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.Map;

public class Constants {
	
	public static final Map<?, ?> DESKTOP_HINTS = (Map<?, ?>) Toolkit.getDefaultToolkit().getDesktopProperty("awt.font.desktophints");
	public static final String fontString = "BiauKai";
	
	public static final int CELL_PIXELS = 60;
	public static final Dimension CELL_SIZE = new Dimension(CELL_PIXELS, CELL_PIXELS);
	
	public static final int GRID_SIZE = 9;
	public static final int BOX_SIZE = 3;
	
	public static final Font NUM_FONT = new Font(fontString, Font.PLAIN, 40);
	public static final Font NOTE_FONT = new Font(fontString, Font.PLAIN, 15);
	public static final Font SELECTED_NUM_FONT = new Font(fontString, Font.BOLD, 42);
	public static final Font SELECTED_NOTE_FONT = new Font(fontString, Font.BOLD, 17);
	
	public static final Color UNSELECTED_BG = new Color(255, 255, 255);
	public static final Color SAME_NUM_BG = new Color(210, 210, 210);
	public static final Color SELECTED_BG = new Color(187, 213, 252);
	public static final Color HIGHLIGHTED_BG = new Color(166, 231, 255);
	
	public static final Color FILLED_NUM = new Color(20, 50, 100);
	
	public static final int NOTE_MAGIC_VAL = 4;
	public static final int UNFILLED = -1;
}
