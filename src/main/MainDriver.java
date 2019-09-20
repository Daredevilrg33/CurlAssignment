package main;

import java.util.Scanner;

public class MainDriver {

	public static void main(String[] args) {

//		httpc (get|post) [-v] (-h "k:v")* [-d inline-data] [-f file] URL
		System.out.println("Welcome to the Curl Terminal");
		System.out.println("Please enter the Curl Command");

		Scanner scanner = new Scanner(System.in);
		String inputData = scanner.nextLine();
		String[] input = inputData.split(" ");
		String http = input[0];
		String requestType = input[1];
		for (int i = 2; i < inputData.length(); i++) {
			
		}

	}
}
