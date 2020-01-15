package cz.mleb00.rukovoditel.selenium;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TasksTest extends RukovoditelTest {

    private static String TYPE = "Task";
    private static String STATUS = "New";
    private static String PRIORITY = "Medium";
    private static String DESCRIPTION = "Some description";
    private ArrayList<String> statuses = new ArrayList<>();
    private ArrayList<String>  expectedStatuses = new ArrayList<>();

    private String projectName;
    private String taskName;

    @Test
    public void shouldCreateNewTask() {

        //Given
        validLogin();

        //Create project
        String projectUuid = UUID.randomUUID().toString();
        projectName = "mleb00 " + projectUuid;
        createProject(projectName);

        //Create task name
        String taskUuid = UUID.randomUUID().toString();
        taskName = "mleb00 " + taskUuid;

        //When
        createTask(taskName, TYPE, STATUS, PRIORITY, DESCRIPTION);

        //Then
        //Get and verify task info
        WebElement infoIcon = driver.findElement(By.cssSelector(".fa-info"));
        infoIcon.click();

        WebElement typeTd = driver.findElement(By.cssSelector(".form-group-167 td"));
        Assert.assertEquals(TYPE, typeTd.getText());

        WebElement taskNameField = driver.findElement(By.cssSelector(".caption"));
        Assert.assertEquals(taskName,taskNameField.getText());

        WebElement statusTd = driver.findElement(By.cssSelector(".form-group-169 td"));
        Assert.assertEquals(STATUS, statusTd.getText());

        WebElement priorityTd = driver.findElement(By.cssSelector(".form-group-170 td"));
        Assert.assertEquals(PRIORITY, priorityTd.getText());

        WebElement descriptionField = driver.findElement(By.cssSelector(".fieldtype_textarea_wysiwyg"));
        Assert.assertEquals(DESCRIPTION, descriptionField.getText());

        //Delete task and project
        deleteTask();
        deleteProject(projectName);
    }

    @Test
    public void shouldCreate7Tests(){

        //Given
        validLogin();

        //Create project
        String projectUuid = UUID.randomUUID().toString();
        projectName = "mleb00 " + projectUuid;
        createProject(projectName);

        //Statuses for the tasks
        statuses.addAll(Arrays.asList("New", "Open", "Waiting", "Done", "Closed", "Paid", "Canceled"));

        //When
        //Create 7 tasks with different statuses
        for(String status:statuses){
            taskName = "mleb00 " + status;
            createTask(taskName, TYPE, status, PRIORITY, DESCRIPTION);
        }
        resetFilter();

        //Then
        setDefaultFilter();
        verifyFilteredResults(3);

        removeOpenStatus();
        verifyFilteredResults(2);

        //Reset filter and delete tasks and project
        resetFilter();
        verifyFilteredResults(7);

        deleteTasks();
        deleteProject(projectName);
    }

    private void createTask(String name, String type, String status, String priority, String description){

        //Open form for a new task
        WebElement addTaskButton = driver.findElement(By.cssSelector(".btn-primary"));
        addTaskButton.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".modal-body")));

        //Select type
        Select typeSelect = new Select(driver.findElement(By.id("fields_167")));
        typeSelect.selectByVisibleText(type);

        //Name
        WebElement taskNameInput = driver.findElement(By.id("fields_168"));
        taskNameInput.sendKeys(name);

        //Select status
        Select statusSelect = new Select(driver.findElement(By.id("fields_169")));
        statusSelect.selectByVisibleText(status);

        //Select priority
        Select priorityStatus = new Select(driver.findElement(By.id("fields_170")));
        priorityStatus.selectByVisibleText(priority);

        //Description
        driver.switchTo().frame(driver.findElementByTagName("iframe"));
        driver.findElementByTagName("body").sendKeys(description);
        driver.switchTo().defaultContent();

        //Save task
        WebElement saveButton = driver.findElement(By.className("btn-primary-modal-action"));
        saveButton.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".fa-info")));
    }

    private void deleteTask(){
        WebElement moreActionsDropdown = driver.findElement(By.cssSelector(".prolet-body-actions .dropdown-toggle"));
        moreActionsDropdown.click();

        WebElement deleteIcon = driver.findElement(By.cssSelector(".fa-trash-o"));
        deleteIcon.click();

        WebElement confirmDeleteTaskButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#delete_item_form button.btn-primary-modal-action")));
        wait.until(ExpectedConditions.elementToBeClickable(confirmDeleteTaskButton));
        confirmDeleteTaskButton.click();
    }

    private void deleteTasks(){
        WebElement selectAllItemsInput = driver.findElement(By.cssSelector("#select_all_items"));
        selectAllItemsInput.click();

        WebElement taskMoreActionsToggle = driver.findElement(By.cssSelector(".entitly-listing-buttons-left .dropdown-toggle"));
        taskMoreActionsToggle.click();

        WebElement deleteProjectIcon = driver.findElement(By.cssSelector(".entitly-listing-buttons-left .fa-trash-o"));
        deleteProjectIcon.click();

        WebElement submitDeleteTaskButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".btn-primary-modal-action")));
        submitDeleteTaskButton.click();
    }

    private void resetFilter() {
        List<WebElement> filter = driver.findElements(By.cssSelector("a[title='Remove All Filters'] > .fa-trash-o"));

        if (!filter.isEmpty()) {
            driver.findElement(By.cssSelector("a[title='Remove All Filters'] > .fa-trash-o")).click();
        }
    }

    private void setDefaultFilter(){

        WebElement filterDropdown = driver.findElement(By.cssSelector(".portlet-filters-preview .fa-angle-down"));
        filterDropdown.click();

        WebElement defaultFilterLink = driver.findElement(By.cssSelector(".portlet-title .caption .btn-group:nth-child(1) ul li:nth-child(1) a"));
        defaultFilterLink.click();
    }

    private void removeOpenStatus(){
        setDefaultFilter();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".filters-preview-box-heading")));
        driver.findElement(By.cssSelector(".filters-preview-box-heading")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".btn-primary-modal-action")));

        WebElement choices = driver.findElement(By.cssSelector(".chosen-container ul li:nth-child(2) a"));
        choices.click();

        WebElement saveButton = driver.findElement(By.className("btn-primary-modal-action"));
        saveButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#slimScroll > table")));
    }

    private void verifyFilteredResults(int expectedNumberOfTasks){
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#slimScroll > table")));
        int result = driver.findElements(By.cssSelector(".table-scrollable > table > tbody > tr")).size();
        Assert.assertEquals(result, expectedNumberOfTasks);

    }
}
