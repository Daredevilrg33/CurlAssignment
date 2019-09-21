package main;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class httpc {
	
		public httpc() throws IOException
		{
			String[] getParams = new String[5];
	    	getParams[0]="?course=networking&assignment=1";
	    	String hostname = "www.httpbin.org";
			int port = 80;

			InetAddress addr = InetAddress.getByName(hostname);
			Socket socket = new Socket(addr, port);
	        
	        BufferedWriter out = new BufferedWriter(
	                new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
	        BufferedReader in = new BufferedReader(
	                new InputStreamReader(socket.getInputStream()));
	        
	        String params = URLEncoder.encode("course", "UTF-8") + "=" + URLEncoder.encode("networking", "UTF-8");
			params += "&" + URLEncoder.encode("Assignment", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
	                
	       // sendGETRequest(out,  "get",  getParams );
	        sendPOSTRequest(out,  "post",  params );
	        readResponse(in);
	        
	        out.close();
	        in.close();	
		}
		
   
  private static void sendGETRequest(BufferedWriter out, String  requestType, String params[]) throws IOException {
        System.out.println(" * Request");
        
//        for (String line : getContents(request)) {
//            System.out.println(line);
//            out.write(line + "\r\n");
//        }
        
        String path = "/"+requestType+params[0];

		// Send headers
		out.write("GET " + path + " HTTP/1.0\r\n");
		out.write("Host: httpbin.org\r\n");
		out.write("Content-Type: application/json"+"\r\n");
		out.write("User-Agent: Concordia-HTTP/1.0\r\n");
        out.write("\r\n");
        out.flush();
    }
  
  private static void sendPOSTRequest(BufferedWriter out, String  requestType, String params) throws IOException {
      System.out.println(" * Request");
      
      String path = "/"+requestType;

		// Send headers
      out.write("POST " + path + " HTTP/1.0\r\n");
      out.write("Host: httpbin.org\r\n");
      out.write("Content-Length: " + params.length() + "\r\n");
      out.write("Content-Type: application/json\r\n");
      out.write("User-Agent: Concordia-HTTP/1.0\r\n");
      out.write("\r\n");

		// Send parameters
      out.write(params);
      out.flush();
  }
    
    private static void readResponse(BufferedReader in) throws IOException {
        System.out.println("\n * Response");
        
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
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
}