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
import dev.stan.workutils.exception.PropertyNotFoundException;
import dev.stan.workutils.helper.PropHelper;

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

		
		String username = null, pw = null;

		try {
			username = PropHelper.getPropertyValue("user");
			pw = PropHelper.getPropertyValue("password");
		} catch (PropertyNotFoundException e) {
			e.printStackTrace();
		}
		InventoryNotify tweeter = new InventoryNotify(username,pw,new File("C:\\Users\\User\\Documents\\Dymax Crawler\\Dymax Crawler.txt"));
		
		Calendar now = new GregorianCalendar();
		//run continuously until 9 o'clock
		while(now.get(Calendar.HOUR_OF_DAY) < 21 && tweeter.getSKUList().size() > 0){
			tweeter.crawl();
		}
		
		tweeter.getParts().quit();
	}
}
