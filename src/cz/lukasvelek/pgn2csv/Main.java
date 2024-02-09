package cz.lukasvelek.pgn2csv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		BufferedReader reader;
		String fileLocation = new String();
		
		System.out.println("=====   WELCOME TO PGN2CSV   =====");;
		
		if(args.length == 1) {
			fileLocation = args[0];
		} else {
			Scanner scanner = new Scanner(System.in);
			System.out.print("Enter file location: ");
			fileLocation = scanner.next();
			scanner.close();
		}
		
		System.out.println();
		
		try {
			BufferedWriter writer2 = new BufferedWriter(new FileWriter("output.csv"));
			writer2.write("index;player1;player2;result;date;player1_elo;player2_elo;player1_elo_change;player2_elo_change" + "\r\n");
			writer2.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		String temp = new String();
		int parts = 0;
		
		Long lastIndex = (long) 1;
		String lastElapsedTime = new String();
		
		try {
			reader = new BufferedReader(new FileReader(fileLocation));
			BufferedWriter writer = new BufferedWriter(new FileWriter("output.csv", true));
			
			String line = reader.readLine();
			
			Long startTime = System.nanoTime();
			
			Long index = (long) 1;
			while(line != null) {
				String data = new String();
				boolean isOk = false;
				
				if(line != "") {
					if(line.startsWith("[White ")) {
						data = line.substring(8);
						data = data.replace('"', ' ');
						data = data.replace(']', ' ');
						data = data.trim();
						isOk = true;
					} 
					if(line.startsWith("[Black ")) {
						data = line.substring(8);
						data = data.replace('"', ' ');
						data = data.replace(']', ' ');
						data = data.trim();
						isOk = true;
					}
					if(line.startsWith("[Result ")) {
						data = line.substring(9);
						data = "" + data.charAt(0);
						isOk = true;
					}
					if(line.startsWith("[UTCDate ")) {
						data = line.substring(9);
						data = data.replace('"', ' ');
						data = data.replace(']', ' ');
						data = data.trim();
						isOk = true;
					}
					if(line.startsWith("[WhiteElo ")) {
						data = line.substring(11);
						data = data.replace('"', ' ');
						data = data.replace(']', ' ');
						data = data.trim();
						isOk = true;
					}
					if(line.startsWith("[BlackElo ")) {
						data = line.substring(11);
						data = data.replace('"', ' ');
						data = data.replace(']', ' ');
						data = data.trim();
						isOk = true;
					}
					if(line.startsWith("[WhiteRatingDiff ")) {
						data = line.substring(18);
						data = data.replace('"', ' ');
						data = data.replace(']', ' ');
						data = data.trim();
						isOk = true;
					}
					if(line.startsWith("[BlackRatingDiff ")) {
						data = line.substring(18);
						data = data.replace('"', ' ');
						data = data.replace(']', ' ');
						data = data.trim();
						isOk = true;
					}
					
					if(parts == 0) {
						if(isOk) {
							writer.append(index.toString() + ";");
							parts++;
						}
					}
					
					if(parts == 9) {
						parts = 0;
						
						writer.append(temp + "\r\n");
						temp = "";
						index++;
					} else {
						if(isOk) {
							if(parts == 8) {
								temp += data;
							} else {
								temp += data + ";";
							}
							parts++;
						}
					}
					
					if(index != lastIndex) {
						Long nowTime = System.nanoTime();
						
						Long timeDiff = (nowTime - startTime) / 1000000000;
						Long minutes = (long) 0;
						Long hours = (long) 0;
						
						String elapsedTime = "";
						
						if(timeDiff >= 60) {
							while(timeDiff >= 60) {
								timeDiff -= 60;								
								minutes++;
							}
						}
						
						if(minutes >= 60) {
							while(minutes >= 60) {
								minutes -= 60;
								hours++;
							}
						}
						
						elapsedTime = hours.toString() + "h " + minutes.toString() + "min " + timeDiff.toString() + "s";
						lastElapsedTime = elapsedTime;
						
						System.out.println("Processed #" + index.toString() + " (Time elapsed: " + elapsedTime + ")");
						lastIndex = index;
					}
					line = reader.readLine();
				}
			}
			
			writer.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Export finished! Time taken: " + lastElapsedTime);
	}
}
