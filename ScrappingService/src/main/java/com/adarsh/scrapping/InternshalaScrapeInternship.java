package com.adarsh.scrapping;

import com.adarsh.model.EventModel;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class InternshalaScrapeInternship {
    public List<EventModel> scrapWebPage() {

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        WebDriver driver = new ChromeDriver();
        List<EventModel> events = new ArrayList<>();

        try {
            driver.get("https://internshala.com/internships/");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            try {

                WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("/html/body/div[1]/div[21]/div/div[1]/i")
                ));

                closeBtn.click();
                System.out.println("Popup closed successfully!");

            } catch (Exception e) {
                System.out.println("No popup appeared, continuing...");
            }


            List<WebElement> jobs = driver.findElements(By.xpath("//div[contains(@class,'individual_internship')]"));

            System.out.println(jobs.size());
            for (WebElement job : jobs) {
                highlight(driver, job);
                try{

                    String title = job.findElement(By.xpath(".//a[@class='job-title-href']")).getText();

                    String link = job.findElement(By.xpath(".//a[@class='job-title-href']")).getAttribute("href");

                    String image = job.findElement(By.xpath(".//div[@class='internship_logo']//img")).getAttribute("src");

                    String location = "Not Available";
                    try {
                        WebElement locSpan = job.findElement(By.xpath(".//div[contains(@class,'row-1-item') and contains(@class,'locations')]/span"));
                        location = locSpan.getText().trim(); // e.g., "Raipur, Devpuri (Hybrid)"
                        if (location.toLowerCase().contains("remote")) {
                            location = "Remote";
                        }
                    } catch (NoSuchElementException e) {
                        location = "Not Available";
                    }


                    String description = "Not Available";
                    try {
                        description = job.findElement(By.xpath(".//div[contains(@class,'text')]")).getText();
                    } catch (Exception e) {}

                    String salary = job.findElement(By.xpath(".//div[i[contains(@class,'ic-16-money')]]/span")).getText();

                    String startDate = "";
                    try {
                        startDate = job.findElement(By.xpath(".//div[contains(@class,'color-labels')]//span")).getText();
                    } catch (Exception e) {
                        startDate = "Not Available";
                    }

                    String submissionDate = "";

                    System.out.println("Title: " + title);
                    System.out.println("Link: " + link);
                    System.out.println("Image: " + image);
                    System.out.println("Location: " + location);
                    System.out.println("Salary: " + salary);
                    System.out.println("Start Date: " + startDate);
                    System.out.println("Submission Date: " + submissionDate);
                    System.out.println("Description :- " + description);
                    System.out.println("------------------------------");

                    EventModel event = EventModel.builder()
                            .title(title)
                            .imageUrl(image)
                            .eventLink(link)
                            .location(location.toString())
                            .salary(salary)
                            .startDate(startDate)
                            .endDate(submissionDate)
                            .type("Internship")
                            .description(description)
                            .build();
                    events.add(event);

                }
                catch (NoSuchElementException e){

                }
            }
        }catch (Exception e) {
            throw new RuntimeException("Unable to Scrap the jobs of internshala");
        } finally {
            driver.quit();
        }
        return events;
    }



    public void highlight(WebDriver driver, WebElement element) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript(
                "arguments[0].setAttribute('style','border: 2px solid red; background: yellow;')",
                element
        );
    }

}