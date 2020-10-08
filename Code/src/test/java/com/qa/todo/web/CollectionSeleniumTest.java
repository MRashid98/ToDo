package com.qa.todo.web;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class CollectionSeleniumTest {

	public static WebDriver driver;

	public static ChromeOptions chromeCfg() {
		Map<String, Object> prefs = new HashMap<String, Object>();
		ChromeOptions cOptions = new ChromeOptions();

		// Settings
		prefs.put("profile.default_content_setting_values.cookies", 2);
		prefs.put("network.cookie.cookieBehavior", 2);
		prefs.put("profile.block_third_party_cookies", true);

		// Create ChromeOptions to disable Cookies pop-up
		cOptions.setExperimentalOption("prefs", prefs);
		cOptions.setBinary("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
		return cOptions;
	}

	@BeforeEach
	public void init() {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\azwad\\Documents\\workspace-spring-tool-suite-4-4.8.0.RELEASE\\ToDo\\Code\\src\\test\\resources\\drivers\\chromedriver.exe");
		driver = new ChromeDriver(chromeCfg());
		driver.get("http://127.0.0.1:5500/html/index.html");
	}

	@Test
	public void testCreateCollection() {

		WebElement newColBtn = driver.findElement(By.xpath("/html/body/div[1]/a"));
		newColBtn.click();

		WebElement colNameTextField = driver.findElement(By.id("collectionName"));
		WebElement createColBtn = driver.findElement(By.xpath("/html/body/div[1]/form/button"));
		colNameTextField.sendKeys("Work");
		createColBtn.click();

		WebElement homeBtn = driver.findElement(By.xpath("//*[@id=\"navbarSupportedContent\"]/ul/li/a"));
		homeBtn.click();

		WebElement workColRow = driver.findElement(By.xpath("/html/body/div[1]/table/tbody/tr"));
		assertEquals(true, workColRow.isDisplayed());

		workColRow = driver.findElement(By.xpath("/html/body/div[1]/table/tbody/tr/th"));
		assertEquals("Work", workColRow.getText());

	}

	@Test
	public void testUpdateCollection() {

		WebElement updateColBtn = driver.findElement(By.xpath("/html/body/div[1]/table/tbody/tr/td[2]/a[1]"));
		updateColBtn.click();

		WebElement colNameTextField = driver.findElement(By.xpath("//*[@id=\"collName\"]"));
		WebElement updateBtn = driver.findElement(By.id("updateBtn"));
		colNameTextField.clear();
		colNameTextField.sendKeys("Work QA");
		updateBtn.click();

		WebElement homeBtn = driver.findElement(By.xpath("//*[@id=\"navbarSupportedContent\"]/ul/li/a"));
		homeBtn.click();

		WebElement workColRow = driver.findElement(By.xpath("/html/body/div[1]/table/tbody/tr/th"));
		assertEquals("Work QA", workColRow.getText());
	}


	@AfterEach
	public void cleanUp() {
		driver.quit();
	}
}
