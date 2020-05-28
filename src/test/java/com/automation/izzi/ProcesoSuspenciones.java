package com.automation.izzi;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProcesoSuspenciones {
	private MainClass main = new MainClass();
	private WebDriver driver;
	private WebDriverWait wait;
	
	@Before
	public void setUp() throws InterruptedException, IOException {
		
		driver = main.setDriver();
		main.initBrowser();
		main.goToAccountLink();

		driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
		
		Thread.sleep(20000);
		
	}
	//test
	
	@Test
	public void testScript() throws InterruptedException {
		try {
			clickSuspension();
			confirmarSuspension();
			motivoDeSuspension();
			main.returnExecutionSuccess(getClass().getName());
		} catch (Exception e) {
			main.returnExecutionError(getClass().getName(), e);
		}
	}
	
	public void clickSuspension() throws InterruptedException {
		Thread.sleep(2000);
		WebElement frame = driver.findElement(By.id("iFrameResizer2"));
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].style.display = 'block'; arguments[0].style.zIndex = '999999';", frame);
		driver.switchTo().frame(frame);
		Thread.sleep(2000);
		List<WebElement> links = driver.findElements(By.linkText("Suspensiones"));
		executor.executeScript("arguments[0].click();", links.get(0));
		//links.get(0).click();
		Thread.sleep(2000);
	}
	
	public void confirmarSuspension() throws InterruptedException {
		driver.switchTo().defaultContent();
		WebElement frame = new WebDriverWait(driver, 40)
				.until(ExpectedConditions.elementToBeClickable(By.id("iFrameResizer3")));
		frame.click();
		driver.switchTo().frame(frame);
		new WebDriverWait(driver, 40)
        	.until(ExpectedConditions.elementToBeClickable(By.id("confirmSuspension")));
		List<WebElement> opt = driver.findElements(By.xpath("//*[@id=\'confirmSuspension\']"));
		Thread.sleep(2000);
		opt.get(0).findElement(By.xpath("./..")).click();
		Thread.sleep(2000);
	}
	
	public void motivoDeSuspension() throws InterruptedException {
		Select picklist = new Select(driver.findElement(By.id("suspendMotive")));
		picklist.selectByIndex(1);
		Thread.sleep(1000);


		new WebDriverWait (driver,40).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\'confirmSuspensionStep_nextBtn\']/p")));
		driver.findElement(By.xpath("//*[@id=\'confirmSuspensionStep_nextBtn\']/p")).click();
		Thread.sleep(3000);
		
		new WebDriverWait (driver,40).until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='slds-button slds-button_brand ng-binding' and contains(text(),Finalizar)]")));
		driver.findElement(By.xpath("//button[@class='slds-button slds-button_brand ng-binding' and contains(text(),Finalizar)]")).click();
		Thread.sleep(1000);
	}
}
