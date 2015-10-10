package log.reader.LogReader;

import java.io.EOFException;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.TreeSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import generated.EventList;

/**
 * Main class for getting information from log files
 */
public class Main {
    static private final Logger log = LoggerFactory.getLogger(Main.class);

    static private final String pathToProperties = "resources/config.properties";

    static private String pathToLogs;
    static private String logFormat;
    static private String pathToMatchidFile;

    static private Map<Integer, SportName> matchidToSportName;

    /**
     * Main method for getting events from log files
     * 
     * @param args
     */
    public static void main(String[] args) {
	/**
	 * loading properties from pathToProperties
	 */
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
	pathToLogs = properties.getProperty("path_to_logs"); // where log files are
	logFormat = properties.getProperty("log_format"); // log, txt or another
	pathToMatchidFile = properties.getProperty("path_to_matchid_list"); // path to files with matchid,nameofsport
	log.info("Configuration from {} has been successfully loaded", pathToProperties);

	MatchidAndNameOfSportReader reader = null;
	try {
	    reader = new MatchidAndNameOfSportReader(pathToMatchidFile);
	} catch (FileNotFoundException e) {
	    log.error("File witch map matchid->nameofsport not found in {}!", pathToMatchidFile, e);
	    e.printStackTrace();
	}
	matchidToSportName = reader.getSportNameForMatchidMap();

	/**
	 * Scanning directory and finding all log files with extension:
	 * *.logFormat
	 */
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
		log.info("Processing {} started", logFilesList[i]);
		LogFormatter formatter = new LogFormatter(logFilesList[i]);

		/**
		 * until the end of current log file not reached
		 */
		while (true) {
		    try {
			String xml = formatter.nextXML();
			log.debug("Received xml from log file {}:\n {}", logFilesList[i], xml);
			/**
			 * xml starting with <tag attributes> or <tag>
			 */
			StringBuilder s = new StringBuilder("");
			for (int j = 0; j < xml.length() && xml.charAt(j) != ' ' && xml.charAt(j) != '>'; j++)
			    s.append(xml.charAt(j));
			/**
			 * we only interested in event_list tag
			 */
			if (s.toString().equals("<event_list") || s.toString().equals("<event_list>")) {
			    try {
				JAXBContext jaxbContext = JAXBContext.newInstance(EventList.class);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				EventList eventList = (EventList) unmarshaller
					.unmarshal(new StringBufferInputStream(xml));

				for (int j = 0; j < eventList.getEvent().size(); j++) {
				    int curSportId = matchidToSportName.get(eventList.getEvent().get(j).getMatchid())
					    .ordinal();
				    log.debug("Sport name of match with {} id is {}", curSportId,
					    matchidToSportName.get(curSportId).name());
				    for (String string : eventList.getEvent().get(j).getStatistics()) {
					// вот здесь вся статистика сидит, нужно парсить строку 
					// и изымать код события
					// и использовать отображение код события -> энум тип статистики
				    }
				}

			    } catch (JAXBException e) {
				log.error("Problem with JAXB", e);
				e.printStackTrace();
			    }
			}
		    } catch (EOFException e) {
			log.info("Processing {} completed. {}/{} done.", logFilesList[i], (i + 1), logFilesList.length);
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
