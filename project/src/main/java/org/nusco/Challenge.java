package org.nusco;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;

// Remember: don't modify this class! (Actually, you don't even need to look at it... Just run it!
public class Challenge {
	private static int score = 0;
	private static String[] steps = { "Il tuo codice compila.", "Un simpatico sacco di pulci...",
			"Che simpatico micetto!", "Un altro punto per aver descritto i tuoi animali.",
			"Ancora un punto per aver costruito un cimitero per animali.",
			"Povere bestie...", "A volte ritornano!",
			"Me li ricordavo piu' carini...",
			"Qualcuno ha visto il mio gatto?", "Oh mio dio, e' orribile!" };
	
	public static void score() {
		score++;
		
		if (score == 1)
			System.out.println("Welcome, team " + readTeamName() + "!\n");
		
		System.out.println(steps[score - 1]);

		if (score >= steps.length)
			System.out.println("10 punti! HAI VINTO!");
		else if (score != 1)
			System.out.println("Il tuo punteggio: " + score + " punti!");
		else
			System.out.println("Il tuo punteggio: 1 punto!");

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
			System.out.println("L'asserzione che fallisce e' alla riga: " + t.getStackTrace()[2]);	
		} else {
			System.out.println("Il tuo codice non compila. Fallo girare per ricevere il tuo primo punto!");
		}
		System.exit(0);
	}

	private static String readTeamName() {
		try {
			return new BufferedReader(new FileReader("team.txt")).readLine();
		} catch (IOException e) {
			System.out.println("Non riesco a leggere il nome del tuo team.");
			System.out.println("Sei certo di avere un file team.txt?");
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
		throw new RuntimeException("Per avere altri punti, correggi questo errore: My aspettavo [" + expected
				+ "], ma ho avuto [" + actual + "]");
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
			System.out.println("Hai modificato il file HorrorStory.java! Per favore, torna al file originale.");
			System.exit(0);
		}
		try {
			new HorrorStory().go();
		} catch (Throwable t) {
			reportError(t);
		}
	}
}
