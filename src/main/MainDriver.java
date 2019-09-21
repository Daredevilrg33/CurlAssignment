package main;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Scanner;

import javax.print.DocFlavor.INPUT_STREAM;

public class MainDriver {

	public static void main(String[] args) {

//		httpc (get|post) [-v] (-h "k:v")* [-d inline-data] [-f file] URL
		System.out.println("Welcome to the Curl Terminal");
		System.out.println("Please enter the Curl Command");
		
//		try {
//			//httpc httpObj = new httpc();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		Scanner scanner = new Scanner(System.in);
		String inputData = scanner.nextLine();
		String[] input = inputData.split(" ");
		String http = input[0];
		String requestType = input[1];
		String url = input[input.length-1];
		ArrayList<String> headerList = new ArrayList<String>();
		ArrayList<String> dataList = new ArrayList<String>();
		ArrayList<String> fileList = new ArrayList<String>();
		boolean verbose= false;
		int ind =0;
		
		if(inputData.indexOf("httpc")!=-1) {
			http = "httpc";
		}
		else
			System.out.println("Inappropriate command");
		if(inputData.indexOf("get")!=-1 || inputData.indexOf("GET")!=-1)
		{
			requestType = "get";
		    ind = inputData.indexOf("get");
		    ind=ind+4;
		    inputData.substring(ind, inputData.length());
		}
		else if(inputData.indexOf("post")!=-1 || inputData.indexOf("POST")!=-1)
		{
			requestType = "post";
			ind = inputData.indexOf("post");
			ind=ind+5;
			inputData=inputData.substring(ind, inputData.length());
		}
		
		for (int i = 0; i < inputData.length(); i++) {
			
			if(inputData.substring(i, i+1).equals("-"))
			{
				if(inputData.substring(i+1, i+2).equalsIgnoreCase("h"))	
				{
					if( inputData.indexOf("-h") !=-1 ||  inputData.indexOf("-H")!=-1)
					{
						int index1 = inputData.indexOf("-h")==-1? inputData.indexOf("-H"):inputData.indexOf("-h");
					int index2 = inputData.indexOf(" ", index1+3);
					String header = inputData.substring(index1+3,index2);
					headerList.add(header);
					inputData=inputData.substring(index1+3+header.length(), inputData.length());
					}
				}
			}
			if(inputData.substring(i, i+1).equals("-"))
			{
				if(inputData.substring(i+1, i+2).equalsIgnoreCase("d"))	
				{
					if( inputData.indexOf("-d") !=-1 ||  inputData.indexOf("-D")!=-1)
					{
					int index1 = inputData.indexOf("-d")==-1? inputData.indexOf("-D"):inputData.indexOf("-d");
					int index2 = inputData.indexOf("}", index1+3);
					String data = inputData.substring(index1+3,index2+2);
					dataList.add(data);
					inputData=inputData.substring(index1+3+data.length(), inputData.length());
					}
				}
				else if(inputData.substring(i+1, i+2).equalsIgnoreCase("f"))
				{
					if( inputData.indexOf("-f") !=-1 ||  inputData.indexOf("-F")!=-1)
					{
					int index1 = inputData.indexOf("-f")==-1? inputData.indexOf("-F"):inputData.indexOf("-f");
					int index2 = inputData.indexOf("'", index1+4);
					String fileData = inputData.substring(index1+4,index2);
					fileList.add(fileData);
					inputData=inputData.substring(index1+4+fileData.length(), inputData.length());
					}
				}
			}
			
			
		}
		
		
		/*for (int i = 2; i < input.length; i++) {
			
			if(requestType.equalsIgnoreCase("post"))
			{
				if(input[i].equalsIgnoreCase("-d"))
				{
					dataList.add(input[i+1]);	
				}	
				else if(input[i].equalsIgnoreCase("-f"))
				{
					fileList.add(input[i+1]);	
				}
					
			}
			else
			{
				System.out.println("not an appropriate command");
			}
			if(input[i].equalsIgnoreCase("-v"))
			{
				verbose=true;	
			}
			if(input[i].equalsIgnoreCase("-h"))
			{
				headerList.add(input[i+1]);
			}
			
			
		}*/
		
		System.out.println("http: "+ http) ;
		System.out.println("requestType: "+ requestType) ;
		System.out.println("url: "+ url) ;
		System.out.println("headerList: "+ headerList.toString()) ;
		System.out.println("dataList: "+ dataList.toString()) ;
		System.out.println("fileList: "+ fileList.toString()) ;

	}
}
