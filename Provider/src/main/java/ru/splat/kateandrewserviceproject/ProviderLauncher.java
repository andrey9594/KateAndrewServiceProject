package ru.splat.kateandrewserviceproject;

/**
 * <p>
 * Class for launching several Providers
 * @author andrey
 */
public class ProviderLauncher {
	/**
	 * <p>
	 * Main method for launching several Providers
	 * @param args 
	 */
	public static void main(String[] args) {
	//	Provider provider = new Provider("resources/config_xml.properties");
		Provider provider = new Provider("resources/config_json.properties");

		provider.start(); 
	}
}
