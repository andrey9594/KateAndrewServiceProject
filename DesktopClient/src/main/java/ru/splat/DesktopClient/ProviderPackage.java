package ru.splat.DesktopClient;

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

	private int id;
	
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
	@XmlAttribute
	public int getId() {
		return id;
	}
	
	/**
	 * @return value
	 */
	@XmlElement
	public int getValue() {
		return value;
	}
	
	/**
	 * setId
	 * @param id new id value
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * setValue
	 * @param value new value 
	 */
	public void setValue(int value) {
		this.value = value;
	}
	
	/**
	 * Override method for gson library (Object -> json, json -> Object)
	 * @return String of the form 'DataObject [id = %num, value = %value]' 
	 */
	@Override
	public String toString() {
		return "DataObject [id=" + id + ", value=" + value + "]";		
	}
}
