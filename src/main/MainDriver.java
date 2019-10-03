package main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class MainDriver {

	public static void main(String[] args) {
		httpc httpcObj;

		// httpc (get|post) [-v] (-h "k:v")* [-d inline-data] [-f file] URL
		System.out.println("Welcome to the Curl Terminal");
		System.out.println("Please enter the Curl Command");
		// httpc post -h Content-Type:application/json -d '{"Assignment": 1}' -f
		// 'C:/Us ers/Nancy Goyal/Contacts/Desktop/compnetw/hello.txt'
		// http://httpbin.org/post
		while (true) {
			Scanner scanner = new Scanner(System.in);
			String inputData = scanner.nextLine();
			if (inputData.trim().toLowerCase().contains("help")) {
				String sb = "";
				if (inputData.trim().equalsIgnoreCase("httpc help")) {
					sb = httpc.help();
				} else if (inputData.trim().equalsIgnoreCase("httpc help get")) {
					sb = httpc.helpGET();
				} else if (inputData.trim().equalsIgnoreCase("httpc help post")) {
					sb = httpc.helpPOST();
				}
				System.out.println(sb);
			} else {
				String[] test = inputData.split(" ");
				String url = "";
				for (String str : test) {
					str = str.replaceAll("'", "").trim();
					str = str.replaceAll("\"", "").trim();
					if (str.matches(
							"^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/|www\\.)+[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$")) {
						url = str;

					}
				}
				inputData = inputData.replaceAll(url, "");
				String[] inputs = inputData.split(" -");
				for(int i=0;i<inputs.length;i++)
				{
					System.out.println("*********"+inputs[i]);	
				}
				
				String[] httpRequestType = inputs[0].split(" ");
				String http = httpRequestType[0];
				String requestType = httpRequestType[1];
				boolean enableVerbose = false;
				boolean enableFileWrite = false;
				boolean enableFileRead = false;
				HashMap<String, String> headerHashMap = new HashMap<String, String>();
				HashMap<String, String> dataHashMap = new HashMap<String, String>();
				String inLineData = "";
				String file = "";
				String OutputFileName = "";
				String InputFileName = "";
				for (int i = 1; i < inputs.length; i++) {
					String[] data = inputs[i].split(" ");
					if (data[0].equalsIgnoreCase("h")) {
						String val = "";
						for (int j = 1; j < data.length; j++) {
							val = val.concat(data[j]);
						}
						val = val.replaceAll("\"", "");
						val = val.replaceAll("'", "");

						String[] header = val.split(":");
						headerHashMap.put(header[0].trim(), header[1].trim());
					}
					if (data[0].equalsIgnoreCase("d")) {
						String vall = "";
						for (int j = 1; j < data.length; j++) {
							vall = vall.concat(data[j] + " ");
						}
						inLineData = vall.trim();
						String dataValue = vall.replaceAll("\\}", "").replaceAll("'", "").replaceAll("\"", "")
								.replaceAll("\\{", "");
						String[] allData = dataValue.split(",");
						for (String deta : allData) {
							String[] d = deta.split(":");
							dataHashMap.put(d[0].trim(), d[1].trim());
						}
					}

					if (data[0].equalsIgnoreCase("f")) {
						for (int j = 1; j < data.length; j++) {
							file = file.concat(data[j]);
						}
						file = file.replaceAll("\'", "").trim();
					}
					if (data[0].equalsIgnoreCase("v")) {
						enableVerbose = true;
					}
					if (data[0].equalsIgnoreCase("o")) {
						for (int j = 1; j < data.length; j++) {
							OutputFileName = OutputFileName.concat(data[j]);
						}
						
						enableFileWrite = true;
					}
					if (data[0].equalsIgnoreCase("f")) {
						for (int j = 1; j < data.length; j++) {
							InputFileName = InputFileName.concat(data[j]);
						}
						
						enableFileRead = true;
					}
				}
				System.out.println("inLineData" + inLineData);
				System.out.println(enableVerbose);
				System.out.println(enableFileWrite);
				System.out.println(enableFileRead);
				System.out.println(headerHashMap);
				System.out.println(dataHashMap);
				System.out.println(http);
				System.out.println(requestType);
				System.out.println(file);
				System.out.println(url);

				url = url.replaceAll("'", "");
				String properURL = "";
				String queryParams = "";
				if (url.contains("?")) {
					String[] urls = url.split("\\?");
					properURL = urls[0];
					queryParams = urls[1];
				} else {
					properURL = url;
				}
				System.out.println(properURL);
				System.out.println(queryParams);
				String[] checkVal = properURL.split("[^a-zA-Z0-9.-]");
				String hostName = "";
				String methodName = "";
				for (int i = 1; i < checkVal.length; i++) {
					String s = checkVal[i];
					if (!s.isEmpty()) {
						if (s.contains(".")) {
							hostName = s;
						} else {
							methodName = methodName.concat(s + "/");
						}
					}
				}

				if (methodName.charAt(methodName.length() - 1) == '/') {
					methodName = methodName.substring(0, methodName.length() - 1);
				}
				System.out.println("HostName : " + hostName);
				System.out.println("Method Name : " + methodName);
				String response = "";
				try {
					httpcObj = new httpc(hostName);
					if (enableVerbose)
						httpc.enableHeaders = true;
					if (enableFileWrite) {
						httpcObj.setFileName(OutputFileName);
						httpc.enableFileWrite = true;
					}
					if (enableFileRead) {
						httpcObj.setInputFileName(InputFileName);
						httpc.enableFileRead = true;
					}

					if (requestType.equalsIgnoreCase("get")) {
						httpcObj.setRequestType("GET");
						httpcObj.setMethodName(methodName);
						httpcObj.setHashMapHeaders(headerHashMap);
						response = httpc.sendGETRequest(queryParams);
					} else if (requestType.equalsIgnoreCase("post")) {
						httpcObj.setRequestType("POST");
						httpcObj.setMethodName(methodName);
						httpcObj.setHashMapHeaders(headerHashMap);
//						httpcObj.setParams(dataHashMap);
						httpc.inLineData = inLineData;
						response = httpc.sendPOSTRequest();
					} else
						System.out.println("Invalid request type.");
					System.out.println(response);
					httpc.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
