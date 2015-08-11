package dev.stan.workutils.dymax;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import dev.stan.workutils.exception.LoginCredentialsException;

public class PartAvailability extends DymaxNav {

	private String desc, partNum, cost;
	private int qty;
	private final String PART_URL_BASE = "/xt_product.aspx?sku=";
	
	
	public PartAvailability(String id, String pw) {
		super(id, pw);
	}

	/**
	 * Navigates to the specified part and gathers information regarding that SKU.
	 * @param partNum The desired Verizon Dymax part number to gather info for.
	 */
	public void setPartInfo(String partNum){
		this.partNum = partNum;
		String urlToCheck = BASE_URL + PART_URL_BASE + partNum;
		driver.get(urlToCheck);
		desc = checkDesc();
		cost = checkCost();
		qty = checkQty();
	}

	private String checkDesc(){
		WebElement descriptField = driver.findElement(By.id("ctl00_MainPlaceHolder_lblItemDescription"));
		return descriptField.getText();
	}
	
	private String checkCost(){
		WebElement priceField = driver.findElement(By.id("ctl00_MainPlaceHolder_lblUnitPrice"));
		return priceField.getText();
	}
	
	private int checkQty(){
		WebElement qtyField = driver.findElement(By.id("ctl00_MainPlaceHolder_lblAvailQty"));
		return Integer.parseInt(qtyField.getText());
	}
	
	/**
	 * 	 @return Description as pulled from Dymax website. 
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @return Dymax part number
	 */
	public String getPartNum() {
		return partNum;
	}

	/**
	 * @return Current cost as loaded in Dymax
	 */
	public String getCost() {
		return cost;
	}

	/**
	 * @return Current number available of specific part number
	 */
	public int getQty() {
		return qty;
	}
	
	public static void main(String[] args) throws LoginCredentialsException{
		PartAvailability tst = new PartAvailability(args[0], args[1]);
		tst.signIn();
		tst.setPartInfo("KYOE4520-D");
		
		System.out.println(tst.getCost() + "\n" + tst.getDesc()+"\n"+ tst.getPartNum()+"\n"+tst.getQty());
	}
}
