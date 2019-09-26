package main;

import java.util.HashMap;
import java.util.Scanner;

public class MainDriver {

	public static void main(String[] args) {

		// httpc (get|post) [-v] (-h "k:v")* [-d inline-data] [-f file] URL
		System.out.println("Welcome to the Curl Terminal");
		System.out.println("Please enter the Curl Command");
//httpc post -h Content-Type:application/json -d '{"Assignment": 1}' -f 'C:/Users/Nancy Goyal/Contacts/Desktop/compnetw/hello.txt' http://httpbin.org/post
		while (true) {
			Scanner scanner = new Scanner(System.in);
			String inputData = scanner.nextLine();
			if (inputData.trim().toLowerCase().contains("help")) {
				String sb = "";
				if (inputData.trim().equalsIgnoreCase("httpc help")) {
					sb = "httpc is a curl-like application but supports HTTP protocol only." + "\n" + " Usage:" + "\n"
							+ " httpc command [arguments]" + "\n" + "The commands are: " + "\n" + "get" + "\t"
							+ " executes a HTTP GET request and prints the response." + "\n" + "post" + "\t"
							+ " executes a HTTP POST request and prints the response." + "\n" + "help" + "\t"
							+ " prints this screen." + "\n"
							+ "Use \"httpc help [command]\" for more information about a command.";

				} else if (inputData.trim().equalsIgnoreCase("httpc help get")) {
					sb = "usage: httpc get [-v] [-h key:value] URL" + "\n"
							+ "Get executes a HTTP GET request for a given URL." + "\n" + "-v" + "\t\t"
							+ "Prints the detail of the response such as protocol, status, and headers." + "\n"
							+ "-h key:value" + "\n" + "Associates headers to HTTP Request with the format 'key:value'.";
				} else if (inputData.trim().equalsIgnoreCase("httpc help post")) {
					sb = "usage: httpc post [-v] [-h key:value] [-d inline-data] [-f file] URL" + "\n"
							+ "Post executes a HTTP POST request for a given URL with inline data or from file." + "\n"
							+ "-v" + "\t\t" + "Prints the detail of the response such as protocol, status, and headers."
							+ "\n" + " -h key:value" + "\t"
							+ "Associates headers to HTTP Request with the format 'key:value'." + "\n" + "-d string"
							+ "\t" + "Associates an inline data to the body HTTP POST request." + "\n"
							+ "-f file Associates the content of a file to the body HTTP POST request." + "\n"
							+ "Either [-d] or [-f] can be used but not both.";
				}

				System.out.println(sb);
			} else {
				String[] test = inputData.split(" ");
				String url = test[test.length - 1];
				inputData = inputData.replaceAll(url, "");
				String[] inputs = inputData.split(" -");
				String[] httpRequestType = inputs[0].split(" ");
				String http = httpRequestType[0];
				String requestType = httpRequestType[1];
				HashMap<String, String> headerHashMap = new HashMap<String, String>();
				HashMap<String, String> dataHashMap = new HashMap<String, String>();
				String file = "";

				for (int i = 1; i < inputs.length; i++) {
					String[] data = inputs[i].split(" ");
					if (data[0].equalsIgnoreCase("h")) {
						String[] header = data[1].split(":");
						headerHashMap.put(header[0].trim(), header[1].trim());
					}
					if (data[0].equalsIgnoreCase("d")) {
						String vall = "";
						for (int j = 1; j < data.length; j++) {
							vall = vall.concat(data[j]);
						}
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

				}

				System.out.println(headerHashMap);
				System.out.println(dataHashMap);
				System.out.println(http);
				System.out.println(requestType);
				System.out.println(file);
				System.out.println(url);

			}
		}

	}
}
