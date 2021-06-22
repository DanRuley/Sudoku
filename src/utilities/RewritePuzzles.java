package utilities;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class RewritePuzzles {

	public static void main(String[] args) {
		String basePath = "C:\\Users\\drslc\\OneDrive\\Documents\\GitHub\\Sudoku\\src\\puzzles\\rated";
		for (int i = 1; i <= 6; i++) {
			String fullPath = basePath + i + ".txt";
			try (Scanner s = new Scanner(new File(fullPath))) {
				ArrayList<String> lines = new ArrayList<>();
				while (s.hasNextLine()) {
					String l = s.nextLine();
					if (l.length() == 81)
						lines.add(l + "\n");
				}
				s.close();

				FileWriter fw = new FileWriter(new File(fullPath));
				fw.write(lines.size() + "\n");
				for (String line : lines)
					fw.write(line);
				fw.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
