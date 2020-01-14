package cz.mleb00.rukovoditel.selenium;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TasksTest extends RukovoditelTest {

    private static String TYPE = "Task";
    private static String STATUS = "New";
    private static String PRIORITY = "Medium";
    private static String DESCRIPTION = "Some description";

    private String projectName;
    private String taskName;

    @Test
    public void shouldCreateNewTask() {

        //Given
        validLogin();

        String projectUuid = UUID.randomUUID().toString();
        projectName = "mleb00 " + projectUuid;
        createProject(projectName);

        String taskUuid = UUID.randomUUID().toString();
        taskName = "mleb00 " + taskUuid;

        //When
        createTask();

        //Then
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

        //Delete
        deleteTask();

        deleteProject(projectName);
    }

    private void createTask(){
        WebElement addTaskButton = driver.findElement(By.cssSelector(".btn-primary"));
        addTaskButton.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".modal-body")));

        //Type
        Select typeSelect = new Select(driver.findElement(By.id("fields_167")));
        typeSelect.selectByVisibleText(TYPE);

        //Name
        WebElement taskNameInput = driver.findElement(By.id("fields_168"));
        taskNameInput.sendKeys(taskName);

        //Status
        Select statusSelect = new Select(driver.findElement(By.id("fields_169")));
        statusSelect.selectByVisibleText(STATUS);

        //Priority
        Select priorityStatus = new Select(driver.findElement(By.id("fields_170")));
        priorityStatus.selectByVisibleText(PRIORITY);

        //Description
        driver.switchTo().frame(driver.findElementByTagName("iframe"));
        driver.findElementByTagName("body").sendKeys(DESCRIPTION);
        driver.switchTo().defaultContent();

        WebElement saveButton = driver.findElement(By.className("btn-primary-modal-action"));
        saveButton.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".fa-info")));
    }

    private void deleteTask(){
        WebElement moreActionsDropdown = driver.findElement(By.cssSelector(".prolet-body-actions .dropdown-toggle"));
        moreActionsDropdown.click();

        WebElement deleteIcon = driver.findElement(By.cssSelector(".fa-trash-o"));
        deleteIcon.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ajax-modal")));
        WebElement confirmDeleteButton = driver.findElement(By.className("btn-primary-modal-action"));
        confirmDeleteButton.click();
    }
}
