package dev.stan.workutils.helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Singleton {
    
	private static final Singleton inst= new Singleton();

    private Singleton() {
        super();
    }

    public synchronized void writeToFile(String str, File file) {
    	BufferedWriter writer = null;
    	try {
    		writer = new BufferedWriter(new FileWriter(file));
    		writer.write(str);
       } catch (IOException e) {
    	   e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
			}
		}
    }

    public static Singleton getInstance() {
        return inst;
    }

}