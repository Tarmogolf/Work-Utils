package dev.stan.workutils.dymax.avail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import dev.stan.workutils.excel.JXLW;
import dev.stan.workutils.exception.LoginCredentialsException;

public class DymaxAvailSheet {

	private PartAvailability avail;
	private WritableWorkbook book;
	private ArrayList<String> partNums;
	private WritableSheet sheet;
	private final int DESC_COL = 0, PART_NUM_COL = 1, QTY_COL = 2, COST_COL = 3;
	
	public DymaxAvailSheet(String inputPath, String outputPath) throws IOException, LoginCredentialsException{
		avail = new PartAvailability();
		avail.signIn();
		book = Workbook.createWorkbook(new File(outputPath + generateFileName()));
		partNums = generateSKUList(new File(inputPath));
		sheet = book.createSheet("Availability", 0);
	}
	
	public void writeAvailability() throws RowsExceededException, WriteException, IOException{
		int row = 0;
		
		for(String partNum: partNums){
			avail.setPartInfo(partNum);
			writeRow(row, avail);			
			row++;
		}
		avail.quit();
		JXLW.closeBook(book);
	}
	
	private void writeRow(int row, PartAvailability avail) throws RowsExceededException, WriteException{
		JXLW.writeCellContents(sheet, DESC_COL, row, avail.getDesc());
		JXLW.writeCellContents(sheet, PART_NUM_COL, row, avail.getPartNum());
		JXLW.writeCellContents(sheet, QTY_COL, row, Integer.toString(avail.getQty()));
		JXLW.writeCellContents(sheet, COST_COL, row, avail.getCost());
	}
	
	private String generateFileName(){
		Calendar now = new GregorianCalendar();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		String date = dateFormat.format(now.getTime());
		return "Verizon Availability " + date+".xls";
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
	
	public static void main(String[] args) throws IOException, RowsExceededException, WriteException, LoginCredentialsException{
		DymaxAvailSheet mySheet = new DymaxAvailSheet(
				"C:\\Users\\User\\Documents\\Dymax Crawler\\Availability Sheet.txt",
				"C:\\Users\\User\\Documents\\");
		
		mySheet.writeAvailability();
	}

}
