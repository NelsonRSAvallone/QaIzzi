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

public class ProcesoBloqueoDeIMEI {
	
	private MainClass main = new MainClass();
	private WebDriver driver;

	
	@Before
	public void setUp() throws InterruptedException, IOException {
		
		driver = main.setDriver();
		main.initBrowser();
		main.goToAccountLink();

		driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
		
		Thread.sleep(20000);
		
	}
	
	
	@Test
	public void Bloqueo() throws InterruptedException {
		
		try {	
			main.waitForInvisibleSpinner();
			
			WebElement frame = driver.findElement(By.id("iFrameResizer1"));
			JavascriptExecutor executor = (JavascriptExecutor)driver;
			executor.executeScript("arguments[0].style.display = 'block'; arguments[0].style.zIndex = '999999';", frame);
			driver.switchTo().frame(frame);
			Thread.sleep(2000);
			WebElement boton = driver.findElement(By.xpath("/html/body/div[1]/div[1]/ng-include/div/div/section/div[4]/button"));
			executor.executeScript("arguments[0].click();", boton);
			
			driver.switchTo().defaultContent();
			
			IMEI(1);
			
			main.returnExecutionSuccess(getClass().getName());
		} catch (Exception e) {
			main.returnExecutionError(getClass().getName(), e);
		}
}
	
	public void IMEI(int index) throws InterruptedException {
		
		WebElement frame = new WebDriverWait(driver, 40)
				.until(ExpectedConditions.elementToBeClickable(By.id("iFrameResizer3")));
		frame.click();
		driver.switchTo().frame(frame);
		new WebDriverWait(driver, 40)
		
	.until(ExpectedConditions.elementToBeClickable(By.id("CheckIMEIRadio")));
		Thread.sleep(3000);
		
		List<WebElement> opt = driver.findElements(By.xpath("//*[@id=\'CheckIMEIRadio\']"));
		Thread.sleep(2000);
		if (index == 0) { //¿El cliente posee IMEI? --> SI
		opt.get(index).findElement(By.xpath("./..")).click();
		Thread.sleep(2000);
		
		driver.findElement(By.xpath("//*[@id=\'NumberImei\']")).sendKeys("357208104228356");
		Thread.sleep(1000);
		
		driver.findElement(By.xpath("//*[@id='RAValidationImei']")).click();
		Thread.sleep(3000);
		
		}
		
		else { //¿El cliente posee IMEI? --> NO
			opt.get(index).findElement(By.xpath("./..")).click(); //Selecciona la opción NO
			new WebDriverWait (driver, 20)
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='slds-radio--faux']")));
			
			List<WebElement> lineas = driver.findElements(By.xpath("//span[@class='slds-radio--faux']")); //Hacemos List con todas las lineas que aparecen
			lineas.get(0).click(); //Seleccionamos linea
			Thread.sleep(2000);
			}
			
	
		main.waitForInvisibleSpinner();
		WebElement siguiente = new WebDriverWait(driver,40).until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='StepLockImei_nextBtn']")));
		//driver.findElement(By.xpath("//div[@id='StepLockImei_nextBtn']"));
		siguiente.click();
		Thread.sleep(1000);
	
		Confirmacion();
	}
	
	public void Confirmacion() throws InterruptedException {
		
		main.waitForInvisibleSpinner();
		
		new WebDriverWait(driver,40).until(ExpectedConditions.elementToBeClickable(By.id("radioConfirmation")));
		Thread.sleep(3000);
	
		List<WebElement> opt = driver.findElements(By.xpath("//*[@id=\'radioConfirmation\']"));
		Thread.sleep(2000);
		opt.get(0).findElement(By.xpath("./..")).click();
		Thread.sleep(2000);
		
		main.waitForInvisibleSpinner();
		WebElement siguiente = new WebDriverWait(driver,40).until(ExpectedConditions.elementToBeClickable(By.id("Confirmation_nextBtn")));
		siguiente.click();
		Thread.sleep(2000);
		
		main.waitForInvisibleSpinner();
		WebElement finalizar = new WebDriverWait(driver,40).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"doneAction-241\"]/div/div/div[3]/div/button")));
		finalizar.click();

		} 
		
		
	}

	
		




