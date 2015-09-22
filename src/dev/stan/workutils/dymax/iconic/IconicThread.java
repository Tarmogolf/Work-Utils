package dev.stan.workutils.dymax.iconic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import dev.stan.workutils.exception.LoginCredentialsException;

public class IconicThread implements Runnable {
	private List<String[]> subList;
	private final int PREORDER_NUM_INDEX = 8, CUST_NAME_INDEX = 10;
	private File outputFile;
	static CopyOnWriteArrayList<String> allDFills = new CopyOnWriteArrayList<String>();
	
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
			System.exit(0);
		}
		for (String[] i : subList){
			int count = 0;
			int maxTries = 5;
			
			while(true){
				try{
					String customerName = fetcher.getCustomerName(i[PREORDER_NUM_INDEX]);
					i[CUST_NAME_INDEX] = customerName;
					//System.out.println(customerName);
					
					String delim = "";
					StringBuilder sb = new StringBuilder();
					for(String j : i){
						sb.append(delim).append(j);
						delim = ",";
					}
					String customerData = sb.toString();
					allDFills.add(customerData);
					break;
				}catch(Exception e){
					//e.printStackTrace();
					if(++count == maxTries){
						System.out.println(i[PREORDER_NUM_INDEX] + " lookup failed.");
						break;
					}
				}

			}
		}
		fetcher.quit();
	}
	
	public File getOutputFile(){
		return outputFile;
	}
	
	public static void main(String[] args) throws InterruptedException, IOException{
		final long startTime = System.currentTimeMillis();
		IconicInput test = new IconicInput(new File("C:/Users/User/Documents/DFill Upload.txt"));
		
		ArrayList<String[]> myList = test.getIconicList();
		
		int numThreads = 7;
		
		int numPerThread = myList.size() / numThreads;
		
		Thread[] threads = new Thread[numThreads];
		
		for(int i = 0, j = 0; i < numThreads; i++,j+=numPerThread){
			if(i == numThreads-1){
				threads[i] = new Thread(new IconicThread(myList.subList(j, myList.size())));
			}else{
				threads[i] = new Thread(new IconicThread(myList.subList(j, j+numPerThread)));
			}
			threads[i].start();
		}
		
		for(int i = 0; i < numThreads; i++){
			threads[i].join();
		}
		
		FileWriter writer = new FileWriter("C:/Users/User/Documents/Output.txt");
		
		for(String str : allDFills){
			writer.write(str);
			writer.write("\n");
		}
		
		writer.close();
		
		final long endTime = System.currentTimeMillis();
		
		float timeElapsed = (endTime-startTime);
		
		System.out.println("total execution time: " + timeElapsed);
	
	}


}
