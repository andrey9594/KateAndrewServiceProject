package log.reader.LogReader;

import java.io.EOFException;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class Main {
	static private final Logger log = LoggerFactory.getLogger(Main.class);
	
	static private String pathToLogs;
	static private String logFormat;
	static private final String pathToProperties = "resources/config.properties";
	
	public static void main(String[] args) {
        Properties properties = new Properties();
        log.info("Loading configuration from {}...", pathToProperties);
		try {
			properties.load(new FileInputStream(new File(pathToProperties)));
		} catch (FileNotFoundException e) {
			log.error("Config file {} not found!", pathToProperties);
			e.printStackTrace();
		} catch (IOException e) {
			log.error("Can't read config file {}", pathToProperties);
			e.printStackTrace();
		}
		pathToLogs = properties.getProperty("path_to_logs");
		logFormat = properties.getProperty("log_format");
		log.info("Configuration from {} has been successfully loaded", pathToProperties);
        
		//список файлов
		File logsDirectory = new File(pathToLogs);
		File[] logFilesList = logsDirectory.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.toString().endsWith("." + logFormat);
			}
		});
		if (logFilesList == null) {
			log.error("Directory with log files {1} was't found!", pathToLogs);
			throw new NullPointerException();
		}		
		if (logFilesList.length == 0) {
			log.error("Log files was't found in {1}!", pathToLogs);
			throw new NullPointerException();
		}
		for (int i = 0; i < logFilesList.length; i++) {
			try {
				log.info("Proccessing {} started", logFilesList[i]);
				LogFormatter formatter = new LogFormatter(logFilesList[i]);
				
				while (true) {
					try {
						String xml = formatter.nextXML();
						log.debug("Received xml from log file {}:\n {}", logFilesList[i], xml);
						// ???
					} catch (EOFException e) {
						log.info("Proccessing {} completed", logFilesList[i]);
						break;
					} catch (IOException e) {
						log.error("Can't read log file {}!", logFilesList[i], e);
						e.printStackTrace();
					}
				}
			} catch (FileNotFoundException e) {
				log.error("Log file {} not excepted!", logFilesList[i], e);
				e.printStackTrace();
			}
		}

	}
}
