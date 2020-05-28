package com.automation.izzi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import javax.naming.directory.DirContext;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MainClass {
	
	
	//ESTE COMENTARIO SOLO DEBERÍA APLICARSE AL AMBIENTE DE SITTEST
	
	//ignorar esta linea
	public WebDriver driver;
	public WebDriverWait wait;;
	public String staticAccessLink;
	public String accountId = "0013K000008rvh7QAA";
	public String staticOrderId = "8013K000000Etx7QAC";
	public String fileToWrite;
	public String runningClass;
	
	public boolean isRunning;
	
	public int tiempo = 2000;
	
	private boolean isAutoTest = false;
	
	
	@Test
	public void startExecution() throws IOException, ClassNotFoundException {
		String[] classesList = executionOrder();
		for (int i = 0; i < classesList.length; i++) {
			runningClass = classesList[i];
			Class<?> cls = (Class<?>) Class.forName("com.automation.izzi." + runningClass);
			
			System.out.println("Started: " + runningClass);
	        System.out.println("...");
			
	        Result result = JUnitCore.runClasses(cls);

			while (result == null) {
				continue;
			}
			
			System.out.println("Finished: " + runningClass);
		}

		fileToWrite = executionFile();
		saveResponse(fileToWrite, "Test Completed!");
	}
	
	public void initBrowser() throws IOException {
		String accessUrl = "";
		if (isAutoTest)
			accessUrl = getAutoTestUrl();
		else
			accessUrl = getStaticAccessLink();
		driver.manage().window().maximize();
		driver.get(accessUrl);
	}
	
	public WebDriver setDriver() {
		System.setProperty("webdriver.chrome.driver", "./src/test/resources/chromedriver/chromedriver.exe");
		driver = new ChromeDriver();
		return driver;
	}
	
	public String[] executionOrder() {
		String[] classesList = {
				"ProcesoFVentas",
				"ProcesoEntregarPedidos",
				/*"ProcesoAltaDeServicios",
				"ProcesoCambioDeServicio",
				"ProcesoPortabilidad",
				"ProcesoGestionDeCasos",
				"ProcesoCambioDeSim",
				"ProcesoBloqueoDeIMEI",
				"ProcesoCancelacionLinea",
				"ProcesoSuspenciones",
				"ProcesoReactivacion",*/
						
				
				};
		return classesList;
	}
	
	public String getStaticAccessLink() {

		staticAccessLink = "https://test1dom--sittest.my.salesforce.com/secur/frontdoor.jsp?sid=00D3K0000008jQa!ARwAQMeIwqwwTSVOnEzACPUN0E8kWibx4TUuTjZXvGMzZY1_Fz.EZ56dhQbyp2t2BAaDyEVFvJHrwyameZUs0ndprY0r5yLd";

		return staticAccessLink;
	}
	
	public void goToAccountLink() {
		driver.get("https://test1dom--sittest.lightning.force.com/lightning/r/Account/" + accountId + "/view");
	}
	
	public void goToOrderLink() {
		driver.get("https://test1dom--sittest.lightning.force.com/lightning/r/Order/" + getOrderId() + "/view");
	}
	
	public String getAutoTestUrl() throws IOException {
		auth();
		retrieveUrl();
		String content = Files.readString(Paths.get("C:\\sfdx\\accessurl.txt"));
		String url = content.substring(content.indexOf(" http")+1);
		url.trim();
		System.out.println(url);
		return url;
	}
	
	public void retrieveUrl() throws IOException {
		ProcessBuilder builder = new ProcessBuilder(
            "cmd.exe", "/c", "sfdx force:org:open -r -u SitTest > C:\\sfdx\\accessurl.txt");
        builder.redirectErrorStream(true);
        builder.start();
	}

	public void auth() throws IOException {
		ProcessBuilder builder = new ProcessBuilder(
            "cmd.exe", "/c", "sfdx force:auth:sfdxurl:store -f C:\\sfdx\\sfdxurl.txt -a SitTest");
        builder.redirectErrorStream(true);
        builder.start();
	}

	/**
	 * Retrasa la ejecucion hasta que spinner sea invisible
	 */
	public void waitForInvisibleSpinner() {
		new WebDriverWait(driver, 40).until(ExpectedConditions.invisibilityOfElementLocated(By.className("slds-spinner_container")));
	}
	
	public String createFile() throws IOException {
        String date = LocalDate.now().toString();
        String folderName = date;
        
        new File("executions").mkdir();
        
        String absoluteFolderPath = "executions/" + folderName;
        boolean dir = new File(absoluteFolderPath).mkdir();
        if(dir){
            System.out.println(absoluteFolderPath+" Dir Created");
        }else {
        	System.out.println("Directory "+absoluteFolderPath+" already exists");
        }
        
        String[] pathList = new File(absoluteFolderPath).list();
        int txts = 0;
        for (int i=0; i < pathList.length; i++) {
			if (pathList[i].startsWith("execution") && pathList[i].endsWith(".txt")) {
				txts++;
			}
		}

        String fileName = "execution_" + (txts + 1);
        String absoluteFilePath = absoluteFolderPath + "/" + fileName + ".txt";
        File file = new File(absoluteFilePath);
        if (file.createNewFile()) {
    		System.out.println("File " + absoluteFilePath + " Created");
		}else {
        	System.out.println("File already exists in the project root directory");
        }
        
        return file.toString();
    }
	
	public String executionFile() throws IOException {
		String file = "";
		File dir = new File("executions/" + LocalDate.now().toString());
		if (!dir.exists() || dir.list().length == 0) {
			file = createFile();
		} else if(dir.list().length > 0) {
			
			String[] pathList = dir.list();
	        int txts = 0;
	        for (int i=0; i < pathList.length; i++) {
				if (pathList[i].startsWith("execution") && pathList[i].endsWith(".txt")) {
					txts++;
				}
			}
	        
			File lastFile = new File("executions/" + LocalDate.now().toString() + "/execution_" + txts + ".txt");
			if (lastFile.length() > 0) {
				Scanner scanner = new Scanner(lastFile);

			    while (scanner.hasNextLine()) {
			        String line = scanner.nextLine();
			        if(line.contains("Test Completed!")) { 
		        		file = createFile();
		        		break;
			        }else {
			        	file = lastFile.toString();
			        }
			    }
			    scanner.close();
			}else {
				file = lastFile.toString();
			}
		}
		System.out.println("File to write: " + file);
			
		return file;
	}
	
	public void saveResponse (String path, String str) throws IOException{
		FileWriter writer = new FileWriter(path, true);
		writer.write(str + "\n");
		writer.close();
		System.out.println("Successfully wrote to the file.");
	}
	
	public void returnExecutionError(String rc, Exception error) {
		try {
			fileToWrite = executionFile();
			saveResponse(fileToWrite, rc + ":\n" + error + "\n" + "-".repeat(30));
			String dirPath = fileToWrite.replace(fileToWrite.substring(fileToWrite.lastIndexOf("\\")+1), "");
			String picName = rc.replace("com.automation.izzi.", "");
			
			String[] pathList = new File(dirPath).list();
	        int jpgs = 0;
	        for (int i=0; i < pathList.length; i++) {
				if (pathList[i].startsWith(picName) && pathList[i].endsWith(".jpg")) {
					jpgs++;
				}
			}
	        
			Thread.sleep(5000);
			this.takeSnapShot(driver, dirPath + picName + "_" + jpgs + ".jpg"); 
			//driver.quit();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void returnExecutionSuccess(String rc) {
		try {
			fileToWrite = executionFile();
			saveResponse(fileToWrite, rc + ":\nSUCCESS\n" + "-".repeat(30));
			String dirPath = fileToWrite.replace(fileToWrite.substring(fileToWrite.lastIndexOf("\\")+1), "");
			String picName = rc.replace("com.automation.izzi.", "");
			
			String[] pathList = new File(dirPath).list();
	        int jpgs = 0;
	        for (int i=0; i < pathList.length; i++) {
				if (pathList[i].startsWith(picName) && pathList[i].endsWith(".jpg")) {
					jpgs++;
				}
			}
	        
			Thread.sleep(5000);
			this.takeSnapShot(driver, dirPath + picName + "_" + jpgs + ".jpg");
			//driver.quit();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void storeCreatedOrder(String order) {
		try {
			new File("executions").mkdir();
			new File("executions/order_number.txt");
			FileWriter writer = new FileWriter("executions/order_number.txt");
			writer.write(order);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getOrderId() {
		String orderId = staticOrderId;
		File file = new File("executions/order_number.txt");
		if (file.exists() && file.length() > 0) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(
						"executions/order_number.txt"));
				String line = reader.readLine();
				while (line != null) {
					orderId = line;
					break;
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return orderId;
	}
	
	public void takeSnapShot(WebDriver driver, String fileWithPath) {
		TakesScreenshot scrShot =((TakesScreenshot)driver);
		File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
		File DestFile=new File(fileWithPath);

		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(SrcFile);
	        os = new FileOutputStream(DestFile);
			byte[] buffer = new byte[1024];
			int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
			is.close();
			os.close();
	    }
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}