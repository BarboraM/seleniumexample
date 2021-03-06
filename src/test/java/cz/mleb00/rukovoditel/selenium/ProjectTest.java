package cz.mleb00.rukovoditel.selenium;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.UUID;

public class ProjectTest extends RukovoditelTest {

    private String projectName;

    @Test
    public void projectWithoutName(){

        //Given
        validLogin();
        projectName = "";

        // When
        driver.get(PROJECTS_URL);
        createProject(projectName);

        //Then
        WebElement projectNameErrorLabel = driver.findElement(By.id("fields_158-error"));
        Assert.assertEquals("This field is required!", projectNameErrorLabel.getText());
    }

    @Test
    public void successfulProjectCreation(){

        //Given
        validLogin();

        String uuid = UUID.randomUUID().toString();
        projectName = "mleb00 " + uuid;

        // When
        createProject(projectName);

        //Then
        Assert.assertEquals("Rukovoditel | Tasks", driver.getTitle());

        //Find project
        findProject(projectName);

        //Verify that the project was found
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#slimScroll tbody")));
        List<WebElement> rows = driver.findElements(By.cssSelector("table tr"));
        Assert.assertFalse(rows.isEmpty());

        WebElement tableRow = driver.findElement(By.cssSelector("#slimScroll tbody tr:nth-child(1)"));
        List<WebElement> tableData = tableRow.findElements(By.tagName("td"));
        Assert.assertEquals(projectName, tableData.get(4).getText());

        //Delete project
        deleteProject(projectName);
    }


}


