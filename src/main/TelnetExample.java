package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class TelnetExample {
	public static void main(String[] args) throws Exception {
		InetAddress addr = InetAddress.getByName("www.httpbin.org");
		Socket socket = new Socket(addr, 80);
		boolean autoflush = true;
		PrintWriter out = new PrintWriter(socket.getOutputStream(), autoflush);
		BufferedReader in = new BufferedReader(

				new InputStreamReader(socket.getInputStream()));
		// send an HTTP request to the web server
		out.println("GET /status/418 HTTP/1.0");
		out.println("Host: httpbin.org");
		out.println("Connection: Close");
		out.println();

		// read the response
		boolean loop = true;
		StringBuilder sb = new StringBuilder(8096);
		while (loop) {
			if (in.ready()) {
				int i = 0;
				while (i != -1) {
					i = in.read();
					sb.append((char) i);
				}
				loop = false;
			}
		}
		System.out.println(sb.toString());
		socket.close();
	}
}
