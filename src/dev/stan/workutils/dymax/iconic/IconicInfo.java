package dev.stan.workutils.dymax.iconic;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import dev.stan.workutils.dymax.DymaxNav;

public class IconicInfo extends DymaxNav {

	public IconicInfo() {
		super();
	}
	
	public String getCustomerName(String preorderNum) throws InterruptedException{
		navigateToPreorder(preorderNum);
		String custName = driver.findElement(By.xpath("//*[@id=\"MainPlaceHolder_lblShipToContact\"]")).getText();
		
		if (custName.isEmpty()){
			return "Auto Submit";
		}else{
			return custName;
		}
	}
	
	private void navigateToPreorder(String preorderNumber) throws InterruptedException{
		driver.get("https://indirectorders.verizonwireless.com/xt_documentsearch.aspx");
		
		WebElement dropDown = driver.findElement(By.xpath("//*[@id=\"MainPlaceHolder_ddlCriteriaMain\"]"));
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
		driver.findElement(By.xpath("//*[@id=\"MainPlaceHolder_btnSearch\"]")).click();

		driver.findElement(By.xpath("//*[@id=\"MainPlaceHolder_grdOrdersList_LinkButton1_0\"]")).click();
	}
}
