package Severotek;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import java.util.*;

public class DefaultSuiteTest {

    private String tetingBrowser= new String("webdriver.gecko.driver");
    private WebDriver driver;
    private Map<String, Object> vars;
    private String title = new String("Title43565463456111111111111");
    private String slug = new String("Slug43565463456");
    private String text_markdown = new String("Title43565463456!!!!!!!!!");
    private String text = new String("Slug43565463456??????????????");
    JavascriptExecutor js;
    @Before
    public void setUp() {
        switch (tetingBrowser){
            case "webdriver.gecko.driver":
                System.setProperty("webdriver.gecko.driver","src/main/resources/geckodriver.exe");
                driver = new FirefoxDriver();
                break;
            case "webdriver.chrome.driver":
                System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
                driver = new ChromeDriver();
                break;
            case "webdriver.edge.driver":
                System.setProperty("webdriver.edge.driver", "src/main/resources/msedgedriver.exe");
                WebDriver driver = new EdgeDriver();
                break;
            default:
                Assert.fail("unsupported webdriver");
                break;

        }

        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        js = (JavascriptExecutor) driver;
        vars = new HashMap<String, Object>();
    }

    @After
    public void tearDown() {
        driver.quit();
    }


    @Test
    public void singIn() throws InterruptedException {
        driver.get("https://igorakintev.ru/admin/");


        driver.findElement(By.id("id_username")).sendKeys("selenium");
        driver.findElement(By.id("id_password")).sendKeys("super_password");
        driver.findElement(By.xpath(".//*[@value='Войти']")).click();

        assertTrue(driver.getTitle().contains("Панель управления"));

    }
    @Test
    public void fillFields() throws InterruptedException {
        driver.get("https://igorakintev.ru/admin/");
        singIn();
        driver.findElement(By.xpath(".//*[@href='/admin/blog/entry/add/']")).click();
        assertTrue(driver.getTitle().contains("Добавить entry"));

        driver.findElement(By.id("id_title")).sendKeys(title);
        driver.findElement(By.id("id_slug")).clear();
        driver.findElement(By.id("id_slug")).sendKeys(slug);
        driver.findElement(By.id("id_text_markdown")).sendKeys(text_markdown);
        driver.findElement(By.id("id_text")).sendKeys(text);
        driver.findElement(By.name("_save")).click();
    }
    @Test
    public void check() {
        driver.get("https://igorakintev.ru/blog/");
        assertTrue(driver.findElement(By.xpath(".//*[substring(@href, string-length(@href) - string-length('-" + slug + "/') +1) = '-" + slug + "/']")).getText().contains(title));

    }


    @Test
    public void delete() throws InterruptedException {


        driver.get("https://igorakintev.ru/admin/blog/entry/");

        driver.findElement(By.id("id_username")).sendKeys("selenium");
        driver.findElement(By.id("id_password")).sendKeys("super_password");
        driver.findElement(By.id("id_password")).sendKeys(Keys.ENTER);

        {
            WebElement element = driver.findElement(By.tagName("tbody"));

            System.out.println(element.getTagName());
            List<WebElement> rows = element.findElements(By.tagName("tr"));

            for (WebElement i : rows){
                List<WebElement> rowCont = i.findElements(By.tagName(("td")));
                WebElement rowTitle = i.findElement(By.tagName("th"));
                String rowSlug = rowCont.get(1).getText();
                if (rowSlug.equals(slug) && rowTitle.getText().equals(title)){
                    i.findElement(By.className("action-checkbox")).click();
                    break;
                }

            }


        }

        driver.findElement(By.name("action")).click();
        {
            WebElement dropdown = driver.findElement(By.name("action"));
            dropdown.findElement(By.xpath("//option[. = 'Удалить выбранные Entries']")).click();
        }

        driver.findElement(By.xpath(".//*[@title='Выполнить выбранное действие']")).click();
        driver.findElement(By.xpath(".//*[@value='Да, я уверен']")).click();
    }

}

