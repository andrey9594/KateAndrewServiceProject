package ru.splat.kateandrewserviceproject;

public class ProviderTest {
//
//	@Test(timeout = 60000)
//	public void testXmlGettingSeveralObjects() {
//		final int OBJECT_COUNT = 10;
//		
//		Thread providerThread = new Thread() {
//			public void run() {
//				Provider xmlProvider = new Provider("resources/config_xml.properties");
//				xmlProvider.start();
//			}
//		};
//		providerThread.start();
//		
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		
//		Socket socket = null;
//		try {
//			socket = new Socket("localhost", 34560);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		try {
//            Scanner scanner = new Scanner(socket.getInputStream());
//            for (int i = 0; i < OBJECT_COUNT; i++) {
//                String xmlString = scanner.nextLine();
//              
//                JAXBContext jaxbContext = JAXBContext.newInstance(ProviderPackage.class);
//                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
//                ProviderPackage providerPackage = (ProviderPackage) unmarshaller.unmarshal(new StringBufferInputStream(xmlString));
//                
//                System.out.println("Got xml object number: " + i);
//            }
//        } catch (JAXBException | IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                socket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//
//            }
//        }
//		
//		providerThread.interrupt();
//	}
//
//	@Test(timeout = 60000)
//	public void testJsonGettingSeveralObjects() {
//		final int OBJECT_COUNT = 10;
//		
//		Thread providerThread = new Thread() {
//			public void run() {
//				Provider jsonProvider = new Provider("resources/config_json.properties");
//				jsonProvider.start();
//			}
//		};
//		providerThread.start();
//		
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		
//		Socket socket = null;
//		try {
//			socket = new Socket("localhost", 34561);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		try {
//            Gson gson = new Gson();
//            Scanner scanner = new Scanner(socket.getInputStream());
//
//            for (int i = 0; i < OBJECT_COUNT; i++) {
//                String json = scanner.next();
//                ProviderPackage providerPackage = gson.fromJson(json, ProviderPackage.class);  
//                
//                System.out.println("Got json object number: " + i); 
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                socket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//		
//		providerThread.interrupt();
//	}
}
