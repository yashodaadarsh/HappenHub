package com.adarsh.scrapping;

import com.adarsh.model.EventModel;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class DevpostScrapper {
    public List<EventModel> scrapWebPage(){
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        WebDriver driver = new ChromeDriver();
        Actions actions = new Actions(driver);
        List<EventModel> events = new ArrayList<>();

        try {
            driver.get("https://devpost.com/hackathons?status[]=upcoming&status[]=open");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".hackathons-container")));

            JavascriptExecutor js = (JavascriptExecutor) driver;

            boolean found = false;
            List<WebElement> hackathons = null;

            while (!found) {
                List<WebElement> list = driver.findElements(By.cssSelector(".hackathons-container .hackathon-tile"));
                try {
                    WebElement target = driver.findElement(By.xpath("//div[@class='text-center']//button[text()=' Search again ']"));
                    hackathons = list;
                    found = true;
                    break;
                } catch (NoSuchElementException ignored) {

                    actions.sendKeys(Keys.PAGE_DOWN).perform();
                    Thread.sleep(500);
                }
            }
            System.out.println("Total hackathons found: " + hackathons.size());

            for (WebElement hackathon : hackathons) {
                try {
                    String title = hackathon.findElement(By.cssSelector("h3.mb-4")).getText();
                    String link = hackathon.findElement(By.cssSelector("a.flex-row.tile-anchor")).getAttribute("href");
                    String image = hackathon.findElement(By.cssSelector("img.hackathon-thumbnail")).getAttribute("src");

                    String status = "";
                    try { status = hackathon.findElement(By.cssSelector(".status-label")).getText(); } catch (NoSuchElementException ignored) {}

                    String mode = "";
                    try { mode = hackathon.findElement(By.cssSelector(".info-with-icon .info span")).getText(); } catch (NoSuchElementException ignored) {}

                    String prize = "";
                    try {
                        prize = hackathon.findElement(By.cssSelector(".prize-amount")).getText();
                        if( prize.equals("1") || prize.equals("0") || prize.equals("") ) prize = "Visit website for more details.";
                    } catch (NoSuchElementException ignored) {}

                    String participants = "";
                    try { participants = hackathon.findElement(By.cssSelector(".participants strong")).getText(); } catch (NoSuchElementException ignored) {}

                    String host = "";
                    try { host = hackathon.findElement(By.cssSelector(".host .host-label")).getText(); } catch (NoSuchElementException ignored) {}

                    String submissionPeriod = "";
                    try { submissionPeriod = hackathon.findElement(By.cssSelector(".submission-period")).getText(); } catch (NoSuchElementException ignored) {}

                    List<WebElement> themeElements = hackathon.findElements(By.cssSelector(".themes .theme-label"));
                    List<String> themes = new ArrayList<>();
                    for (WebElement t : themeElements) {
                        themes.add(t.getText().trim());
                    }

                    System.out.println("Title: " + title);
                    System.out.println("Link: " + link);
                    System.out.println("Image: " + image);
                    System.out.println("Status: " + status);
                    System.out.println("Mode: " + mode);
                    System.out.println("Prize: " + prize);
                    System.out.println("Participants: " + participants);
                    System.out.println("Host: " + host);
                    System.out.println("Submission Period: " + submissionPeriod);
                    System.out.println("Themes: " + String.join(", ", themes));
                    System.out.println("--------------------------");

                    int idx = submissionPeriod.indexOf('-');

                    EventModel event = EventModel.builder()
                            .title(title)
                            .imageUrl(image)
                            .eventLink(link)
                            .location(host)
                            .salary(prize)
                            .startDate(submissionPeriod.substring(0,idx))
                            .endDate(submissionPeriod.substring(idx))
                            .type("Hackathon")
                            .description(String.join(", ", themes))
                            .build();
                    events.add(event);

                } catch (Exception e) {
                    System.out.println("Skipped one hackathon due to missing fields");
                }
            }


        } catch (RuntimeException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
           driver.quit();
        }
        return events;
    }
}