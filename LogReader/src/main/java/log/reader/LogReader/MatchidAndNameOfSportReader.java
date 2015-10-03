package log.reader.LogReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class MatchidAndNameOfSportReader {
	private Scanner scanner;
	
	public MatchidAndNameOfSportReader(String pathToFile) throws FileNotFoundException {
		scanner = new Scanner(new File(pathToFile));
	}
	
	public Map <Integer, SportName> getSportNameForMatchidMap() {
		Map <Integer, SportName> sportNameForMatchidMap = new TreeMap<>();
		
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			
			String words[] = line.split(",");
			
			int id = Integer.parseInt(words[0]);
			words[1] = words[1].substring(1);
			words[1] = words[1].substring(0, words[1].length() - 1);	
		
			SportName sportName = null;
			switch(words[1]) {
			case "Soccer":
				sportName = SportName.SOCCER;
				break;
			case "Basketball":
				sportName = SportName.BASKETBALL;
				break;
			case "Snooker":
				sportName = SportName.SNOOKER;
				break;
			case "Ice Hockey":
				sportName = SportName.ICEHOCKEY;
				break;
			case "Volleyball":
				sportName = SportName.VOLLEYBALL;
				break;
			case "Beach volleyball":
				sportName = SportName.BEACHVOLLEAYBALL;
				break;
			case "Handball":
				sportName = SportName.HANDBALL;
				break;
			}
			sportNameForMatchidMap.put(id, sportName);
		}	
		
		return sportNameForMatchidMap;
	}
}
