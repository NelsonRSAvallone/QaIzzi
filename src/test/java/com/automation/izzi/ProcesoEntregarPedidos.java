package com.automation.izzi;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProcesoEntregarPedidos {
	
	private MainClass main = new MainClass();
	private WebDriver driver;
	private WebDriverWait wait;
			//************************LEER*****************************************************************
			// En eclipse para ir al desarrollo del metodo debo hacer CTRL + Click al llamamiento del mismo.
			// En algunos casos hay metodos que estan comentados, en caso de querer cambiar las elecciones solo basta con descomentar uno y comentar el otro.
	
	@Before
	public void setUp() throws InterruptedException, IOException {
		

		driver = main.setDriver();
		main.initBrowser();
		main.goToOrderLink();
		wait = new WebDriverWait (driver, 40);
		driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
		
		Thread.sleep(20000);
	}
	
	@Test
	public void testScript() throws InterruptedException {
		try {
			clickEntregaDePedido();
			accid();
			main.returnExecutionSuccess(getClass().getName());
		} catch (Exception e) {
			main.returnExecutionError(getClass().getName(), e);
		}
	}
	//------------------------------------------------METODOS--------------------------------------------------
	public void clickEntregaDePedido () throws InterruptedException{
		By iFrame1 = By.id("iFrameResizer0");
		new WebDriverWait (driver,40).until(ExpectedConditions.visibilityOfElementLocated(iFrame1));
		driver.switchTo().frame("iFrameResizer0");
		driver.findElement(By.xpath("/html/body/div[1]/div[1]/ng-include/div/div/section/div/button")).click();
		driver.switchTo().defaultContent();
		Thread.sleep(5000);
	}
		
	//----------------------------------------------------------------------------------------------------------	
		public void accid () {
		//8952140061736667340F

		//Creacion de lista de Iframes para saleccionar el ultimo de la lista.
		List<WebElement> cantIFrames = driver.findElements(By.xpath("//iFrame"));
		int size = cantIFrames.size();
		driver.switchTo().frame(size - 1);
		
		main.waitForInvisibleSpinner();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='ICCID']")));
		driver.findElement(By.xpath("//input[@id=\'ICCID\']")).sendKeys("7962147864323791419F");
		driver.findElement(By.xpath("//input[@id='ICCIDVal']")).sendKeys("7962147864323791419F");

		
		
		try {
			Thread.sleep(2000);
			driver.findElement(By.xpath("//div[@id='WrapperValidarICCID']/p")).click();
			Thread.sleep(5000);
			
		//-----GUARDAR!!!!---- solo descomentar si se esta seguro que se quiere generar/guardar el Pedido.
	
		driver.findElement(By.xpath("//div[@id='DeliverySimCard_nextBtn']/p")).click();
		Thread.sleep(3000);
		//-----Finalizar-------

		main.waitForInvisibleSpinner();
		
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	}
		

}