package ru.splat.kateandrewserviceproject;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <p>
 * @author andrey
 * Class provider of transmitting data to the server through socket
 * Possible only one connection at a time
 */
public class ProviderJSON {
	private final static int DEFAULT_PORT = 12345;
	
	private String protocol;
	private long periodWaitTime = 1000L;
	private int idList[];
	
	private static void init() {
		/**
		 * properties
		 * protocol = string: xml or json
		 * periodWaitTime: T is random in (0, waitingTime]
		 * idList
		 */
		
		
	}
	
	/**
	 * main method to start a Provider 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT);
			try {
				while (true) {
					Socket socket = serverSocket.accept();
					try {
						// xml or json
					} finally {
						socket.close();
					}
				}
			} finally {
				serverSocket.close();
			}
		} catch (IOException e) {
			//
		}
		
	}
}
