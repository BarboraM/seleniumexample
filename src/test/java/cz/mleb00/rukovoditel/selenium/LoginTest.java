package cz.mleb00.rukovoditel.selenium;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class LoginTest extends RukovoditelTest {

    @Test
    public void successfulLogin() {

        //Given When
        validLogin();

        //Then
        Assert.assertEquals(driver.getTitle(), "Rukovoditel | Dashboard");

        WebElement userDropdown = driver.findElement(By.cssSelector(".dropdown.user"));
        Assert.assertTrue(userDropdown.isDisplayed());
    }

    @Test
    public void invalidPassword() {

        //Given When
        validLogin();

        //Then
        Assert.assertEquals(driver.getTitle(), "Rukovoditel");

        WebElement alertDiv = driver.findElement(By.cssSelector(".alert-danger"));
        Assert.assertTrue(alertDiv.isDisplayed());
        Assert.assertEquals(alertDiv.getText(),"No match for Username and/or Password.");
    }

    @Test
    public void successfulLogout() {

        //Given When
        validLogin();

        WebElement logoffLink = driver.findElement(By.cssSelector(".dropdown.user .fa.fa-sign-out"));
        logoffLink.click();

        //Then
        Assert.assertEquals(driver.getTitle(), "Rukovoditel");

        WebElement loginForm = driver.findElement(By.cssSelector("#login_form"));
        Assert.assertTrue(loginForm.isDisplayed());
    }

}
