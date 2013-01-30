package org.nusco;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.security.MessageDigest;

// Remember: don't modify this class! (Actually, you don't even need to look at it... Just run it!
public class Challenge {
	private static int score = 0;
	private static String[] steps = { "Your code compiles.", "Here, doggy...",
			"What a cute pussycat!", "Another point for describing your pets.",
			"Yet another point for creating a pet cemetery.",
			"Poor little creatures...", "Sometimes they come back!",
			"I'm pretty sure they used to look nicer...",
			"Did anybody see my cat?", "Oh my God, this is horrible!" };
	
	public static void score() {
		score++;
		
		if (score == 1)
			System.out.println("Welcome, team " + readTeamName() + "!\n");
		
		System.out.println(steps[score - 1]);

		if (score >= steps.length)
			System.out.println("10 points! YOU WON!");
		else if (score != 1)
			System.out.println("Your score: " + score + " points!");
		else
			System.out.println("Your score: 1 point!");

		System.out.println();
		
		sendInfoToServer(readTeamName(), score);
	}

	private static void sendInfoToServer(String teamName, int score) {
		try {
			URL url = new URL("http://microlog.herokuapp.com/" + readTeamName() + "/" + score);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoOutput(true);
	        connection.setRequestMethod("POST");
	        connection.disconnect();
	        connection.getOutputStream().close();
	        connection.getInputStream().close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static void reportError(Throwable t) {
		if (t instanceof Exception) {
			System.out.println(t.getMessage());
			System.out.println("The failed assertion is at: " + t.getStackTrace()[2]);	
		} else {
			System.out.println("Your code doesn't compile yet. Make it run to score your first point!");
		}
		System.exit(0);
	}

	private static String readTeamName() {
		try {
			return new BufferedReader(new FileReader("team.txt")).readLine();
		} catch (IOException e) {
			System.out.println("I'm having trouble reading your team's name.");
			System.out.println("Are you sure you have a team.txt file?");
			System.exit(0);
			return null;
		}
	}

	public static void assertEquals(Object expected, Object actual) {
		if (!expected.equals(actual))
			failComparison(expected, actual);
	}

	public static void assertSame(Object expected, Object actual) {
		if (expected != actual)
			failComparison(expected, actual);
	}

	private static void failComparison(Object expected, Object actual) {
		throw new RuntimeException("To get more points, fix this error: I expected [" + expected
				+ "], but I got [" + actual + "]");
	}

	private static boolean checkSourceIntegrity() {
		try {
			InputStream fis = new FileInputStream("src/main/java/org/nusco/HorrorStory.java");
			byte[] buffer = new byte[1024];
			MessageDigest complete = MessageDigest.getInstance("MD5");
			int numRead;
			do {
				numRead = fis.read(buffer);
				if (numRead > 0) {
					complete.update(buffer, 0, numRead);
				}
			} while (numRead != -1);
			fis.close();
			
			String mainFileChecksum = "";
			byte[] b = complete.digest();
			for (int i = 0; i < b.length; i++) {
				mainFileChecksum += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
			}

			return mainFileChecksum.equals("f79e625c63e29c71197ecef8f3df4fd4");
		} catch (Exception e) {
			return false;
		}
	}
	
	public static void main(String[] args) {
		if(!checkSourceIntegrity()) {
			System.out.println("You modified the HorrorStory.java file! Please revert your changes.");
			System.exit(0);
		}
		try {
			new HorrorStory().go();
		} catch (Throwable t) {
			reportError(t);
		}
	}
}
