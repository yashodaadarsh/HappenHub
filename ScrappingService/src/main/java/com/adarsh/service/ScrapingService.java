package com.adarsh.service;

import com.adarsh.eventProducer.EventProducer;
import com.adarsh.model.EventModel;
import com.adarsh.scrapping.DevpostScrapper;
import com.adarsh.scrapping.InternshalaScrapeInternship;
import com.adarsh.scrapping.InternshalaScrapeJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ScrapingService {

    private static final AtomicLong counter = new AtomicLong(0);

    @Autowired
    private InternshalaScrapeJob internshalaScrapeJob;

    @Autowired
    private InternshalaScrapeInternship internshalaScrapeInternship;

    @Autowired
    private DevpostScrapper devpostScrapper;

    @Autowired
    private EventProducer eventProducer;

    public List<EventModel> scrapIntenshalaJobs() throws Exception{
        List<EventModel> jobs = internshalaScrapeJob.scrapWebPage();
        for (EventModel event : jobs ) {
            long eventId = generateUniqueEventId();

            event.setEventId(eventId);
            eventProducer.sendEventToKafka(event);
        }
        return jobs;
    }

    public List<EventModel> scrapIntenshalaInternships() {
        List<EventModel> internships = internshalaScrapeInternship.scrapWebPage();
        for (EventModel event : internships ) {
            long eventId = generateUniqueEventId();

            event.setEventId(eventId);
            eventProducer.sendEventToKafka(event);
        }
        return internships;
    }

    public List<EventModel> scrapDevPostHackthons(){
        List<EventModel> hackthons = devpostScrapper.scrapWebPage();
        for (EventModel event : hackthons ) {
            long eventId = generateUniqueEventId();

            event.setEventId(eventId);
            eventProducer.sendEventToKafka(event);
        }
        return hackthons;
    }

    public long generateUniqueEventId() {
        long base = System.currentTimeMillis() * 1_000_000 + (System.nanoTime() % 1_000_000);
        long id = counter.getAndIncrement();

        // reset if it reaches Long.MAX_VALUE
        if (id == Long.MAX_VALUE) {
            counter.compareAndSet(Long.MAX_VALUE, 0); // reset safely
        }

        return base + id;
    }

}
