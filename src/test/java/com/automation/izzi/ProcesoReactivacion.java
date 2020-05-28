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
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProcesoReactivacion {
	
	private MainClass main = new MainClass();
	private WebDriver driver;
	private WebDriverWait wait;
	
	@Before
	public void setUp() throws InterruptedException, IOException {
		driver = main.setDriver();
		main.initBrowser();
		main.goToAccountLink();
		wait = main.wait;

		driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
		
		Thread.sleep(20000);
	}
	
	@Test
	public void Reactivar () throws InterruptedException {
		try {
			Thread.sleep(2000);
			WebElement frame = driver.findElement(By.id("iFrameResizer2"));
			JavascriptExecutor executor = (JavascriptExecutor)driver;
			executor.executeScript("arguments[0].style.display = 'block'; arguments[0].style.zIndex = '999999';", frame);
			driver.switchTo().frame(frame);
			Thread.sleep(2000);
			List<WebElement> links = driver.findElements(By.linkText("Reactivar"));
			executor.executeScript("arguments[0].click();", links.get(0));
			driver.switchTo().defaultContent();
			Thread.sleep(2000);
			
			Confirmar(0);
			main.returnExecutionSuccess(getClass().getName());
		} catch (Exception e) {
			main.returnExecutionError(getClass().getName(), e);
		}
		
	}
	
	public void Confirmar(int index) throws InterruptedException {
		main.waitForInvisibleSpinner();
		
		WebElement frame = new WebDriverWait(driver, 40)
			.until(ExpectedConditions.elementToBeClickable(By.id("iFrameResizer3")));
		frame.click();
		driver.switchTo().frame(frame);
		
		new WebDriverWait(driver, 40)
			.until(ExpectedConditions.elementToBeClickable(By.id("RadioButtonConfirmation")));
		List<WebElement> opt = driver.findElements(By.id("RadioButtonConfirmation"));
		Thread.sleep(1000);

		if (index == 1) {
			opt.get(index).findElement(By.xpath("./..")).click();
			Thread.sleep(1000);
		}
		
		driver.findElement(By.id("StepReactivationConfirmation_nextBtn")).click();
		Thread.sleep(2000);
		
	}
}
