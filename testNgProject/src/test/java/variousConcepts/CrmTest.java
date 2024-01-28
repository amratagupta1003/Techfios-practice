package variousConcepts;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CrmTest {
	static WebDriver driver;
//	String url;
//	String userId;
//	String pwd;
//	String browser;

	String dashboardValidationTest = "Dashboard";
	String userAlertValidationText = "Please enter your user name";
	String passwordAlertValidationText = "Please enter your password";
	String fullName = "selenium";
	String company = "Techfios";

	String browser = "chrome";
	String url = "https://codefios.com/ebilling/login";
	String userId = "demo@codefios.com";
	String password = "abc123";
	String addCUstomerValidation = "New Customer";
	String email = "demo@codefios.com";
	String phone = "1234567";
	

	By USER_NAME_FIELD = By.xpath("//input[@id='user_name']");
	By PASSWORD_FIELD = By.xpath("//input[@id='password']");
	By SINGIN_BUTTON_FIELD = By.xpath("//*[@id=\"login_submit\"]");

	By SUBMIT_BUTTON_FIELD = By.xpath("//button[@id='login_submit']");
	By DASHBOARD_VALIDATION_FIELD = By.xpath("/html/body/div[1]/section/div/div[2]/div/div/header/div/strong");
	By CUSTOMER_MENU_FIELD = By.xpath("/html/body/div[1]/aside[1]/div/nav/ul[2]/li[2]/a");
	By ADD_CUSTOMER_MENU_FIELD = By.xpath("//*[@id=\"customers\"]/li[2]/a/span");
	By ADD_CUSTOMER_VALIDATION_FIELD = By.xpath("/html/body/div[1]/section/div/div[2]/div/div[1]/div[1]/div/div/header/div/strong");
	By FULL_NAME_FIELD = By.xpath("//*[@id=\"general_compnay\"]/div[1]/div/input");
	By COMPANY_DROPDOWN_FILED = By.xpath("//*[@id=\"general_compnay\"]/div[2]/div/select");
	By EMAIL_FIELD = By.xpath("//*[@id=\"general_compnay\"]/div[3]/div/input");
	By COMPANY_DROPDOWN = By.xpath("//*[@id=\"general_compnay\"]/div[2]/div/select");
	By USER_FULL_NAME = By.xpath("//*[@id=\"general_compnay\"]/div[1]/div/input");
	By PHONE_FIELD = By.xpath("//*[@id=\"phone\"]");
	By COUNTRY_DROPDOWN = By.xpath("//*[@id=\"general_compnay\"]/div[8]/div[1]/select");

	//@BeforeClass
	public void readConfig() {
		// InputStream //BufferedReader //FileReader //Scanner
		try {
			InputStream input = new FileInputStream("src/main/java/config/config.properties");
			Properties prop = new Properties();
			prop.load(input);
			browser = prop.getProperty("browser");
			url = prop.getProperty("url");
			userId = prop.getProperty("userId");
			password = prop.getProperty("password");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@BeforeMethod
	public void init() {

		if (browser == "chrome") {
			System.setProperty("webdriver.chrome.driver", "driver/chromedriver");
			driver = new ChromeDriver();
		} else if (browser == "edge") {
			System.setProperty("webdriver.edge.driver", "driver/msedgedriver");
			driver = new EdgeDriver();
		} else {
			System.out.println("no browser found");
		}

		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.get(url);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@Test(priority = 1)
	public void testLogin() {
		driver.findElement(USER_NAME_FIELD).sendKeys(userId);
		driver.findElement(PASSWORD_FIELD).sendKeys(password);
		driver.findElement(SUBMIT_BUTTON_FIELD).click();
		Assert.assertEquals(driver.findElement(DASHBOARD_VALIDATION_FIELD).getText(), dashboardValidationTest,
				"Dashboard page not forund!!");

	}

	@AfterMethod
	public void tearDown() {
		driver.close();
	}
	
	@Test(priority = 2)
	public void testAlert() {
		driver.findElement(SINGIN_BUTTON_FIELD).click();
		Assert.assertEquals(driver.switchTo().alert().getText(), userAlertValidationText, "Alert is not available!");
		driver.switchTo().alert().accept();
		
		driver.findElement(USER_NAME_FIELD).sendKeys(userId);
		driver.findElement(SINGIN_BUTTON_FIELD).click();
		Assert.assertEquals(driver.switchTo().alert().getText(), passwordAlertValidationText, "Alert is not available!");
		driver.switchTo().alert().accept();
		
	}

	@Test(priority = 3)
	public void testAddCustomer() {
		testLogin();
		driver.findElement(CUSTOMER_MENU_FIELD).click();
		driver.findElement(ADD_CUSTOMER_MENU_FIELD).click();

		Assert.assertEquals(driver.findElement(ADD_CUSTOMER_VALIDATION_FIELD).getText(), addCUstomerValidation, "New customer page not forund!!");
		
		driver.findElement(FULL_NAME_FIELD).sendKeys(fullName + generateRandomNumber(999));
		
		
		selectDropdown(COMPANY_DROPDOWN_FILED, 1);
		
		driver.findElement(EMAIL_FIELD).sendKeys(generateRandomNumber(99) + email);
		driver.findElement(PHONE_FIELD).sendKeys(phone + generateRandomNumber(9999));

		selectDropdown(COUNTRY_DROPDOWN, 2);
	}
	
	private int generateRandomNumber(int boundNum) {
		Random rnd = new Random();
		int randomNum = rnd.nextInt(boundNum);
		return randomNum;
	}
	
	private void selectDropdown(By element, int val) {
		Select sel = new Select(driver.findElement(element));
		sel.selectByIndex(val);
	}
}
