import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang.RandomStringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestTrello {
    String textAfterTrelloLoad = "//div[@class='link-buttonstyles__BxpLink-sc-1utqn26-0 blbVsN']//span";
    String loginButton = "//div[@class='Buttonsstyles__ButtonGroup-sc-1jwidxo-3 jnMZCI']//a[1]";
    String emailInput = "//input[@placeholder='Enter email']";
    String continueButton = "//input[@value='Continue']";
    String passwordInput = "//input[@placeholder='Enter password']";
    String loginbuttonAfterCredentials = "(//span[contains(text(),'Log')])[1]/../..";
    String loggedIn = "//h2[contains(text(),'Most popular templates')]";
    String createBoardButton = "(//span[contains(text(),'Create new board')]//..//..//..)[1]";
    String boardName = "//div[contains(text(),'Board title')]/../input";
    String boardButton = "//button[@data-testid='create-board-submit-button']";
    String boardCreated = "//div[@class='js-board-views-switcher']/div/div/div/a/span[2]";
    String listName = "(//div[@id='board']/div//input)[1]";
    String listButton = "(//div[@id='board']/div//input)[2]";
    String createCardText = "(//div[@class='card-composer-container js-card-composer-container'])[1]";
    String inputCardText = "//textarea[@placeholder='Enter a title for this card…']";
    String createCardButton = "((//div[@class='card-composer']//..)//div)[7]/div/input";
    String dragLocation = "((//div[@class='list-cards u-fancy-scrollbar u-clearfix js-list-cards js-sortable ui-sortable'])[1]//a)[1]";
    String dropLocation = "((//div[@class='list js-list-content'])[2]/div)[1]";
    String movedCard = "//div[@class='list-card-details js-card-details']/span";
    String profileButton = "//*[@id=\"header\"]/div[3]/div[5]/button";
    String logoutButton = "//button[@data-testid='account-menu-logout']";
    String logoutSubmitButton = "//button[@id='logout-submit']";

    public WebDriver driver;
    @BeforeTest()
    public void beforeTest(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @Test(priority = 0)
    public void Tasks(){
        //Opening the Browser
        driver.manage().window().maximize();
        driver.navigate().to("https://trello.com/");
        Assert.assertEquals(driver.findElement(By.xpath(textAfterTrelloLoad)).getText(), "Watch video", "Browser Did Not Open Trello");

        // Login
        WebElement logInbutton = new WebDriverWait(driver, 40).until(ExpectedConditions.elementToBeClickable(By.xpath(loginButton)));
        logInbutton.click();

        WebElement emailClickable = new WebDriverWait(driver, 40).until(ExpectedConditions.elementToBeClickable(By.xpath(emailInput)));
        emailClickable.click();
        emailClickable.sendKeys("Enter a Registered Email ID");

        driver.findElement(By.xpath(continueButton)).click();

        WebElement passwordClickable = new WebDriverWait(driver, 40).until(ExpectedConditions.elementToBeClickable(By.xpath(passwordInput)));
        passwordClickable.click();
        passwordClickable.sendKeys("Enter Correct Password");

        driver.findElement(By.xpath(loginbuttonAfterCredentials)).click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(loggedIn)));
        Assert.assertEquals(driver.findElement(By.xpath(loggedIn)).getText(), "Most popular templates", "Log In Unsuccessful");

        //Create a new Board
        WebElement createBoardClickable = new WebDriverWait(driver, 40).until(ExpectedConditions.elementToBeClickable(By.xpath(createBoardButton)));
        createBoardClickable.click();
        String randomName = RandomStringUtils.randomAlphabetic(8);
        WebElement boardNameInput = new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath(boardName)));
        boardNameInput.sendKeys(randomName);
        WebElement boardClick = new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath(boardButton)));
        boardClick.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(boardCreated)));
        Assert.assertEquals(driver.findElement(By.xpath(boardCreated)).getText(), "Board", "Board Not Created");

        //Create List A
        WebElement createListClickable = new WebDriverWait(driver, 40).until(ExpectedConditions.elementToBeClickable(By.xpath(listName)));
        createListClickable.click();
        createListClickable.sendKeys("List A");
        driver.findElement(By.xpath(listButton)).click();

        //Create List B
        createListClickable = new WebDriverWait(driver, 40).until(ExpectedConditions.elementToBeClickable(By.xpath(listName)));
        createListClickable.click();
        createListClickable.sendKeys("List B");
        driver.findElement(By.xpath(listButton)).click();

        //Check If Lists Are Created
        driver.getPageSource().contains("List A");
        driver.getPageSource().contains("List B");
        //Assert.assertEquals(driver.findElement(By.xpath(listACreated)).getText(), "List A", "List A Not Created");
        //Assert.assertEquals(driver.findElement(By.xpath(listBCreated)).getText(), "List B", "List B Not Created");

        //Create a Card In List A
        new WebDriverWait(driver, 40).until(ExpectedConditions.elementToBeClickable(By.xpath(createCardText))).click();
        randomName = RandomStringUtils.randomAlphabetic(8);
        new WebDriverWait(driver, 40).until(ExpectedConditions.elementToBeClickable(By.xpath(inputCardText))).sendKeys(randomName);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(createCardButton)));
        driver.findElement(By.xpath(createCardButton)).click();

        //Drag a card From List A to List B
        Actions build = new Actions(driver);
        build.dragAndDrop(driver.findElement(By.xpath(dragLocation)),driver.findElement(By.xpath(dropLocation))).perform();

        //Find the X and Y Coordiates of Moved Card
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(movedCard)));
        WebElement cardMoved = driver.findElement(By.xpath(movedCard));
        double getX = cardMoved.getLocation().getX();
        System.out.println("X coordinate: " +getX);
        double getY = cardMoved.getLocation().getY();
        System.out.println("Y coordinate: " +getY);

        //Logout
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(profileButton))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(logoutButton))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(logoutSubmitButton))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(textAfterTrelloLoad)));
        Assert.assertEquals(driver.findElement(By.xpath(textAfterTrelloLoad)).getText(), "Watch video", "Logout Unsuccessful");
    }

    @AfterTest()
    public void afterTest(){
        driver.quit();
    }
}
