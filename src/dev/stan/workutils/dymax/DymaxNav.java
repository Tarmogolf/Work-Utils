package dev.stan.workutils.dymax;
import dev.stan.workutils.exception.LoginCredentialsException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;



/**
 * Base class for launching Dymax website
 * @author Stan Bessey
 *
 */
public class DymaxNav {

	protected WebDriver driver;
	protected final String BASE_URL = "https://indirectorders.verizonwireless.com";
	private String id, pw,loginSuff = "/xt_webloginvalidation.aspx";

	
	/**
	 * Initializes Selenium Driver
	 */
	public DymaxNav(String id, String pw){
		driver = new FirefoxDriver();
		this.id = id;
		this.pw = pw;
	}
	
	/**
	 * Launches web browser and attempts to sign in using provided
	 * credentials.
	 * 
	 * @param id User's Dymax ID
	 * @param pw User's Dymax password
	 * @throws LoginCredentialsException 
	 */
	public void signIn() throws LoginCredentialsException{
		driver.get(BASE_URL + loginSuff);
		
		WebElement userField, pwField;
		
		userField = driver.findElement(By.name("ctl00$MainPlaceHolder$TextBox1"));
		pwField = driver.findElement(By.name("ctl00$MainPlaceHolder$TextBox2"));
		
		userField.sendKeys(id);
		pwField.sendKeys(pw);
		
		//click login button
		driver.findElement(By.name("ctl00$MainPlaceHolder$Button1")).click();
		
		if(driver.getCurrentUrl().equals(BASE_URL+loginSuff)){
			throw new LoginCredentialsException("Invalid login. Please check credentials and try again.");
		}
	}
	
	/**
	 * Closes the web browser
	 */
	public void quit(){
		driver.quit();
	}
	
	public static void main(String[] args) throws LoginCredentialsException{
		DymaxNav tst = new DymaxNav(args[0],args[1]);
		
		try{
			tst.signIn();
		}catch (LoginCredentialsException e){
			tst.quit();
		}
	}
}
