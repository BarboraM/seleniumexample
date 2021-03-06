package cz.mleb00.rukovoditel.selenium;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RukovoditelTest {

    protected ChromeDriver driver;
    protected WebDriverWait wait;
    protected static String BASE_URL = "https://digitalnizena.cz/rukovoditel/";
    protected static String PROJECTS_URL = BASE_URL +"index.php?module=items/items&path=21";
    private static String VALID_USERNAME = "rukovoditel";
    private static String VALID_PASSWORD = "vse456ru";

    @Before
    public void init(){

        ChromeOptions cho = new ChromeOptions();
        boolean runOnTravis = true;
        boolean windows = true;

        if (runOnTravis) {
            cho.addArguments("--headless");
            cho.addArguments("start-maximized");
            cho.addArguments("window-size=1200,1100");
        } else {
            if (windows) {
                System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
            } else {
                System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver");
            }
        }
        driver = new ChromeDriver(cho);
        wait = new WebDriverWait(driver, 3);
    }

    @After
    public void tearDown(){
        driver.close();
    }

    public void login(String username, String password){

        //Get login page URL
        driver.get(BASE_URL);

        WebElement usernameInput = driver.findElement(By.name("username"));
        usernameInput.sendKeys(username);
        WebElement passwordInput = driver.findElement(By.name("password"));
        passwordInput.sendKeys(password);

        WebElement loginButton = driver.findElement(By.cssSelector(".btn"));
        loginButton.click();
    }

    public void validLogin(){
        login(VALID_USERNAME, VALID_PASSWORD);
    }

    public void createProject(String name){

        //get projects page URL
        driver.get(PROJECTS_URL);

        //Open form for a new project
        WebElement addProjectButton = driver.findElement(By.className("btn-primary"));
        addProjectButton.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".modal-body")));

        // Select Priority
        Select prioritySelect = new Select(driver.findElement(By.id("fields_156")));
        prioritySelect.selectByVisibleText("High");

        //Project name
        WebElement projectNameInput = driver.findElement(By.cssSelector("#fields_158"));
        projectNameInput.sendKeys(name);

        //Date
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        WebElement dateInput = driver.findElement(By.id("fields_159"));
        dateInput.sendKeys(dateFormat.format(date));

        //Save project
        WebElement saveButton = driver.findElement(By.className("btn-primary-modal-action"));
        saveButton.click();
    }

    public void findProject(String name){

        //get projects page URL
        driver.get(PROJECTS_URL);
        WebElement searchTable = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".table")));

        //Search project by name
        WebElement nameInput = driver.findElementByName("entity_items_listing66_21_search_keywords");
        nameInput.clear();
        nameInput.sendKeys(name);
        WebElement searchButton = driver.findElement(By.cssSelector(".fa-search"));
        searchButton.click();

        wait.until(ExpectedConditions.stalenessOf(searchTable));
    }

    public void deleteProject(String projectName){

        findProject(projectName);

        //Click on trash icon
        WebElement trashIcon = driver.findElement(By.cssSelector(".table .fa-trash-o"));
        trashIcon.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ajax-modal")));

        //Confirm deletion
        WebElement deleteCheckbox = driver.findElement(By.id("delete_confirm"));
        deleteCheckbox.click();
        WebElement confirmDeleteButton = driver.findElement(By.className("btn-primary-modal-action"));
        confirmDeleteButton.click();
    }
}
