package com.automation.izzi;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
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

public class ProcesoCambioDeServicio {
	
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
	public void testScript() throws InterruptedException {
		try {
			cambioDeServicio(driver);
			seleccionDePlan(driver);
			main.returnExecutionSuccess(getClass().getName());
		} catch (Exception e) {
			main.returnExecutionError(getClass().getName(), e);
		}
	}

	public void cambioDeServicio(WebDriver driver) throws InterruptedException {
		Thread.sleep(2000);
		WebElement frame = driver.findElement(By.id("iFrameResizer2"));
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].style.display = 'block'; arguments[0].style.zIndex = '999999';", frame);
		driver.switchTo().frame(frame);
		Thread.sleep(2000);
		List<WebElement> links = driver.findElements(By.linkText("Cambio de Servicio"));
		executor.executeScript("arguments[0].click();", links.get(0));
		driver.switchTo().defaultContent();
	}

	public void seleccionDePlan(WebDriver driver) throws InterruptedException {

		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("slds-spinner_container")));
		WebElement frame = wait.until(ExpectedConditions.elementToBeClickable(By.id("iFrameResizer3")));
		driver.switchTo().frame(frame);
		// frames.get(size-1).click();
		Thread.sleep(2000);
		
		List<WebElement> optPlanes = getPlanes(driver);
		
		Thread.sleep(1000);
		optPlanes.get(0).click();
		
		/*if(optDisplayed(driver)) {
			WebElement plan = null;
			try {
				plan = driver.findElement((By.xpath("//div[@id='block_01tc0000007pvuiAAA']")));
				plan.click();
			} catch (Exception e) {
				if(plan == null) {
					plan = driver.findElement((By.xpath("//div[@id='block_01tc0000007pvuhAAA']")));
				}
				plan.click();
			}
		}*/
		
		
		
		// siguiente
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("slds-spinner_container")));
		Thread.sleep(1000);
		WebElement siguiente = wait.until(ExpectedConditions.elementToBeClickable(By.id("Planes_nextBtn")));
		siguiente.click();
		Thread.sleep(1000);
		// Confirmacion Siguiente
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("slds-spinner_container")));
		Thread.sleep(1000);
		WebElement siguiente2 = wait.until(ExpectedConditions.elementToBeClickable(By.id("Confirmacion_nextBtn")));
		siguiente2.click();
		//Finalizar
		main.waitForInvisibleSpinner();

		new WebDriverWait (driver, 20)
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='slds-button slds-button_brand ng-binding']")));

		String orden = driver.findElement(By.xpath("//p[@class='done-action-subtitle ng-binding ng-scope']")).getText();
		driver.switchTo().defaultContent();
		String letra = "";
		for (int i = 5; i < orden.length(); i++) {
		letra = letra + orden.charAt(i);
		}
		driver.findElement(By.xpath("//input[@id='159:0;p']")).sendKeys(letra);
		Thread.sleep(2000);
		List<WebElement> desplegable = driver.findElements(By.xpath("//li[@data-aura-class='uiAutocompleteOption forceSearchInputDesktopOption']"));
		desplegable.get(1).click();
		Thread.sleep(2000);

	}
	public List<WebElement> getPlanes(WebDriver driver) throws InterruptedException{
		new WebDriverWait(driver,40).until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class,'slds-grid slds-wrap slds-box slds-grid_vertical-align-center slds-grid_align-center ng-scope viewStyle')]")));
		List<WebElement> planes = driver.findElements(By.xpath("//div[contains(@class,'slds-grid slds-wrap slds-box slds-grid_vertical-align-center slds-grid_align-center ng-scope viewStyle')]"));
		return planes;
	}

}