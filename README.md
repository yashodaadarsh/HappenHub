```markdown
# 🚀 HappenHub Scraping Service

This microservice is responsible for **scraping event, job, internship, and hackathon data** from external websites (like **Internshala** and **DevPost**) and publishing the collected data to an **Apache Kafka** topic named **`scrap_data`**.

It is part of the **HappenHub ecosystem**, designed for event aggregation and distribution.

---

## 🧠 Overview

- 🕸️ Scrapes data from trusted platforms.
- 📨 Publishes the scraped data to **Kafka (`scrap_data` topic)**.
- 🧱 Built using **Spring Boot**, **Kafka**, and **Java 17**.
- 🧩 Exposes REST endpoints for testing and manual triggering.

---

## 🌐 Base URL

All API routes are prefixed with:

```

/api/v1/data

```

Example full path:
```

[http://localhost:8080/api/v1/data/internshala/jobs](http://localhost:8080/api/v1/data/internshala/jobs)

```

---

## ⚙️ Tech Stack

| Component | Technology |
|------------|-------------|
| Framework | Spring Boot |
| Messaging | Apache Kafka |
| Language | Java 17+ |
| JSON Processing | Jackson |
| Build Tool | Gradle / Maven |

---

## 📁 Project Structure

```

src/main/java/com/adarsh/
├── controller/
│   └── ScrapingController.java
├── eventProducer/
│   └── EventProducer.java
├── model/
│   └── EventModel.java
└── service/
└── ScrapingService.java

````

---

## 🧭 API Endpoints

### 1️⃣ Scraping Endpoints (GET)

These endpoints trigger scraping operations for different platforms.  
The results are both **returned to the client** and **sent to Kafka** (`scrap_data` topic).

| HTTP Method | Endpoint | Description | Success Status |
| :--- | :--- | :--- | :--- |
| `GET` | `/internshala/jobs` | Scrapes available **jobs** from Internshala. | `200 OK` |
| `GET` | `/internshala/internships` | Scrapes available **internships** from Internshala. | `200 OK` |
| `GET` | `/devposts/hackathons` | Scrapes ongoing or upcoming **hackathons** from DevPost. | `200 OK` |

#### ✅ Example Success Response (`200 OK`)

```json
[
  {
    "event_id": 101,
    "title": "Software Development Internship",
    "image_url": "https://internshala.com/logo.png",
    "event_link": "https://internshala.com/internship/software-dev",
    "location": "Remote",
    "salary": "₹15,000 / month",
    "start_date": "2025-06-01",
    "end_date": "2025-12-31",
    "type": "Internship",
    "description": "Develop and maintain backend systems."
  }
]
````

---

### 2️⃣ Event Publishing Endpoint (POST)

Manually publish a custom event to the Kafka topic **`scrap_data`**.

| HTTP Method | Endpoint  | Description                               | Success Status |
| :---------- | :-------- | :---------------------------------------- | :------------- |
| `POST`      | `/events` | Publishes a custom `EventModel` to Kafka. | `201 Created`  |

#### 🧾 Request Body (`EventModel`)

| Field         | Type     | Description                                           |
| :------------ | :------- | :---------------------------------------------------- |
| `event_id`    | `Long`   | Unique identifier for the event.                      |
| `title`       | `String` | The title of the event/job/internship.                |
| `image_url`   | `String` | URL to an image or logo.                              |
| `event_link`  | `String` | Link to the event or listing.                         |
| `location`    | `String` | Location or “Remote”.                                 |
| `salary`      | `String` | Salary or stipend (if applicable).                    |
| `start_date`  | `String` | Start date (format: `YYYY-MM-DD`).                    |
| `end_date`    | `String` | End date or deadline.                                 |
| `type`        | `String` | Type of the event (e.g., Job, Internship, Hackathon). |
| `description` | `String` | Detailed event description.                           |

#### Example Request

```json
{
  "title": "Hack the Future 2025",
  "image_url": "https://devpost.com/hackathon.png",
  "event_link": "https://devpost.com/hackathons/hackthefuture",
  "location": "Online",
  "salary": "N/A",
  "start_date": "2025-12-01",
  "end_date": "2025-12-03",
  "type": "Hackathon",
  "description": "A global hackathon for innovators."
}
```

#### ✅ Example Response (`201 Created`)

```
Event published successfully.
```

---

## 🛑 Error Handling

| Status Code                 | Description                                                         | Example Body                                 |
| :-------------------------- | :------------------------------------------------------------------ | :------------------------------------------- |
| `500 Internal Server Error` | Occurs when scraping fails or Kafka publishing encounters an error. | `"Failed to retrieve Internshala job data."` |

#### Example:

```
HTTP 500 INTERNAL_SERVER_ERROR
Body: "Failed to publish event."
```

---

## ⚙️ Kafka Configuration

In `application.yml`:

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
```

### Kafka Topic

| Property          | Value                        |
| ----------------- | ---------------------------- |
| **Topic Name**    | `scrap_data`                 |
| **Message Key**   | Event ID or UUID             |
| **Message Value** | Serialized `EventModel` JSON |

---

## 🧠 How It Works

```mermaid
flowchart TD
    A[Website Sources (Internshala, DevPost)] --> B[ScrapingService]
    B --> C[ScrapingController]
    C --> D[EventProducer]
    D --> E[Kafka Topic: scrap_data]
    E --> F[Consumer Microservices]
```

1. The controller triggers the `ScrapingService`.
2. The service scrapes website data and converts it into `EventModel` objects.
3. The `EventProducer` publishes the data to Kafka topic **`scrap_data`**.
4. Consumer microservices subscribe to this topic for further processing (like storing or analyzing).

---

## 🧰 Development Setup

### Prerequisites

* Java 17+
* Kafka & Zookeeper running locally
* Gradle or Maven
* Internet connection for scraping

### Run the Service

```bash
./gradlew bootRun
```

or

```bash
mvn spring-boot:run
```

---

## 🧩 Event Model Definition

```java
@Data
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventModel {
    private Long eventId;
    private String title;
    private String imageUrl;
    private String eventLink;
    private String location;
    private String salary;
    private String startDate;
    private String endDate;
    private String type;
    private String description;
}
```

---

## 📜 License

This project is licensed under the **MIT License** — you are free to modify and use it.

---

## 👤 Author

**Adarsh Yashoda**
🎓 Rajiv Gandhi University of Knowledge Technologies, Basar
💻 Backend Developer | Java | Spring Boot | Kafka | Selenium
📧 [[adarsh@example.com](mailto:adarsh@example.com)]

---

```

---

Would you like me to add a **Postman collection JSON export section** (so you can directly import and test these APIs in Postman)?
```
