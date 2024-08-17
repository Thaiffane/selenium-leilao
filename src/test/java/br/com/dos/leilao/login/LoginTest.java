package br.com.dos.leilao.login;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;

public class LoginTest {
    WebDriver browser;
    private static final String URL_LOGIN = "http://localhost:8080/login";

    @BeforeAll
    public static void beforeAll(){
        System.setProperty("webdriver.chrome.whitelistedIps", "");
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
    }

    @BeforeEach
    public void beforeEach() {
        this.browser = new ChromeDriver();
        this.browser.navigate().to(URL_LOGIN);
        }

    @AfterEach
    public void afterEach(){
        this.browser.quit();
    }

    @Test
    public void  deveriaEfetuarLoginComDados() {
        this.browser.findElement(By.id("username")).sendKeys("fulano");
        this.browser.findElement(By.id("password")).sendKeys("pass");
        this.browser.findElement(By.id("login-form")).submit();
        Assert.assertFalse(this.browser.getCurrentUrl().equals(URL_LOGIN));
        Assert.assertEquals("fulano", this.browser.findElement(By.id("usuario-logado")).getText());
    }

    @Test
    public void  naoDeveriaLogarComDadosInvalidos(){
        this.browser.findElement(By.id("username")).sendKeys("invalido");
        this.browser.findElement(By.id("password")).sendKeys("1234");
        this.browser.findElement(By.id("login-form")).submit();

        Assert.assertTrue(this.browser.getCurrentUrl().equals(URL_LOGIN + "?error"));
        Assert.assertTrue(this.browser.getPageSource().contains("Usuário e senha inválidos."));
        Assert.assertThrows(NoSuchElementException.class, ()-> browser.findElement(By.id("usuário-logado)")));
    }

    @Test
    public void naoDeveriaAcessarPaginaRestritaSemEstarLogado(){
        this.browser.navigate().to("http://localhost:8080/leiloes/2");
        Assert.assertTrue(this.browser.getCurrentUrl().equals((URL_LOGIN)));
        Assert.assertFalse(this.browser.getPageSource().contains("Dados do Leilão"));

    }
}
