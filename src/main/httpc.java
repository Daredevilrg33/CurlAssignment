package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

public class httpc {
	private static String methodName;
	private static String requestType;

	public static boolean enableHeaders = false;
	public static boolean enableFileWrite = false;
	public static boolean enableFileRead = false;
	private static HashMap<String, String> hashMapHeaders;
	private static BufferedWriter out;
	private static BufferedReader in;
	private static String fileName = "";
	private static String inputFileName = "";
	private static int port = 8080;
	private static String queryParameters = "";

	public static String responseData = "";
	public static String inLineData = "";;
	public static String hostName = "";

	public httpc(String url) throws IOException {
		hashMapHeaders = new HashMap<>();
		fileName = "";
		inputFileName = "";
		hostName = url;
		// if (!url.contains("www.")) {
		// url = "www." + url.trim();
		// }
		initializeSocket(url);
	}

	private static void initializeSocket(String url) throws IOException {
		// TODO Auto-generated method stub
		// System.out.println(url);
		// System.out.println(url);
		InetAddress addr = InetAddress.getByName(url);
		Socket socket = new Socket(addr, port);
		out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	public static void close() throws IOException {
		enableFileWrite = false;
		enableFileRead = false;
		enableHeaders = false;
		requestType = "";
		methodName = "";
		hostName = "";
		hashMapHeaders.clear();
		fileName = "";
		queryParameters = "";
		inLineData = "";
		responseData = "";
		inputFileName = "";
		out.close();
		in.close();
	}

	public static void setFileName(String file) {
		fileName = file;
	}

	public static void setInputFileName(String file) {
		inputFileName = file;
	}

	public static String sendGETRequest(String queryParams) throws IOException {
		// System.out.println("GET * Request");
		queryParameters = queryParams;
		String path = "";
		path = "/" + getMethodName();
		if (queryParameters.trim().length() > 0)
			path = path.concat("?" + queryParams);

		// Send headers
		out.write("GET " + path + " HTTP/1.0\r\n");
		out.write("Host: " + hostName.trim() + "\r\n");
		HashMap<String, String> headers = getHashMapHeaders();
		for (String key : headers.keySet()) {
			out.write(key + ": " + headers.get(key) + "\r\n");
		}
		out.write("\r\n");
		out.flush();

		responseData = readResponse(in);
		// System.out.println("Response Data " + responseData);
		// System.out.println("\n\n *** response sendGETRequest::: " + responseData);
		// System.out.println(responseData);
		if (responseParser()) {

			return responseData;

		} else
			return "";
	}

	public static String sendRequest(String queryParams, String requestType) throws IOException {
		boolean isResponse = false;
		String verboseResponse = "";
		if (requestType.equalsIgnoreCase("get")) {
			responseData = sendGETRequest(queryParams);
		} else {
			responseData = sendPOSTRequest(queryParams);
		}

		if (!enableHeaders) {
			for (String str : responseData.split("\n")) {
				if (isResponse)
					verboseResponse = verboseResponse.concat(str + "\n");
				if (str.trim().length() <= 0) {
					isResponse = true;
				}
			}
		}

		if (!enableHeaders)
			responseData = verboseResponse;
		if (enableFileWrite) {

			writeToFile(fileName, responseData);
			responseData = "";
		}
		return responseData;

	}

	public static String sendPOSTRequest(String queryParams) throws IOException {
		// System.out.println(" * Request");
		queryParameters = queryParams;
		String path = "/" + getMethodName();
		if (queryParameters.trim().length() > 0)
			path = path.concat("?" + queryParams);

		// Send headers
		out.write("POST " + path + " HTTP/1.0\r\n");
		out.write("Host:" + hostName.trim() + "\r\n");

		if (inLineData.length() > 0) {
			out.write("Content-Length: " + inLineData.length() + "\r\n");

		}
		String fileContent = "";
		if (enableFileRead) {
			fileContent = getContents(inputFileName);
		}
		if (fileContent.length() > 0) {
			out.write("Content-Length: " + fileContent.length() + "\r\n");

		}
		HashMap<String, String> headers = getHashMapHeaders();
		if (headers.isEmpty() || !headers.containsKey("Content-Type"))
			headers.put("Content-Type", "application/json");
		for (String key : headers.keySet()) {
			out.write(key + ": " + headers.get(key) + "\r\n");
		}
		out.write("\r\n");
		if (inLineData.length() > 0)
			out.write(inLineData);

		if (fileContent.length() > 0)
			out.write(fileContent);

		out.flush();
		responseData = readResponse(in);
		// System.out.println("Response Data " + responseData);
		boolean isResponse = false;
		String verboseResponse = "";
		if (responseParser()) {
			return responseData;
		} else
			return "";
	}

	public void setMethodName(String methodName) {
		httpc.methodName = methodName;
	}

	public static String getMethodName() {
		return methodName;
	}

	private static String readResponse(BufferedReader in) throws IOException {

		String response = "";
		String line;
		while ((line = in.readLine()) != null) {
			response += line + "\n";
			line = null;
		}
		return response.toString();

	}

	public static String help() {
		return "httpc is a curl-like application but supports HTTP protocol only." + "\n" + " Usage:" + "\n"
				+ " httpc command [arguments]" + "\n" + "The commands are: " + "\n" + "get" + "\t"
				+ " executes a HTTP GET request and prints the response." + "\n" + "post" + "\t"
				+ " executes a HTTP POST request and prints the response." + "\n" + "help" + "\t"
				+ " prints this screen." + "\n" + "Use \"httpc help [command]\" for more information about a command.";
	}

	public static String helpGET() {
		return "usage: httpc get [-v] [-h key:value] URL" + "\n" + "Get executes a HTTP GET request for a given URL."
				+ "\n" + "-v" + "\t\t" + "Prints the detail of the response such as protocol, status, and headers."
				+ "\n" + "-h key:value" + "\n" + "-o write response to a given file name." + "\n"
				+ "Associates headers to HTTP Request with the format 'key:value'.";
	}

	public static String helpPOST() {
		return "usage: httpc post [-v] [-h key:value] [-d inline-data] [-f file] URL" + "\n"
				+ "Post executes a HTTP POST request for a given URL with inline data or from file." + "\n" + "-v"
				+ "\t\t" + "Prints the detail of the response such as protocol, status, and headers." + "\n"
				+ "-h key:value" + "\t" + "Associates headers to HTTP Request with the format 'key:value'." + "\n"
				+ "-d string" + "\t" + "Associates an inline data to the body HTTP POST request." + "\n"
				+ "-f file Associates the content of a file to the body HTTP POST request." + "\n"
				+ "-o write response to a given file name." + "\n" + "Either [-d] or [-f] can be used but not both.";
	}

	public void setRequestType(String requestType) {
		httpc.requestType = requestType;
	}

	public static String getRequestType() {
		return requestType;
	}

	public void setHashMapHeaders(HashMap<String, String> hashMap) {
		hashMapHeaders.putAll(hashMap);
	}

	private static HashMap<String, String> getHashMapHeaders() {
		return hashMapHeaders;
	}

	private static String getContents(String filePath) throws IOException {
		String contents = "";
		BufferedReader input = new BufferedReader(new FileReader("./" + filePath));
		String line;
		while ((line = input.readLine()) != null) {
			contents = contents.concat(line);
		}
		input.close();

		return contents;
	}

	private static void writeToFile(String fileName, String response) throws IOException {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(new File(fileName)));
			writer.write(response);
		} catch (IOException ex) {
			System.err.println("An IOException was caught!");
			ex.printStackTrace();
		} finally {
			writer.close();
		}
	}

	private static boolean responseParser() {
		boolean isValidResponse = false;

//		System.out.println("Response Data : " + responseData);
		String[] splitResponse = responseData.split("\n");
		boolean fileDownload = false;
		String contentType = "";
		String ext = "";
		for (int i = 0; i < splitResponse.length; i++) {
			String str = splitResponse[i];
			String[] arr = str.split(":");
			if (i == 0) {
				String[] data = str.split(" ");
				if (Integer.parseInt(data[1]) >= 300 && Integer.parseInt(data[1]) < 306) {
					isValidResponse = false;
				} else {
					isValidResponse = true;
					// break;
				}
			} else if (!isValidResponse) {
				if (arr[0].equalsIgnoreCase("location")) {
					String d = "";
					for (int j = 1; j < arr.length; j++)
						d = d.concat(arr[j] + ":");
					try {
						d = d.substring(0, d.length() - 1);
						// System.out.println(d);
						d = parseURL(d);
						// System.out.println(d);
						initializeSocket(d);
						if (requestType.equalsIgnoreCase("GET")) {
							responseData = sendGETRequest(queryParameters);
							return true;
						} else {
							responseData = sendPOSTRequest(queryParameters);
							return true;
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			} else {
				if (arr[0].trim().equalsIgnoreCase("Content-Disposition")) {
					if (arr[1].trim().equalsIgnoreCase("attachment")) {
						fileDownload = true;
					}
				}

				if (arr[0].trim().equalsIgnoreCase("Content-Type")) {
					contentType = arr[1].trim();
					System.out.println("contentType " + contentType);
					if (contentType.equalsIgnoreCase("application/json"))
						ext = "json";
					if (contentType.equalsIgnoreCase("application/xml") || contentType.equalsIgnoreCase("text/xml"))
						ext = "xml";
					if (contentType.equalsIgnoreCase("application/pdf"))
						ext = "pdf";
					if (contentType.equalsIgnoreCase("text/html"))
						ext = "html";
					if (contentType.equalsIgnoreCase("text/plain"))
						ext = "txt";
					if (contentType.equalsIgnoreCase("text/css"))
						ext = "css";
					if (contentType.equalsIgnoreCase("text/csv"))
						ext = "csv";
				}

			}

		}
		if (isValidResponse && fileDownload)
			savingAttachment(responseData, ext);

		return isValidResponse;
	}

	public static void savingAttachment(String responseData, String ext) {
		boolean isResponse = false;
		String verboseResponse = "";
		for (String str : responseData.split("\n")) {
			if (isResponse)
				verboseResponse = verboseResponse.concat(str + "\n");
			if (str.trim().length() <= 0) {
				isResponse = true;
			}
		}

		BufferedWriter writer = null;
		String fileN = "";
		if (methodName.indexOf("/") == -1)
			fileN = methodName;
		else
			fileN = methodName.substring(methodName.lastIndexOf('/') + 1, methodName.length());
		if (!fileN.contains("."))
			fileN = fileN + "." + ext;
		try {
			writer = new BufferedWriter(new FileWriter(new File(fileN)));
			writer.write(verboseResponse);
		} catch (IOException ex) {
			System.err.println("An IOException was caught!");
			ex.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
				ex.getMessage();
			}
		}
	}

	public static String parseURL(String url) {
		String[] checkVal = url.trim().split("[^a-zA-Z0-9.-]");
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
		// System.out.println("HostName : " + hostName);
		// System.out.println("Method Name : " + methodName);
		httpc.methodName = methodName;
		if (hostName.trim().isEmpty()) {
			hostName = httpc.hostName;
		}

		return hostName;
	}
}