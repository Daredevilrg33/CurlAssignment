package main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class MainDriver {

	static Scanner userInput = new Scanner(System.in);

	public static void main(String[] args) {
		httpc httpcObj;
		MainDriver obj = new MainDriver();
		int sel = obj.displayFrontScreen();
		while (true) {
			boolean dataOrFileFlag = false;
			boolean validCurl = true;
			if (sel != 2) {
				System.out.print("Enter the CURL Command ");
				userInput = new Scanner(System.in);
				String inputData = userInput.nextLine();
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
//						if (str.matches(
//								"^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/|www\\.)+[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$")) {
						url = str;

//						}
					}
					inputData = inputData.replaceAll(url, "");
					String[] inputs = inputData.split(" -");
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
							if (requestType.equalsIgnoreCase("post")) {
								String vall = "";
								if (data.length > 0 && dataOrFileFlag != true) {
									for (int j = 1; j < data.length; j++) {
										vall = vall.concat(data[j] + " ");
									}
									inLineData = vall.replaceAll("'", "").trim();
//									String dataValue = vall.replaceAll("\\}", "").replaceAll("'", "")
//											.replaceAll("\"", "").replaceAll("\\{", "");
//									String[] allData = dataValue.split(",");
//									for (String deta : allData) {
//										String[] d = null;
//										if (deta.contains(":"))
//											d = deta.split(":");
//										else if (deta.contains("=")) {
//											d = deta.split("=");
//
//										}
//										dataHashMap.put(d[0].trim(), d[1].trim());
//									}
									dataOrFileFlag = true;
								} else {
									validCurl = false;
									break;
								}
							} else {
								validCurl = false;
								break;
							}
						}
						if (data[0].equalsIgnoreCase("f")) {
							if (requestType.equalsIgnoreCase("post")) {
								if (data.length > 0 && dataOrFileFlag != true) {
									for (int j = 1; j < data.length; j++) {
										file = file.concat(data[j]);
									}
									file = file.replaceAll("\'", "").trim();
									dataOrFileFlag = true;
								} else {

									validCurl = false;
									break;
								}
							} else {
								validCurl = false;
								break;
							}
						}
						if (data[0].equalsIgnoreCase("v")) {
							enableVerbose = true;
						}
						if (data[0].equalsIgnoreCase("o")) {
							for (int j = 1; j < data.length; j++) {
								OutputFileName = OutputFileName.concat(data[j]);
								if (OutputFileName.contains("'"))
									OutputFileName = OutputFileName.replaceAll("'", "");
								if (OutputFileName.contains("\""))
									OutputFileName = OutputFileName.replaceAll("\"", "");
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

					if (validCurl) {
//						System.out.println("inLineData" + inLineData);
//						System.out.println(enableVerbose);
//						System.out.println(enableFileWrite);
//						System.out.println(enableFileRead);
//						System.out.println(headerHashMap);
//						System.out.println(dataHashMap);
//						System.out.println(http);
//						System.out.println(requestType);
//						System.out.println(file);
//						System.out.println(url);

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
						if (!(properURL.contains("http://") | properURL.contains("https://"))) {
							properURL = "http://" + properURL.trim();
						}
						System.out.println(properURL);
//						System.out.println(queryParams);
//						String[] checkVal = properURL.split("[^a-zA-Z0-9.-]");
						String[] checkVal = properURL.split("/");
						String hostName = checkVal[2];
						String methodName = "";
						for (int i = 3; i < checkVal.length; i++) {
							String s = checkVal[i];
							if (!s.isEmpty()) {
								methodName = methodName.concat(s + "/");

							}
						}
						if (methodName.length() > 0) {
							if (methodName.charAt(methodName.length() - 1) == '/') {
								methodName = methodName.substring(0, methodName.length() - 1);
							}
						}

//						System.out.println("HostName : " + hostName);
//						System.out.println("Method Name : " + methodName);
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
								response = httpc.sendRequest(queryParams, requestType);
							} else if (requestType.equalsIgnoreCase("post")) {
								httpcObj.setRequestType("POST");
								httpcObj.setMethodName(methodName);
								httpcObj.setHashMapHeaders(headerHashMap);
								// httpcObj.setParams(dataHashMap);
								httpc.inLineData = inLineData;
								response = httpc.sendRequest(queryParams, requestType);
							} else
								System.out.println("Invalid request type.");
							System.out.println(response);
							httpc.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println("Connection Refused !!!");
						}
					} else {
						System.out.println("Incorrect Curl Command");
					}
				}
			}

			else {
				System.out.print("===========EXIT the process================ ");
				break;
			}
			sel = obj.displayFrontScreen();
		}
	}

	private int displayFrontScreen() {
		System.out.println("*************************************************");
		System.out.println("Welcome to the Curl Terminal");
		System.out.println("*************************************************");
		System.out.println("Press 1  to enter curl command");
		System.out.println("Press 2 to Exit the process.");
		return userInput.nextInt();
	}

}
