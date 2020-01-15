package cz.mleb00.rukovoditel.selenium;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


public class LoginTest extends RukovoditelTest {


    @Test
    public void successfulLogin() {

        //Given
        //When
        validLogin();

        //Then
        Assert.assertEquals("Rukovoditel | Dashboard", driver.getTitle());

        WebElement userDropdown = driver.findElement(By.cssSelector(".dropdown.user"));
        Assert.assertTrue(userDropdown.isDisplayed());
    }

    @Test
    public void invalidPassword() {

        //Given
        // When
        login("rukovoditel", "invalidPassword");

        //Then
        Assert.assertEquals(driver.getTitle(), "Rukovoditel");

        //Verify alert is displayed
        WebElement alertDiv = driver.findElement(By.cssSelector(".alert-danger"));
        Assert.assertTrue(alertDiv.isDisplayed());
        Assert.assertTrue(alertDiv.getText().contains("No match for Username and/or Password."));
    }

    @Test
    public void successfulLogout() {

        //Given When
        validLogin();

        //When
        WebElement dropdownLink = driver.findElement(By.cssSelector(".dropdown.user .dropdown-toggle"));
        dropdownLink.click();

        //Click on Logoff
        WebElement logoffLink = driver.findElement(By.cssSelector(".fa-sign-out"));
        logoffLink.click();

        //Then
        Assert.assertEquals(driver.getTitle(), "Rukovoditel");

        //Verify login form is displayed
        WebElement loginForm = driver.findElement(By.cssSelector("#login_form"));
        Assert.assertTrue(loginForm.isDisplayed());
    }

}
