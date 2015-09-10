package dev.stan.workutils.dymax;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class IconicInfo extends DymaxNav {

	public IconicInfo() {
		super();
	}
	
	public String getCustomerName(String preorderNum){
		navigateToPreorder(preorderNum);
		return driver.findElement(By.id("ctl00_MainPlaceHolder_lblShipToContact")).getText();
	}
	
	private void navigateToPreorder(String preorderNumber){
		driver.get("https://indirectorders.verizonwireless.com/xt_documentsearch.aspx");
		
		WebElement dropDown = driver.findElement(By.id("ctl00_MainPlaceHolder_ddlCriteriaMain"));
		Select preorder = new Select(dropDown);
		preorder.selectByVisibleText("Pre-Order Number");
		
		//trying to find the preorder text field without a delay was causing issues
		//since it takes a moment for the field to populate after clicking preorder
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		driver.findElement(By.name("ctl00$MainPlaceHolder$txtFromCriteriaNoDate")).sendKeys(preorderNumber);
		driver.findElement(By.id("ctl00_MainPlaceHolder_btnSearch")).click();

		driver.findElement(By.id("ctl00_MainPlaceHolder_grdOrdersList_ctl02_LinkButton1")).click();
	}
}
