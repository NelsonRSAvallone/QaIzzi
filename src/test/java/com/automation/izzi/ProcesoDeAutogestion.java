package com.automation.izzi;

import java.awt.Desktop.Action;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.By.ByTagName;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProcesoDeAutogestion {

	private MainClass main = new MainClass();

	private WebDriver driver;

	private WebDriverWait wait;
	public int tiempo = main.tiempo;

	private int pStepDispositivos = 0;
	private int pStepValidacionDeDispositivos = 1;
	private int pStepPortabilidad = 0;
	private int pStepTipoDeEntrega = 0;

	private boolean pOptValidacionPorDispositivo = true;
	// ************************LEER*****************************************************************
	// En eclipse para ir al desarrollo del metodo debo hacer CTRL + Click al
	// llamamiento del mismo.
	// En algunos casos hay metodos que estan comentados, en caso de querer cambiar
	// las elecciones solo basta con descomentar uno y comentar el otro.

	@Before
	public void setUp() throws InterruptedException {

		driver = main.setDriver();
		driver.manage().window().maximize();

		driver.get("https://sittest-izzimx.cs125.force.com/portal");
		driver.findElement(By.id("username")).sendKeys("lsalas_community@yopmail.com.sittest");
		driver.findElement(By.id("password")).sendKeys("izzi12345");
		driver.findElement(By.xpath("// input [@ class = 'button r4 wide primary']")).click();
		Thread.sleep(5000);
		driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
		wait = new WebDriverWait(driver, 40);
		Thread.sleep(15000);
	}

	@Test
	public void Main() throws InterruptedException {
		try {
			StepiniciarContratacion();
			Thread.sleep(tiempo);
			main.returnExecutionSuccess(getClass().getName());
		} catch (Exception e) {
			main.returnExecutionError(getClass().getName(), e);
		}
		 
	}

	/**
	 * Iniciar la Contratacion
	 * 
	 * @throws InterruptedException
	 */
	public void StepiniciarContratacion() throws InterruptedException {

		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].remove();", driver.findElement(By.xpath("//*[@id=\"CustomerPortalTemplate\"]/div[3]")));
		
		driver.switchTo().frame(0);
		new WebDriverWait(driver, 20)
				.until(ExpectedConditions.invisibilityOfElementLocated(By.className("slds-spinner_container")));
		driver.findElement(By.xpath("// button [@ class ='slds-button slds-button_brand btnCommunity']")).click();
		driver.switchTo().defaultContent();
		StepPlanes();
	}
 
	/**
	 * Selecciona el plan en el paso Planes.
	 * 
	 * @throws InterruptedException
	 */
	public void StepPlanes() throws InterruptedException {
		wait.until(ExpectedConditions.elementToBeClickable(By.id("iFrameResizer1")));
		WebElement frame = driver.findElement(By.id("iFrameResizer1"));
		driver.switchTo().frame(frame);
		Thread.sleep(2000);
		main.waitForInvisibleSpinner();

		// Para elegir otro plan es necesario cambiar el id por el del plan que se desea
		// seleccionar.
		List<WebElement> optPlanes = getPlanes(driver);
		
		Thread.sleep(1000);
		optPlanes.get(0).click();

		main.waitForInvisibleSpinner();
		WebElement dropDown = driver.findElement(By.id("vlcCart_Top"));
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].remove();", dropDown.findElement(By.xpath("./..")));

		main.waitForInvisibleSpinner();
		WebElement btnSiguiente = wait.until(ExpectedConditions.elementToBeClickable(By.id("PlanSelection_nextBtn")));
		
		while (btnSiguiente.isEnabled() && btnSiguiente.isDisplayed()) {
			Thread.sleep(1000);
			executor.executeScript("arguments[0].style.display = 'block'; " + "arguments[0].style.zIndex = '999999'; "
					+ "arguments[0].click()", btnSiguiente);
		}

		Thread.sleep(10000);

		StepDispositivos(pStepDispositivos);
	}

	/**
	 * Selecciona entre usar un dispositivo propio o adquirir uno nuevo
	 * 
	 * @param index 0 = "Dispositivo propio" || 1 = "Compra de dispositivo"
	 * @throws InterruptedException
	 */
	public void StepDispositivos(int index) throws InterruptedException {
		main.waitForInvisibleSpinner();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("RadioDevices")));
		List<WebElement> optTipoDeDispositivo = driver.findElements(By.id("RadioDevices"));
		optTipoDeDispositivo.get(index).findElement(By.xpath("./..")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("StepDevicesSelect_nextBtn")).click();
		Thread.sleep(5000);
		if (index == 1) {

			StepSeleccionDeDispositivo();
			// Selecciona el check que indica que el cliente no esta interesado en estos
			// equipos.
			// OptDesinteresEquipo();
		} else {
			StepValidacionDeDispositivos(pStepValidacionDeDispositivos);
		}
	}

	/**
	 * Selecciona un dispositivo en el paso Seleccion de Dispositivo.
	 * 
	 * @throws InterruptedException
	 */
	public void StepSeleccionDeDispositivo() throws InterruptedException {
		main.waitForInvisibleSpinner();
		List<WebElement> optDispositivo = getPlanes(driver);

		Thread.sleep(tiempo);
		optDispositivo.get(0).click();
		Thread.sleep(tiempo);
		
		main.waitForInvisibleSpinner();
		WebElement dropDown = driver.findElement(By.id("vlcCart_Top"));
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].remove();", dropDown.findElement(By.xpath("./..")));
		WebElement btnSiguiente = driver.findElement(By.id("StepChooseDevices_nextBtn"));
		while (btnSiguiente.isEnabled() && btnSiguiente.isDisplayed()) {
			Thread.sleep(1000);
			btnSiguiente.click();
		}
		Thread.sleep(tiempo);
		
		StepPortabilidad(pStepPortabilidad);
	}

	/**
	 * Selecciona el metodo de validacion de compatibilidad con Izzi.
	 * 
	 * @param index 0 = "IMEI" || 1 = "Dispositivo"
	 * @throws InterruptedException
	 */
	public void StepValidacionDeDispositivos(int index) throws InterruptedException {
		main.waitForInvisibleSpinner();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("RadioSelectMethod")));
		List<WebElement> optMetodoDeValidacion = driver.findElements(By.id("RadioSelectMethod"));
		Thread.sleep(tiempo);
		optMetodoDeValidacion.get(index).findElement(By.xpath("./..")).click();
		Thread.sleep(tiempo);

		if (index == 0)
			OptValidacionPorImei();
		else
			// Se debe cambiar al false para no seleccionar dispositivos de la lista
			OptValidacionPorDispositivo(pOptValidacionPorDispositivo);
	}

	/**
	 * Realiza la validacion por IMEI.
	 * 
	 * @throws InterruptedException
	 */
	public void OptValidacionPorImei() throws InterruptedException {
		// Imei valido
		driver.findElement(By.xpath("//input[@id=\'NumberIMEI\']")).sendKeys("355576090532169");

		// Imei invalido
		// driver.findElement(By.xpath("//input[@id=\'NumberIMEI\']")).sendKeys("000000000000000");
		WebElement btnValidar = driver.findElement(By.xpath("//div[@id='WrapperValidateIMEI']"));
		btnValidar.click();
		Thread.sleep(tiempo);

		main.waitForInvisibleSpinner();
		List<WebElement> optListVerEquiposCompatibles = driver.findElements(By.id("RadioBuyDevices"));

		boolean optVerEquiposCompatibles = false;
		if (optListVerEquiposCompatibles.size() != 0) {
			optVerEquiposCompatibles = true;
		}

		WebElement btnSiguiente = driver.findElement(By.xpath("//div[@id='StepApprovedDevice_nextBtn']"));
		Thread.sleep(tiempo);
		btnSiguiente.click();

		if (optVerEquiposCompatibles)
			StepSeleccionDeDispositivo();
		else 
			StepPortabilidad(pStepPortabilidad);
	}

	/**
	 * Realiza la validacion por dispositivo. Toma el parametro booleano isValid el
	 * cual establece si se selecciona un dispositivo de la lista o si por el
	 * contrario se envia al paso Seleccionar dispositivo
	 * 
	 * @param isValid true = "Selecciona un dispositivo de la lista" || false =
	 *                "Habilita la opcion 'seleccion de dispositivo'"
	 * @throws InterruptedException
	 */
	public void OptValidacionPorDispositivo(boolean isValid) throws InterruptedException {
		boolean optVerEquiposCompatibles = false;

		// Si el parametro es false:
		if (!isValid) {
			// El flujo va a seleccion de dispositivo
			WebElement chkDispositivoNoEncontrado = driver.findElement(By.xpath("//input[@id=\'CheckCompatibility\']"));
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].style.display = 'block'; " + "arguments[0].style.zIndex = '999999'; "
					+ "arguments[0].click()", chkDispositivoNoEncontrado);

			optVerEquiposCompatibles = true;

			// En caso contrario selecciona un equipo de la lista y continua a portabilidad
		} else {
			// Selecciona la marca
			driver.findElement(By.xpath("//select[@id=\'SelectBrand\']")).click();
			driver.findElement(By.xpath("//option[@label='MOTOROLA']")).click();

			// Selecciona el modelo
			driver.findElement(By.xpath("//select[@id=\'SelectModel\']")).click();
			driver.findElement(By.xpath("//option[@label='Moto G8 Plus']")).click();
		}

		WebElement btnSiguiente = driver.findElement(By.xpath("//div[@id='StepApprovedDevice_nextBtn']"));
		Thread.sleep(tiempo);

		btnSiguiente.click();
		StepPortabilidad(pStepPortabilidad);

		if (optVerEquiposCompatibles)
			StepSeleccionDeDispositivo();
	}

	/**
	 * Selecciona la opcion de portabilidad.
	 * 
	 * @param index 0 = "SI" || 1 = "NO"
	 * @throws InterruptedException
	 */
	public void StepPortabilidad(int index) throws InterruptedException {

		wait.until(ExpectedConditions.elementToBeClickable(By.id("OptionPortability")));
		Thread.sleep(1000);

		List<WebElement> optPortarNumeroActual = driver.findElements(By.id("OptionPortability"));
		optPortarNumeroActual.get(index).findElement(By.xpath("./..")).click();
		Thread.sleep(tiempo);

		WebElement btnSiguiente = driver.findElement(By.xpath("//*[@id=\'StepDeviceValidation_nextBtn\']/p"));
		Thread.sleep(1000);
		btnSiguiente.click();
		Thread.sleep(tiempo);
		StepTipoDeEntrega(pStepTipoDeEntrega);
	}

	/**
	 * Selecciona el tipo de entrega en el paso Tipo de Entrega
	 * 
	 * @param index 0 = "Entrega en Suscursal" || 1 = "Entrega en Domicilio"
	 * @throws InterruptedException
	 */
	public void StepTipoDeEntrega(int index) throws InterruptedException {
		Thread.sleep(2000);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("RadioProfileNoVentas")));
		List<WebElement> optTipoDeEntrega = driver.findElements(By.xpath("//input[@id='RadioProfileNoVentas']"));
		Thread.sleep(tiempo);

		// Si la entrega es en Sucursal:
		if (index == 0) {
			optTipoDeEntrega.get(index).findElement(By.xpath("./..")).click();
			Thread.sleep(tiempo);
			List<WebElement> stock = driver.findElements(By.xpath("//span[@class='slds-radio--faux ng-scope']"));
			// driver.findElement(By.id("RadioRetiroOtraSucursal|0")).click();//ng-form[@id='RadioRetiroOtraSucursal|0']

			// Verifica si se puede seleccionar una sucursal
			if (stock.get(0).isEnabled() && stock.get(0).isDisplayed()) {

				stock.get(1).click();

				// selecciona la sucursal "ATIZAPAN"
				driver.findElement(By.xpath("//*[@id=\'SelectSucursal\']/option[3]")).click();
				Thread.sleep(1000);

				// selecciona el boton validar
				driver.findElement(By.xpath("//div[@id=\'WrapperCheckDeviceStockSucursal\']")).click();
			}

			Thread.sleep(tiempo);
			wait.until(ExpectedConditions.elementToBeClickable(By.id("StepSaleProcessDevice_nextBtn")));
			driver.findElement(By.id("StepSaleProcessDevice_nextBtn")).click();
			Thread.sleep(5000);
			StepResumenDeCompra();

			// En caso contrario, la entrega es en Domicilio
		} else {
			optTipoDeEntrega.get(index).findElement(By.xpath("./..")).click();
			Thread.sleep(tiempo);
			wait.until(ExpectedConditions.elementToBeClickable(By.id("StepSaleProcessDevice_nextBtn")));
			driver.findElement(By.id("StepSaleProcessDevice_nextBtn")).click();
			Thread.sleep(5000);
			StepResumenDeCompra();
		}


	}

	/**
	 * Este metodo es el paso final de la gestion de compra, donde se muestra el
	 * resumen y pasa a la siguiente pantalla de finalizar compra
	 * 
	 * @throws InterruptedException
	 */
	public void StepResumenDeCompra() throws InterruptedException {
		main.waitForInvisibleSpinner();
		WebElement btnSiguiente = wait.until(
				ExpectedConditions.elementToBeClickable(By.xpath("//div[@id=\'DeliveryHomeSummary_nextBtn\']/p")));
		Thread.sleep(1000);
		btnSiguiente.click();
		Thread.sleep(tiempo);
		
		main.waitForInvisibleSpinner();
		
		WebElement btnFinish = new WebDriverWait (driver,40).until(ExpectedConditions
				.elementToBeClickable(By.xpath("//button[@class='slds-button slds-button_brand ng-binding' and contains(text(),Finalizar)]")));
		btnFinish.click();
		Thread.sleep(tiempo);
		}

	/**
	 * Selecciona el check que indica que el cliente no esta interesado en ningun
	 * equipo.
	 * 
	 * @throws InterruptedException
	 */
	public void OptDesinteresEquipo() throws InterruptedException {
		driver.findElement(By.id("CheckboxDontWantDevice")).findElement(By.xpath("./..")).click();
		Thread.sleep(tiempo);
		driver.findElement(By.id("StepChooseDevices_nextBtn")).click();
		Thread.sleep(tiempo);
		driver.findElement(By.xpath("slds-button slds-button_brand ng-binding")).click();
		Thread.sleep(tiempo);

	}
	
	public List<WebElement> getPlanes (WebDriver driver) throws InterruptedException{
		new WebDriverWait(driver,40).until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class,'slds-grid slds-wrap slds-box slds-grid_vertical-align-center slds-grid_align-center viewStyle')]")));
		List<WebElement> planes = driver.findElements(By.xpath("//div[contains(@class,'slds-grid slds-wrap slds-box slds-grid_vertical-align-center slds-grid_align-center viewStyle')]"));
		return planes;
	}

}
