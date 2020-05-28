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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProcesoGestionDeCasos {
	
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
	public void Gestion () throws InterruptedException {
		try {
			main.waitForInvisibleSpinner();
			WebElement frame = driver.findElement(By.id("iFrameResizer1"));
			JavascriptExecutor executor = (JavascriptExecutor)driver;
			executor.executeScript("arguments[0].style.display = 'block'; arguments[0].style.zIndex = '999999';", frame);
			driver.switchTo().frame(frame);
			Thread.sleep(2000);
			WebElement boton = driver.findElement(By.xpath("/html/body/div[1]/div[1]/ng-include/div/div/section/div[6]/button"));
			executor.executeScript("arguments[0].click();", boton);
			
			driver.switchTo().defaultContent();
			
			
			CrearModificarCaso(1);
			main.returnExecutionSuccess(getClass().getName());
		} catch (Exception e) {
			main.returnExecutionError(getClass().getName(), e);
		}

		
	}
	
	public void CrearModificarCaso(int index) throws InterruptedException {
		main.waitForInvisibleSpinner();
		WebElement frame = new WebDriverWait(driver, 40)
				.until(ExpectedConditions.elementToBeClickable(By.id("iFrameResizer3")));
		frame.click();
		driver.switchTo().frame(frame);
		
		new WebDriverWait(driver, 40)
			.until(ExpectedConditions.elementToBeClickable(By.id("RadioOptions")));
		List<WebElement> opt = driver.findElements(By.id("RadioOptions"));
		Thread.sleep(2000);
		WebElement imagen = new WebDriverWait(driver, 40).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@title='Crear Caso']")));
		if(imagen.isEnabled()& imagen.isDisplayed()) {
			opt.get(index).findElement(By.xpath("./..")).click();
		}
		Thread.sleep(2000);		
		driver.findElement(By.id("GestionCasos_nextBtn")).click();
		Thread.sleep(2000);
		if (index == 0)
			Crear();
		else
			Modificar();
		
	}

	
	public void Crear() throws InterruptedException {
		main.waitForInvisibleSpinner();
		SelectPicklist("Origen");
		SelectPicklist("Prioridad");
		SelectPicklist("Tipo");
		SelectPicklist("MotivoInfGeneral");
		
		driver.findElement(By.id("CrearCaso_nextBtn")).click();
		Thread.sleep(2000);
		descripcion();
	}
	
	void Modificar() throws InterruptedException {
		Thread.sleep(3000);
		main.waitForInvisibleSpinner();
		new WebDriverWait(driver, 40).until(ExpectedConditions.elementToBeClickable(By.id("CaseSelect")));
		
		List<WebElement> casos = driver.findElements(By.xpath("//span[@class = 'slds-radio_faux']"));
		casos.get(0).click();
		Thread.sleep(2000);
		
		driver.findElement(By.xpath("//div[@id='Casos_nextBtn']")).click();
		Thread.sleep(2000);
		
		Edicion();
	}

	void SelectPicklist(String id) throws InterruptedException {
		Select picklist = new Select(driver.findElement(By.id(id)));
		picklist.selectByIndex(1);
		Thread.sleep(1000);
	}
	
	



	//Una vez que entramos a Crear caso. esto llenaria la descripcion del mismo y finaliza el proceso.
	
	void descripcion () throws InterruptedException {
		main.waitForInvisibleSpinner();
		driver.findElement(By.xpath("//*[@id=\'TextAreaAsunto\']")).sendKeys("Test");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\'TextAreaDescripcion\']")).sendKeys("Testing");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\'TextAreaComentarios\']")).sendKeys("Automation");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\'Descripcion_nextBtn\']")).click();
		

		new WebDriverWait (driver, 20)
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@ng-if='control.propSetMap.structMessage.btnName']")));

		String orden = driver.findElement(By.xpath("//p[@ng-repeat='(key, value) in control.propSetMap.message']")).getText();
		driver.switchTo().defaultContent();
		String letra = "";
		for (int i = 5; i < orden.length(); i++) {
		letra = letra + orden.charAt(i);
		}
		driver.findElement(By.xpath("//input[@id='159:0;p']")).sendKeys(letra);
		Thread.sleep(2000);
		List<WebElement> desplegable = driver.findElements(By.xpath("//li[@data-aura-class='uiAutocompleteOption forceSearchInputDesktopOption']"));
		desplegable.get(1).click();
	}

	
	//Esto es en "Modificar caso", para su edición y finalización.
	void Edicion() throws InterruptedException{
		main.waitForInvisibleSpinner();
		
		Select picklist = new Select(driver.findElement(By.id("SelectEstado")));
		picklist.selectByIndex(1);
		Thread.sleep(1000);
		
		driver.findElement(By.xpath("//*[@id=\'TextAreaComentarios2\']")).sendKeys("Hi");
		Thread.sleep(1000);
		
		driver.findElement(By.xpath("//div[@id='Edicion_nextBtn']")). click();
		Thread.sleep(5000);
		
		main.waitForInvisibleSpinner();

		new WebDriverWait (driver, 20)
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@ng-if='control.propSetMap.structMessage.btnName']")));

		String orden = driver.findElement(By.xpath("//p[@ng-repeat='(key, value) in control.propSetMap.message']")).getText();
		driver.switchTo().defaultContent();
		String letra = "";
		for (int i = 5; i < orden.length(); i++) {
		letra = letra + orden.charAt(i);
		}
		driver.findElement(By.xpath("//input[@id='159:0;p']")).sendKeys(letra);
		Thread.sleep(2000);
		List<WebElement> desplegable = driver.findElements(By.xpath("//li[@data-aura-class='uiAutocompleteOption forceSearchInputDesktopOption']"));
		desplegable.get(1).click();
	}
	
}
