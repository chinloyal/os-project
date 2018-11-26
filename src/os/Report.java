package os;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Report {
	private static PrintWriter writer;
	private static FileWriter fileWriter;
	private static BufferedWriter bufferedWriter;
	private final static String FILENAME = "Report.txt";
	static {
		/*try {

			fileWriter = new FileWriter(FILENAME, true);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
//		bufferedWriter = new BufferedWriter(fileWriter);
		try {
			writer = new PrintWriter(new File(FILENAME));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void writef(String format, Object... arguments) {
		writer.printf(format, (Object[]) arguments);
	}
	
	public static String writeln(String line) {
		writer.println(line);
		return line;
	}
	
	public static void close() {
		writer.close();
	}
	
}
