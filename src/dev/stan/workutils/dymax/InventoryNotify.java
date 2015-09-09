package dev.stan.workutils.dymax;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import dev.stan.workutils.exception.LoginCredentialsException;

public class InventoryNotify {
	private PartAvailability parts;
	private Twitter twitter;
	private final String users[] = {"@stanbessey"};
	private ArrayList<String> SKUlist;
	
	public InventoryNotify(String user, String pw, File file) throws LoginCredentialsException, FileNotFoundException{
		parts = new PartAvailability(user, pw);
		parts.signIn();
		twitter = TwitterFactory.getSingleton();
		SKUlist = this.generateSKUList(file);
	}
	
	public void crawl() throws TwitterException{
		ArrayList<String> foundList = new ArrayList<String>();
		
		for(String SKU : SKUlist){
			parts.setPartInfo(SKU);
			int qtyAvail = parts.getQty();
			String desc = parts.getDesc();
			if(qtyAvail > 0){
				sendTweet(desc, qtyAvail);
				foundList.add(SKU);
			}
		}
		
		for(String i : foundList){
			SKUlist.remove(i);
		}
		
		try {
			Thread.sleep(90000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void sendTweet(String description, int qty) throws TwitterException{
		String tweet = "";
		for(String s : users){
			tweet+=s+ " ";
		}
		tweet += " "+qty+" "+description;

		twitter.updateStatus(tweet);
	}
	
	/**
	 * Reads a plaintext file for the SKUs that should be checked
	 * on Dymax's catalog.
	 * 
	 * @param file The file to be scanned for part numbers
	 * @return ArrayList of all of the part numbers in the given file
	 * @throws FileNotFoundException Thrown if the given file cannot be located
	 */
	private ArrayList<String> generateSKUList(File file) throws FileNotFoundException{
		Scanner scan = new Scanner(file);
		ArrayList<String> SKUList = new ArrayList<String>();

		while(scan.hasNext()){
			SKUList.add(scan.next().trim());
		}
		scan.close();

		return SKUList;
	}
	
	public ArrayList<String> getSKUList(){
		return SKUlist;
	}
	
	public PartAvailability getParts() {
		return parts;
	}

	public static void main(String[] args) throws FileNotFoundException, LoginCredentialsException, TwitterException{
		Calendar now = new GregorianCalendar();
		
		InventoryNotify tst = new InventoryNotify(args[0],args[1],new File("C:\\Users\\User\\Documents\\Dymax Crawler\\Dymax Crawler.txt"));
		
		while(now.get(Calendar.HOUR_OF_DAY) < 21 && tst.getSKUList().size() > 0){
			tst.crawl();
		}
		
		tst.getParts().quit();
	}
}
