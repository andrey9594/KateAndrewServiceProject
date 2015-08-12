package ru.splat.kateandrewserviceproject;

import java.io.IOException;
import java.io.StringBufferInputStream;
import java.net.Socket;
import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class MyServer {

	public static void main(String[] args) throws JAXBException {
		try {
			Socket socket = new Socket("localhost", 34561);
			try {
				Scanner scanner = new Scanner(socket.getInputStream());
				while (true) {
					String xmlString = scanner.nextLine();
					System.out.println(xmlString);
				//	JAXBContext jaxbContext = JAXBContext.newInstance(ProviderPackage.class);
				//	Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				//	ProviderPackage providerPackage = (ProviderPackage)unmarshaller.unmarshal(new StringBufferInputStream(xmlString));
				//	System.out.println("id = " + providerPackage.getId() + ", value = " + providerPackage.getValue());
				}
			} finally {
				socket.close();
			}
		} catch (IOException e) {
			System.out.println("Error here!");
			e.printStackTrace();
		}

	}

}
