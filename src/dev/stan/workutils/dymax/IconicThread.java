package dev.stan.workutils.dymax;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.stan.workutils.exception.LoginCredentialsException;

public class IconicThread implements Runnable {
	private List<String[]> subList;
	private final int PREORDER_NUM_INDEX = 8, CUST_NAME_INDEX = 10;
	
	public IconicThread(List<String[]> list){
		this.subList = list;
	}

	@Override
	public void run() {
		IconicInfo fetcher = new IconicInfo();
		try {
			fetcher.signIn();
		} catch (LoginCredentialsException e) {
			e.printStackTrace();
		}
		for (String[] i : subList){
			int count = 0;
			int maxTries = 3;
			
			while(true){
				try{
					String customerName = fetcher.getCustomerName(i[PREORDER_NUM_INDEX]);
					i[CUST_NAME_INDEX] = customerName;
					System.out.println(Arrays.toString(i));
					break;
				}catch(Exception e){
					if(++count == maxTries){
						break;
					}
				}

			}
		}
		fetcher.quit();
	}
	
	public static void main(String[] args) throws InterruptedException{
		IconicInput test = new IconicInput(new File("C:/Users/User/Documents/CBD00006129_DFILLESNFeed_20150906093054.txt"));
		ArrayList<String[]> myList = test.getIconicList();
		Thread[] threads = new Thread[3];
		for(int i = 0, j = 0; i < 3; i++,j+=25){
			threads[i] = new Thread(new IconicThread(myList.subList(j, j+25)));
			threads[i].start();
		}
		for(int i = 0; i < 3; i++){
			threads[i].join();
		}
	}


}
