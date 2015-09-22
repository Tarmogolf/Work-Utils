package dev.stan.workutils.dymax.iconic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class IconicInput {
	
	private ArrayList<String[]> iconicList;
	private final int[] TO_DELETE = {11, 14}; //list of array indices that we want blank in the output
	
	public IconicInput(File inputFile){
		iconicList = genDfillList(inputFile);
	}


	private ArrayList<String[]> genDfillList(File inputFile) {
		Scanner scan = null;
		ArrayList<String> compressedList = new ArrayList<String>();
		try {
			scan = new Scanner(inputFile);
			while(scan.hasNext()){
				compressedList.add(scan.nextLine());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);;
		}
		scan.close();
		ArrayList<String[]> dFills = splitLines(compressedList);

		return dFills;
	}

	private ArrayList<String[]> splitLines(ArrayList<String> compressedList) {
		ArrayList<String[]> splitDfill = new ArrayList<String[]>();
		
		for(String i : compressedList){
			String[] split = i.split(",");
			for(int j : TO_DELETE){
				split[j] = "";
			}
			splitDfill.add(split);
		}
		
		return splitDfill;		
	}
	
	public ArrayList<String[]> getIconicList(){
		return iconicList;
	}
	
	public static void main(String[] args){
		IconicInput test = new IconicInput(new File("C:/Users/Stan/Documents/CBD00006129_DFILLESNFeed_20150910093052.txt"));
		
		for(String[] i : test.getIconicList()){
			System.out.println(Arrays.toString(i));
		}
	}
}
