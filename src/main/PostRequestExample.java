package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URLEncoder;

public class PostRequestExample {
	public static void main(String[] args) {

		try {

			String params = URLEncoder.encode("course", "UTF-8") + "=" + URLEncoder.encode("networking", "UTF-8");
			params += "&" + URLEncoder.encode("Assignment", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");

			String hostname = "www.httpbin.org";
			int port = 80;

			InetAddress addr = InetAddress.getByName(hostname);
			Socket socket = new Socket(addr, port);
			String path = "/get";

			// Send headers
			BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
			wr.write("GET " + path + " HTTP/1.0\r\n");
			wr.write("Host: httpbin.org\r\n");
			wr.write("Content-Length: " + params.length() + "\r\n");
			wr.write("Content-Type: application/json\r\n");
			wr.write("\r\n");

			// Send parameters
			wr.write(params);
			wr.flush();

			// Get response
			BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String line;

			while ((line = rd.readLine()) != null) {
				System.out.println(line);
			}

			wr.close();
			rd.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
