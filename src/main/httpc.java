package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.HashMap;

public class httpc {
	private static String methodName;
	private static String requestType;
	private static String params;
	public static boolean enableHeaders = false;
	public static boolean enableFileWrite = false;
	private static HashMap<String, String> hashMapHeaders;
	private static BufferedWriter out;
	private static BufferedReader in;
	private static String fileName = "";

	public httpc(String url) throws IOException {
		hashMapHeaders = new HashMap<>();
//		String hostname = "www.httpbin.org";
		if (!url.contains("www.")) {
			url = "www." + url.strip();
		}
		int port = 80;
		params = "";
		fileName = "";
		InetAddress addr = InetAddress.getByName(url);
		Socket socket = new Socket(addr, port);
		out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

	}

	public static void close() throws IOException {
		enableFileWrite = false;
		enableHeaders = false;
		requestType = "";
		params = "";
		methodName = "";
		hashMapHeaders.clear();
		fileName = "";
		out.close();
		in.close();
	}

	public static void setFileName(String file) {
		fileName = file;
	}

	public String setParams(HashMap<String, String> hashMap) throws UnsupportedEncodingException {

		for (String key : hashMap.keySet()) {
			params = params
					.concat(URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(hashMap.get(key), "UTF-8") + "&");
		}

		if (params.charAt(params.length() - 1) == '&') {
			params = params.substring(0, params.length() - 2);
		}
		return params;
	}

	public static String sendGETRequest(String queryParams) throws IOException {
		System.out.println("GET * Request");

		String path = "";
		path = "/" + getMethodName();
		if (queryParams.strip().length() > 0)
			path = path.concat("?" + queryParams);

		// Send headers
		out.write("GET " + path + " HTTP/1.0\r\n");
		out.write("Host: httpbin.org\r\n");
		HashMap<String, String> headers = getHashMapHeaders();
		if (headers.isEmpty() || !headers.containsKey("Content-Type"))
			headers.put("Content-Type", "application/json");
		for (String key : headers.keySet()) {
			out.write(key + ": " + headers.get(key) + "\r\n");
		}
//		out.write("Content-Type: application/json" + "\r\n");
//		out.write("User-Agent: Concordia-HTTP/1.0\r\n");
		out.write("\r\n");
		out.flush();
		String response = readResponse(in);
		;
		if (enableFileWrite) {

			writeToFile(fileName, response);
		}
		return response;
	}

	public static String sendPOSTRequest() throws IOException {
		System.out.println(" * Request");

		String path = "/" + getMethodName();

		// Send headers
		out.write("POST " + path + " HTTP/1.0\r\n");
		out.write("Host: httpbin.org\r\n");
		if (params.length() > 0) {
			out.write("Content-Length: " + params.length() + "\r\n");

		}
		HashMap<String, String> headers = getHashMapHeaders();
		if (headers.isEmpty() || !headers.containsKey("Content-Type"))
			headers.put("Content-Type", "application/json");
		for (String key : headers.keySet()) {
			out.write(key + ": " + headers.get(key) + "\r\n");
		}

//		out.write("Content-Type: application/json\r\n");
//		out.write("User-Agent: Concordia-HTTP/1.0\r\n");
		out.write("\r\n");
		// Send parameters
		if (params.length() > 0)
			out.write(params);
		out.flush();
		String response = readResponse(in);
		if (enableFileWrite) {
			writeToFile(fileName, response);
		}
		return response;
	}

	public void setMethodName(String methodName) {
		httpc.methodName = methodName;
	}

	public static String getMethodName() {
		return methodName;
	}

	private static String readResponse(BufferedReader in) throws IOException {
		System.out.println("\n * Response");
		String response = "";
		String line;
		boolean isResponse = false;
		while ((line = in.readLine()) != null) {
			if (enableHeaders) {
				response += line + "\n";
			} else {
				if (line.strip().length() == 0) {
					isResponse = true;
				}
				if (isResponse)
					response += line + "\n";
			}

		}
		return response;
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
				+ "\n" + "-h key:value" + "\n" + "Associates headers to HTTP Request with the format 'key:value'.";
	}

	public static String helpPOST() {
		return "usage: httpc post [-v] [-h key:value] [-d inline-data] [-f file] URL" + "\n"
				+ "Post executes a HTTP POST request for a given URL with inline data or from file." + "\n" + "-v"
				+ "\t\t" + "Prints the detail of the response such as protocol, status, and headers." + "\n"
				+ " -h key:value" + "\t" + "Associates headers to HTTP Request with the format 'key:value'." + "\n"
				+ "-d string" + "\t" + "Associates an inline data to the body HTTP POST request." + "\n"
				+ "-f file Associates the content of a file to the body HTTP POST request." + "\n"
				+ "Either [-d] or [-f] can be used but not both.";
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
//    private static List<String> getContents(File file) throws IOException {
//        List<String> contents = new ArrayList<String>();
//        
//        BufferedReader input = new BufferedReader(new FileReader(file));
//        String line;
//        while ((line = input.readLine()) != null) {
//            contents.add(line);
//        }
//        input.close();
//        
//        return contents;
//    }

	private static void writeToFile(String fileName, String response) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
		writer.write(response);

		writer.close();
	}

	private boolean responseParser(String response) {
		boolean isValidResponse = false;
		return isValidResponse;
	}
}