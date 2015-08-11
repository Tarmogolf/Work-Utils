package dev.stan.workutils.excel;

import java.io.IOException;

import jxl.Sheet;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class JXLW {

	public static void writeCellContents(WritableSheet sheet, int col, int row, String content) throws RowsExceededException, WriteException{
		sheet.addCell(new Label(col, row, content));
	}
	
	public static String getCellContents(Sheet sheet, int col, int row){
		return sheet.getCell(col, row).getContents();
	}
	
	public static void closeBook(WritableWorkbook b) throws IOException, WriteException{
		b.write();
		b.close();
	}
}