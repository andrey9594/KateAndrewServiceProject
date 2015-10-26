package KateAndrewService;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import matchstatistic.MatchType;

/**
 * 
 * @author Andrey
 * Class for reading information about sport matches from .csv file
 */
public class MatchidAndNameOfSportReader {
	private Scanner scanner;
	
	/**
	 * Main constructor 
	 * @param pathToFile Path to .csv file
	 * @throws FileNotFoundException
	 */
	public MatchidAndNameOfSportReader(String pathToFile) throws FileNotFoundException {
		scanner = new Scanner(new File(pathToFile));
	}
	
	public Map <Integer, MatchType> getSportNameForMatchidMap() {
		Map <Integer, MatchType> sportNameForMatchidMap = new TreeMap<>();
		
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			
			String words[] = line.split(",");
			
			int id = Integer.parseInt(words[0]);
			words[1] = words[1].substring(1);
			words[1] = words[1].substring(0, words[1].length() - 1);	
		
			MatchType sportName = null;
			switch(words[1]) {
			case "Soccer":
				sportName = MatchType.FOOTBALL;
				break;
			case "Basketball":
				sportName = MatchType.BASKETBALL;
				break;
			case "Snooker":
				sportName = MatchType.SNOOKER;
				break;
			case "Ice Hockey":
				sportName = MatchType.ICE_HOCKEY;
				break;
			case "Volleyball":
				sportName = MatchType.VOLLEYBALL;
				break;
			case "Beach volleyball":
				sportName = MatchType.VOLLEYBALL_BEACH;
				break;
			case "Handball":
				sportName = MatchType.HANDBALL;
				break;
			}
			sportNameForMatchidMap.put(id, sportName);
		}	
		
		return sportNameForMatchidMap;
	}
}
