package com.adarsh.scheduling;

import com.adarsh.eventProducer.EventProducer;
import com.adarsh.model.EventModel;
import com.adarsh.service.ScrapingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduledScrapingTask {

    @Autowired
    private ScrapingService scrapingService;

    @Autowired
    private EventProducer eventProducer;

    // Cron expression: At 00:00:00 (midnight) every day
    @Scheduled(cron = "0 0 0 * * *")
    public void runDailyScraping() {
        try {
            // Scrape Internshala Jobs
            List<EventModel> jobs = scrapingService.scrapIntenshalaJobs();
            jobs.forEach(eventProducer::sendEventToKafka);

            // Scrape Internshala Internships
            List<EventModel> internships = scrapingService.scrapIntenshalaInternships();
            internships.forEach(eventProducer::sendEventToKafka);

            // Scrape Devpost Hackathons
            List<EventModel> hackathons = scrapingService.scrapDevPostHackthons();
            hackathons.forEach(eventProducer::sendEventToKafka);

            System.out.println("Daily scraping completed successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error during daily scraping: " + e.getMessage());
        }
    }
}
