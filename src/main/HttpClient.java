package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class HttpClient {

	Socket socket;
	boolean autoFlush = true;

	HttpClient(String url, int port) {
		try {
			InetAddress address = InetAddress.getByName(url);
			socket = new Socket(address, port);
			PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), autoFlush);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String requestTypeGET() {

		return "";

	}

	public String requestTypePOST() {
		return "";
	}
}
