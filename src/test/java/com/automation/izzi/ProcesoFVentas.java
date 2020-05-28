package com.automation.izzi;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.exec.util.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

//import org.testng.annotations.Test;

@RunWith(Parameterized.class)
public class ProcesoFVentas {

	private MainClass main = new MainClass();

	private WebDriver driver;

	private WebDriverWait wait;
	public int tiempo = main.tiempo;

	private int pStepDispositivos = 0;
	private int pStepValidacionDeDispositivos = 1;
	private int pStepPortabilidad = 0;
	private int pStepTipoDeEntrega = 1;
	

	private boolean pOptValidacionPorDispositivo = true;
	

	/**
	 * 
	 * @param pStepDispositivos
	 * @param pStepValidacionDeDispositivos
	 * @param pStepPortabilidad
	 * @param pStepTipoDeEntrega
	 */
	public ProcesoFVentas(int pStepDispositivos, int pStepValidacionDeDispositivos, int pStepPortabilidad, int pStepTipoDeEntrega) {
		this.pStepDispositivos = pStepDispositivos;
		this.pStepValidacionDeDispositivos = pStepValidacionDeDispositivos;
		this.pStepPortabilidad = pStepPortabilidad;
		this.pStepTipoDeEntrega = pStepTipoDeEntrega;
	}

	/** 
	 * Al iniciar, el setUp se encarga del ingreso en la aplicacion y la redireccion
	 * a la pagina correcta
	 * 
	 * @throws IOException
	 */
	@Before

