package log.reader.LogReader;

import static org.junit.Assert.*;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

public class LogFormatterTest {

	@Test
	public void count_xml_test() {
		final int XML_COUNT_IN_TEST_LOG_FILE = 5;
		File testingLogFile = new File("src/test/test_count_5_log.test");
		try {
			LogFormatter logFormatter = new LogFormatter(testingLogFile);
			for (int i = 0; i < XML_COUNT_IN_TEST_LOG_FILE; i++) {
				try {
					logFormatter.nextXML();
				} catch (EOFException e) {
					assertTrue("Not enought xml files! Need "
							+ XML_COUNT_IN_TEST_LOG_FILE + ", but we have only "
							+ (i + 1), false);
				} catch (IOException e) {
					assertTrue("Can't read log file", false);
				}
			}
		} catch (FileNotFoundException e) {
			assertTrue("Log file not found!", false);
		}
		assertTrue(true);
	}

}
