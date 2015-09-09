package dev.stan.workutils.dymax;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import dev.stan.workutils.exception.LoginCredentialsException;

public class IconicDriver {

	public static void main(String[] args) throws FileNotFoundException, LoginCredentialsException{
		File input = new File(args[2]);
		ArrayList<String> rawDfill = new ArrayList<String>();
		Scanner scan = new Scanner(input);
		
		while(scan.hasNext()){
			rawDfill.add(scan.nextLine());
		}
		
		scan.close();
		
		ArrayList<String[]> splitDfill = new ArrayList<String[]>();
		
		for(String i : rawDfill){
			String[] split = i.split(",");
			split[11] = "";
			split[14] = "";
			splitDfill.add(split);
		}
		
		IconicInfo iconic = new IconicInfo(args[0], args[1]);
		iconic.signIn();
		iconic.quit();
	}


}