	public void SetUp() throws InterruptedException, IOException {
		driver = main.setDriver();
		main.initBrowser();
		wait = new WebDriverWait(driver, 40);
		driver.get("https://test1dom--sittest.lightning.force.com/lightning/n/Nueva_Venta");
		driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);

	}

	/**
	 * El metodo principal donde se ejecutan todos los demas metodos.
	 * 
	 * @throws InterruptedException Intercepta los errores de ejecucion.
	 */

	@Test

	public void Main() throws InterruptedException {
		try {
			StepBuscarCliente();
			Thread.sleep(tiempo);
			main.returnExecutionSuccess(getClass().getName());
		} catch (Exception e) {
			main.returnExecutionError(getClass().getName(), e);
		}
	}

	/**
	 * En este paso se ingresan los datos del cliente que va a realizar la compra
	 * 
	 * @throws InterruptedException
	 */
	public void StepBuscarCliente() throws InterruptedException {
		Thread.sleep(tiempo);
		wait.until(ExpectedConditions.elementToBeClickable(By.tagName("iframe")));
		List<WebElement> frm = driver.findElements(By.tagName("iframe"));		
		
		driver.switchTo().frame(frm.get(0));
		try {
		Select pklBuscarPor = new Select(driver.findElement(By.id("selectAccountOrTlfn")));
		pklBuscarPor.selectByIndex(2);
		} catch (Exception e) {
			Select pklBuscarPor = new Select(driver.findElement(By.id("selectAccountOrTlfn")));
			pklBuscarPor.selectByIndex(2);
		}
		Thread.sleep(tiempo);
		driver.findElement(By.xpath("//input[@id='seibelUser']")).sendKeys("19892950");

		WebElement btnBuscarCliente = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='IP_validClient']")));
		btnBuscarCliente.click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("RadioUpdateDatosSeibel")));

		OptActualizarCliente(0);

		StepPlanes();
	}

	/**
	 * Selecciona entre actualizar los datos del cliente o no.
	 * 
	 * @param index 0 = "NO" || 1 = "SI"
	 * @throws InterruptedException
	 */
	public void OptActualizarCliente(Integer index) throws InterruptedException {
		wait.until(ExpectedConditions.elementToBeClickable(By.id("RadioUpdateDatosSeibel")));
		List<WebElement> optActualizarInfo = driver.findElements(By.xpath("//*[@id=\'RadioUpdateDatosSeibel\']"));
		Thread.sleep(2000);
		optActualizarInfo.get(index).findElement(By.xpath("./..")).click();
		Thread.sleep(2000);

		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("slds-spinner_container")));
		wait.until(ExpectedConditions.elementToBeClickable(By.id("SearchClient_nextBtn")));

		driver.findElement(By.xpath("//*[@id=\'SearchClient_nextBtn\']/p")).click();
		Thread.sleep(2000);

		if (index == 1)
			StepDatosAdicionalesDelCliente();
	}

	/**
	 * Actualiza los datos del cliente en caso de que se haya seleccionado esa
	 * opcion.
	 * 
	 * @throws InterruptedException
	 */
	public void StepDatosAdicionalesDelCliente() throws InterruptedException {
		main.waitForInvisibleSpinner();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("TextNumberPhone")));
		WebElement txtPhone = driver.findElement(By.id("TextNumberPhone"));
		txtPhone.clear();
		txtPhone.sendKeys("5529429653");
		Thread.sleep(1000);

		WebElement txtEmail = driver.findElement(By.id("TextEmail"));
		txtEmail.clear();
		txtEmail.sendKeys("faguilera@labsxd.com");
		Thread.sleep(1000);

		WebElement chkInformacionPrincipal = driver.findElement(By.id("CheckboxPrincipal"));
		chkInformacionPrincipal.findElement(By.xpath("./..")).click();
		Thread.sleep(1000);

		WebElement btnSiguiente = wait.until(ExpectedConditions.elementToBeClickable(By.id("AditionalInfo_nextBtn")));
		btnSiguiente.click();

		Thread.sleep(2000);
	}

	/**
	 * Selecciona el plan en el paso Planes.
	 * 
	 * @throws InterruptedException
	 */
	public void StepPlanes() throws InterruptedException {
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
		
		Thread.sleep(tiempo);
		
		WebElement btnSiguiente = wait.until(ExpectedConditions.elementToBeClickable(By.id("PlanSelection_nextBtn")));
		int tries = 0;
		while (btnSiguiente.isEnabled() && btnSiguiente.isDisplayed()) {
			tries++;
			Thread.sleep(1000);
			btnSiguiente.click();
			if (tries >= 3) {
				executor.executeScript("arguments[0].style.display = 'block'; " + "arguments[0].style.zIndex = '999999'; "
						+ "arguments[0].click()", btnSiguiente);
			}
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
		WebElement btnSiguiente = wait.until(ExpectedConditions.elementToBeClickable(By.id("StepDevicesSelect_nextBtn")));
		Thread.sleep(2000);
		btnSiguiente.click();
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
		WebElement optDispositivo = wait
				.until(ExpectedConditions.elementToBeClickable(By.id("block_01t3K000000HEDoQAO")));
		optDispositivo.findElement(By.xpath("./..")).click();
		Thread.sleep(tiempo);

		main.waitForInvisibleSpinner();
		driver.findElement(By.id("vlcCart_Top")).findElement(By.xpath(".//div[1]")).click();
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
		driver.findElement(By.xpath("//input[@id=\'NumberIMEI\']")).sendKeys("357245101804326");

		// Imei invalido
		// driver.findElement(By.xpath("//input[@id=\'NumberIMEI\']")).sendKeys("000000000000000");
		WebElement btnValidar = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id=\'WrapperValidateIMEI\']/p")));
		Thread.sleep(tiempo);
		btnValidar.click();
		Thread.sleep(tiempo);

		main.waitForInvisibleSpinner();
		List<WebElement> optListVerEquiposCompatibles = driver.findElements(By.id("RadioBuyDevices"));

		boolean optVerEquiposCompatibles = false;
		if (optListVerEquiposCompatibles.size() != 0) {
			optVerEquiposCompatibles = true;
		}
		
		WebElement btnSiguiente = wait.until(ExpectedConditions.elementToBeClickable(By.id("StepApprovedDevice_nextBtn")));
		Thread.sleep(tiempo);

		while (btnSiguiente.isEnabled() && btnSiguiente.isDisplayed()) {
			Thread.sleep(1000);
			btnSiguiente.click();
			if (!btnValidar.isDisplayed()) {
				break;
			}
		}
		StepPortabilidad(pStepPortabilidad);

		if (optVerEquiposCompatibles)
			StepSeleccionDeDispositivo();
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

		WebElement btnSiguiente = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='StepApprovedDevice_nextBtn']")));
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

		WebElement btnSiguiente = wait.until(ExpectedConditions.elementToBeClickable(By.id("StepDeviceValidation_nextBtn")));
		
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

			wait.until(ExpectedConditions.elementToBeClickable(By.id("StepSaleProcessDevice_nextBtn")));
			Thread.sleep(tiempo);
			driver.findElement(By.id("StepSaleProcessDevice_nextBtn")).click();
			Thread.sleep(5000);
			StepResumenDeCompra();

			// En caso contrario, la entrega es en Domicilio
		} else {
			optTipoDeEntrega.get(index).findElement(By.xpath("./..")).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.id("StepSaleProcessDevice_nextBtn")));
			Thread.sleep(tiempo);
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
		while (btnSiguiente.isDisplayed() && btnSiguiente.isEnabled()) {
			Thread.sleep(1000);
			btnSiguiente.click();
		}
		Thread.sleep(tiempo);
		
		main.waitForInvisibleSpinner();
		//Boton Finalizar
		WebElement btnFinish = new WebDriverWait (driver,40).until(ExpectedConditions
				.elementToBeClickable(By.xpath("//button[@class='slds-button slds-button_brand ng-binding' and contains(text(),Finalizar)]")));
		btnFinish.click();
		Thread.sleep(tiempo);
		
		main.waitForInvisibleSpinner();

		Thread.sleep(tiempo);
		String url = driver.getCurrentUrl();
		String orderId = url.substring(url.indexOf("Order/"), url.indexOf("/view")).replace("Order/", "");
		main.storeCreatedOrder(orderId);
		Thread.sleep(tiempo);
		// Nos muestra el numero de pedido
		
		main.waitForInvisibleSpinner();
		
		driver.switchTo().defaultContent();
		
		if (pStepTipoDeEntrega == 1) {
			
			JavascriptExecutor js = ((JavascriptExecutor) driver);
			js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
			Thread.sleep(5000);
			List<WebElement> categoria = driver.findElements(By.xpath("//a[@class='slds-card__header-link baseCard__header-title-container']"));
		new WebDriverWait(driver, 40).until(ExpectedConditions.elementToBeClickable(categoria.get(categoria.size()-1)));
		categoria.get(categoria.size()-1).click();
		List<WebElement> ordenServicio = driver.findElements(By.xpath("//a[@data-aura-class='forceOutputLookup']"));
		ordenServicio.get(ordenServicio.size()-1).click();
		main.waitForInvisibleSpinner();
		
		List<WebElement> picked = driver.findElements(By.xpath("//lightning-formatted-text[@data-output-element-id='output-field']"));
		String pickedID = picked.get(2).getText();
		System.out.println(pickedID);
		Thread.sleep(10000);
		}
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
	
	@Parameterized.Parameters
	public static Collection paths() {
		return Arrays.asList(new Object[][] {
			{ 0, 1, 0, 1 },
			{ 0, 0, 0, 1 },
  		});
	}
}