package ru.splat.kateandrewserviceproject;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>
 * @author andrey
 * Class for transmitting information from providers 	
 *	
 */
@XmlRootElement
public class ProviderPackage {
	@XmlAttribute
	private int id;
	
	@XmlElement
	private int value;
	
	/**
	 * no-arg default constructor
	 */
	public ProviderPackage() { }
	
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
