package ru.splat.kateandrewserviceproject;   

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringBufferInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.Random;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.google.gson.Gson;

/**
 * <p>
 * @author andrey
 * Class provider of transmitting data to the server through socket
 * Possible only one connection at a time
 */
public class Provider {
	private final static int DEFAULT_PORT = 12345;
	
	private int format = 0;
	private static final int FORMAT_UNKNOWN = 0;
	private static final int FORMAT_XML = 1;
	private static final int FORMAT_JSON = 2;
	
	private int periodWaitTime = 1000;
	private int idList[];
	
	public Provider(String propertiesPath) {
		/**
		 * properties:
		 * protocol = string: xml or json
		 * periodWaitTime = int: T will be random int in (0, periodWaitTime]
		 * idList = 1,5,7 or 6-23: IDs which the Provider will send 
		 */
		Properties properties = new Properties();
		//Логируй загружаем конфигурацию
		try {
			properties.load(new FileInputStream(new File(propertiesPath)));
		} catch (FileNotFoundException e) {
			//Логируй File not found! Please, take conf.ini with options into resources/";
			e.printStackTrace();
		} catch (IOException e) {
			//Логируй ошибка чтения файла
			e.printStackTrace();
		}
		// Логируй конфигурация была загружена
		String formatString = properties.getProperty("format");
		format = formatString.equals("xml") ? FORMAT_XML : formatString.equals("json") ? FORMAT_JSON : FORMAT_UNKNOWN;
		if (format == FORMAT_UNKNOWN)
			throw new IllegalArgumentException(); // Своё исключение? 
		periodWaitTime = Integer.parseInt(properties.getProperty("period_time"));
		if (periodWaitTime <= 0)
			throw new IllegalArgumentException();
		if (properties.getProperty("idList").split(",").length != 1) {
			String[] idListString = properties.getProperty("idList").split(",");
			idList = new int[idListString.length];
			for (int i = 0; i < idList.length; i++)
				idList[i] = Integer.valueOf(idListString[i]);
		} else {
			String[] idListString = properties.getProperty("idList").split("-");
			int from = Integer.valueOf(idListString[0]);
			int to = Integer.valueOf(idListString[1]);
			idList = new int[to - from + 1];
			for (int i = from; i <= to; i++)
				idList[i - from] = i;
		}
	}
	
	private void sendXmlObject(ProviderPackage providerPackage, Socket socket) {
		// логируй отправка xml пакета
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(ProviderPackage.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			//marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			marshaller.marshal(providerPackage, out);
			out.write(0);
			out.flush();
		} catch (JAXBException | IOException e) {
			// логируй ошибку
			e.printStackTrace();
		}
	}
	
	private void sendJsonObject(ProviderPackage providerPackage, Socket socket) {
		// логируй отправка json пакета
		Gson gson = new Gson();
		String json = gson.toJson(providerPackage);
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println(json);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * main method to start a Provider 
	 * @param args
	 */
	public void start() {
		try {
			ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT);
			try {
				while (true) {
					// логируй, ждем подключение
					Socket socket = serverSocket.accept();
					// логируй, к нам подключились
					try {
						Random random = new Random();
						int currentPosInIdList = 0;
						ProviderPackage providerPackage = new ProviderPackage();
						while (!socket.isClosed()) {
							int T = random.nextInt(periodWaitTime) + 1;
							try {
								Thread.sleep(T);
							} catch (InterruptedException e) {
								// Логируй что нас прервали и мы выходим
								return;
							}
							providerPackage.setId(idList[currentPosInIdList]);
							currentPosInIdList = (currentPosInIdList + 1) % idList.length;
							providerPackage.setValue(random.nextInt()); 
							if (format == FORMAT_XML)
								sendXmlObject(providerPackage, socket);
							else
								sendJsonObject(providerPackage, socket);
						}
					} finally {
						socket.close();
					}
				}
			} finally {
				serverSocket.close();
			}
		} catch (IOException e) {
			// логируй
			e.printStackTrace();
		}
	}
}
