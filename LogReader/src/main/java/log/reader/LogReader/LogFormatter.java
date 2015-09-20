package log.reader.LogReader;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LogFormatter {	
	private BufferedReader bufferedReader;
	
	public LogFormatter(File logFile) throws FileNotFoundException {
		bufferedReader = new BufferedReader(new FileReader(logFile));
	}
	
	public String nextXML() throws EOFException, IOException {
		String xml = "";
		while (true) {
			String currentLine = nextLine();
			if (currentLine == null)
				throw new EOFException();
			if (currentLine.length() != 0 && currentLine.charAt(0) == '<') {
				xml = currentLine;
				break;
			}
		}
		return xml;
	}
	
	private String nextLine() throws IOException {
		return bufferedReader.readLine();	
	}
	
}
