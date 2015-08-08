package ru.splat.kateandrewserviceproject;

/**
 * <p>
 * @author andrey
 * Class for transmitting information from providers 	
 *	
 */
public class ProviderPackage {
	private int id;
	private int value;
	
	/**
	 * Initial constructor
	 * @param id
	 * @param value
	 */
	public ProviderPackage(int id, int value) {
		this.id = id;
		this.value = value;
	}
	
	/**
	 * @return id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @return value
	 */
	public int getValue() {
		return value;
	}
}
