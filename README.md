## Event Discovery Platform 

This repository contains the design and implementation for the Event Discovery Platform, a microservices-based system designed to aggregate, process, and deliver personalized event (jobs, internships, hackathons) information to users.

---

---

## System Architecture Diagram (Visual Overview)

![Event Discovery Platform Microservices Architecture Diagram](design.svg) 

---

## System Architecture: Microservices with Event Streaming

The system employs a **Microservices Architecture** where each business function is isolated into an independent service. **Apache Kafka** is central to this design, acting as the high-throughput, fault-tolerant message broker for asynchronous, decoupled communication between services.

### Key Architectural Principles

* **Decoupling:** Services communicate primarily via asynchronous **event streams (Kafka)**, preventing cascading failures and allowing independent scaling.
* **Domain Isolation:** Each service owns its data store, ensuring autonomy and clear data boundaries (e.g., Auth Service owns `usersdb`, Event Service owns `eventsdb`).
* **Asynchronous Processing:** Long-running tasks, like data processing (DS Service) and search indexing (Search Service), are handled asynchronously via Kafka events.
* **Mixed Communication:** While Kafka is the backbone, direct **HTTP API calls** are used for synchronous operations, such as front-end requests to the Event or Auth services, and specific inter-service triggers (e.g., from Wishlist to Mail Service).

---

## Data Transformation Example (DS Service: Input vs. Output) 
The table below illustrates the core transformation and standardization performed by the **DS Service**. It converts the inconsistent and sometimes abbreviated raw input into a clean, parsable, and standardized output format.

| Data Type | Example (Raw Input via `scrap-data`) | Example (Clean Output via `event-data`) | Transformation Focus |
| :--- | :--- | :--- | :--- |
| **Raw Input** | `start_date`: "Sep 09 ", `end_date`: "- Nov 01, 2025", `salary`: "$70,000" | `start_date`: "2025-09-09 00:00:00", `end_date`: "2025-11-01 23:59:59", `salary`: "Prize Pool: ₹62,10,925" | Date parsing, **USD to INR conversion**, salary normalization, text cleaning. |
| **Job/Internship** | `start_date`: "3 weeks ago", `salary`: "₹ 10,000 - 15,000 /month" | `start_date`: "2025-10-04 10:10:32", `salary`: "₹ 10,000 - 15,000 " | Relative date conversion, currency parsing. |

### Raw Input Data Example (DevPost Hackathon, via scrap-data)

```json

{
  "event_id": 1.7598520980326252e+18,
  "title": "Google Chrome Built-in AI Challenge 2025",
  "image_url": "https://d112y698adiu2z.cloudfront.net/photos/production/challenge_thumbnails/003/687/564/datas/medium_square.png", 
  "event_link": "https://googlechromeai2025.devpost.com/?ref_feature=challenge&ref_medium=discover",
  "location": "Global / Remote",
  "salary": "$70,000",
  "start_date": "Sep 09 ",
  "end_date": "- Nov 01, 2025",
  "type": "Hackathon",
  "description": "Beginner Friendly, Machine Learning/AI, Web"
}
```

### Standardized Output Data Example (Cleaned by DS Service, via event-data)
```json

{
  "event_id": "1759852098032625200", 
  "title": "Google Chrome Built-in AI Challenge 2025", 
  "image_url": "https://d112y698adiu2z.cloudfront.net/photos/production/challenge_thumbnails/003/687/564/datas/medium_square.png", 
  "event_link": "https://googlechromeai2025.devpost.com/?ref_feature=challenge&ref_medium=discover", 
  "location": "Global / Remote", 
  "salary": "₹6,210,925.00", 
  "start_date": "2025-09-09 00:00:00", 
  "end_date": "2025-11-01 23:59:59", 
  "type": "Hackathon", 
  "description": "This is a global, beginner-friendly hackathon focused on Machine Learning/AI and Web technologies."
}
```

## Data Flow and Communication

The platform's operation is defined by the lifecycle of two main entities: **Events** and **Users**.

### 1. Event Data Flow (Scrape $\rightarrow$ Publish $\rightarrow$ Consume)

1.  **Ingestion:** The Scraping Service automatically executes daily at 12 AM to retrieve new event data using Selenium. It also provides manual trigger APIs. The retrieved data is then produced as raw data to the scrap-data Kafka topic.
2.  **Transformation:** The **DS Service** (Data Science/Processing) **consumes** `scrap-data`, cleans it, standardizes dates, and shortens descriptions. It then **produces** the cleaned data to the `event-data` topic.
3.  **Distribution/Indexing:** Multiple services **consume** the final `event-data` for their specific functions:
    * **Event Service** stores the official record in `eventsdb` .
    * **Search Service** indexes the data into its `searchdb`, leveraging Standard SQL `LIKE` Search capabilities for query execution.
    * **Recommendation Service** stores the data in `recommendationdb` to train models and generate personalized user feeds.

### 2. User/Authentication Data Flow

1.  **Authentication:** The **Auth Service** handles sign-up and login, managing user credentials in `usersdb`.
2.  **Profile Events:** Upon significant changes (e.g., new user, preference update), the **Auth Service produces** a `user-data` event.
3.  **Consumption:** The **Recommendation Service consumes** `user-data` to update user profiles and personalize recommendations.

---

## Microservices and Responsibilities

The system is composed of the following services, detailing their role and interaction with the Kafka event stream:

| Service | Primary Function | Kafka Interaction | Data Store / Communication |
| :--- | :--- | :--- | :--- |
| **Scraping Service** | External data extraction. Runs on a daily schedule (12 AM). | **Produces to:** `scrap-data` | N/A |
| **DS Service** | Data cleaning and enrichment (description shortening, date fixing). | **Consumes from:** `scrap-data` **Produces to:** `event-data` | N/A |
| **Event Service** | Core event data management (CRUD operations). | **Consumes from:** `event-data` | `eventsdb` (Primary DB) |
| **Auth Service** | User authentication and identity management. | **Produces to:** `user-data` | `usersdb` |
| **Search Service** | Fast search and filtering on all events. | **Consumes from:** `event-data` | `searchdb` (Search Index) |
| **Recommendation Service** | Personalized event feed generation based on user preferences. | **Consumes from:** `event-data`, `user-data` | `recommendationdb` |
| **Wishlist Service** | Manages user-event favorites and reminder logic. | **Produces to:** `mail-data` (Inferred) | `wishlistdb`, HTTP call to Mail Service |
| **Mail Service** | Sends transactional emails (e.g., expiring event reminders). | **Consumes from:** `mail-data` (Inferred) | N/A |

---

## Kafka Topics (Asynchronous Contracts)

| Topic Name | Producer Service(s) | Consumer Service(s) | Event Type |
| :--- | :--- | :--- | :--- |
| **`scrap-data`** | Scraping Service | DS Service | Raw, uncleaned event JSON. |
| **`event-data`** | DS Service | Event, Search, Recommendation Services | Finalized, standardized event object. |
| **`user-data`** | Auth Service | Recommendation Service | User profile or preference changes. |
| **`mail-data`** | Wishlist Service (Inferred) | Mail Service (Inferred) | Payload to trigger an email send. |


---

## Technology Stack Overview

| Category                     | Technology           | Services Used In           | Purpose & Notes                                                                                   |
|-------------------------------|-------------------|---------------------------|--------------------------------------------------------------------------------------------------|
| Microservices Development      | Spring Boot (Java) | All Services (except DS)  | Primary framework for robust, scalable backend services.                                         |
| Python / LangChain             | Python / LangChain | DS Service Only           | Used for data cleaning, transformation, and complex logic involving AI/NLP.                     |
| Asynchronous Communication     | Apache Kafka       | All Services              | The central, high-throughput message broker for event streaming (Topics: `scrap-data`, `event-data`, `user-data`, `mail-data`). |
| Data Storage                   | MySQL              | All Services              | The single source of truth for all transactional data (`eventsdb`, `usersdb`, `wishlistdb`, `recommendationdb`). |
| Containerization               | Docker / Docker Compose | All Services & Infrastructure | Used for local development, defining the entire environment, and creating deployment artifacts. |
| Scraping Tool                  | Selenium           | Scraping Service          | Used for automated browser interaction to extract data from dynamic websites.                   |

---

---

## Service Structure

- **All Services (except DS):** Java Spring Boot microservices.  
- **DS Service:** Python service leveraging LangChain for AI/NLP tasks.  
- **Search Service:** Provides flexible SQL-like search capabilities.  
- **Scraping Service:** Uses Selenium to fetch dynamic content from websites and provides APIs for event data.  
- **Kafka Topics:** `scrap-data`, `event-data`, `user-data`, `mail-data`.  

---

## Scraping Service APIs

These APIs belong to the **Scraping Service**.

### 1. Get Internshala Jobs

**Request:**  
```http
GET scrapping-service/api/v1/data/internshala/jobs
```
**Response Example:**
```json
[
  {
    "event_id": 1.7598505746792433e+18,
    "title": "Video Editor",
    "image_url": "https://internshala.com/static/images/search/placeholder_logo.svg",
    "event_link": "https://internshala.com/internship/detail/video-editor-internship-in-ahmedabad-at-ranjan-rana1756799951",
    "location": "Ahmedabad",
    "salary": "₹ 10,000 - 15,000 /month",
    "start_date": "3 weeks ago",
    "end_date": "",
    "type": "Internship",
    "description": "Intern responsibilities at Iravaa Media (video editing)..."
  },
  {
    "event_id": 1.7598506012960074e+18,
    "title": "Search Engine Optimization (SEO)",
    "image_url": "https://internshala-uploads.internshala.com/logo%2Fg13qfru3a3p-27596.jpg.webp",
    "event_link": "https://internshala.com/internship/detail/search-engine-optimization-seo-internship-in-multiple-locations-at-devakey-digital-solutions-pvt-ltd1758861418",
    "location": "Pune, Pimpri-Chinchwad",
    "salary": "₹ 4,000 - 5,000 /month",
    "start_date": "1 week ago",
    "end_date": "",
    "type": "Internship",
    "description": "1. Conduct client research and keyword analysis to support SEO strategies..."
  }
]

```

### 2. Get Internshala Internships

**Request:**  
```http
GET scrapping-service/api/v1/data/internshala/internships
```

**Response Example:**
```json
[
  {
    "event_id": 1.7598518103629379e+18,
    "title": "Business Development Executive",
    "image_url": "https://internshala.com/static/images/search/placeholder_logo.svg",
    "event_link": "https://internshala.com/job/detail/business-development-executive-job-in-indore-at-toonzkart-ventures-private-limited1758867799",
    "location": "Indore",
    "salary": "₹ 3,80,000 - 4,64,000",
    "start_date": "1 week ago",
    "end_date": "",
    "type": "Job",
    "description": "As a business development executive at Toonzkart Ventures Private Limited, you will have the opportunity to work in a dynamic and fast-paced environment, where your skills in effective communication, written and spoken English proficiency, and MS Excel will be put to the test. Key Responsibilities: A. Leads calling & follow-up: 1. Demonstrate exceptional verbal and written communication abilities 2. Deliver an outstanding presentation 3. Practice active listening and empathy 4. Apply cross-cultural communication proficiency 5. Build rapport with diverse stakeholders B. Mailing: 1. Design compelling business presentations 2. Develop comprehensive business proposals 3. Visualize complex business opportunities 4. Communicate value propositions effectively 5. Demonstrate excellent prioritization and multitasking abilities 6. Show adaptability in dynamic business environments 7. Maintain meticulous attention to detail If you are a motivated individual with a passion for driving business success, we invite you to join our team and be a part of our exciting journey towards achieving our business goals. Apply now and take the next step in your career with Toonzkart Ventures Private Limited."
  },
  {
    "event_id": 1.7598518110265142e+18,
    "title": "Sales Executive",
    "image_url": "https://internshala-uploads.internshala.com/logo%2Fex08empjmk9-15831.jpg.webp",
    "event_link": "https://internshala.com/job/detail/sales-executive-job-in-hyderabad-at-spotlet1757013406",
    "location": "Hyderabad",
    "salary": "₹ 4,00,000 - 8,00,000",
    "start_date": "3 weeks ago",
    "end_date": "",
    "type": "Job",
    "description": "Key responsibilities: 1.Identify, prospect, and generate leads through various channels including referrals, networking, and real estate portals. 2.Conduct property presentations and site visits, highlighting features and benefits of villas and farmhouses. 3.Manage the entire sales cycle from lead generation to deal closure. 4.Build long-term client relationships to ensure repeat business and referrals. 5.Negotiate terms, finalize agreements, and ensure smooth transaction processes. 6.Achieve and exceed monthly/quarterly sales targets. 7.Provide accurate sales forecasts and market insights to management. 8.Maintain detailed records of interactions, leads, and closed deals in CRM. 9.Represent the company professionally at client meetings, events, and property exhibitions"
  },
]
```

### 3. Get DevPost Hackathons

**Request:**  
```http
GET scrapping-service/api/v1/data/devposts/hackathons
```


**Response Example:**
```json
[
  {
    "event_id": 1.7598520980326252e+18,
    "title": "Google Chrome Built-in AI Challenge 2025",
    "image_url": "https://d112y698adiu2z.cloudfront.net/photos/production/challenge_thumbnails/003/687/564/datas/medium_square.png",
    "event_link": "https://googlechromeai2025.devpost.com/?ref_feature=challenge&ref_medium=discover",
    "location": "Google",
    "salary": "$70,000",
    "start_date": "Sep 09 ",
    "end_date": "- Nov 01, 2025",
    "type": "Hackathon",
    "description": "Beginner Friendly, Machine Learning/AI, Web"
  },
  {
    "event_id": 1.759852098057366e+18,
    "title": "AWS AI Agent Global Hackathon",
    "image_url": "https://d112y698adiu2z.cloudfront.net/photos/production/challenge_thumbnails/003/702/784/datas/medium_square.png",
    "event_link": "https://aws-agent-hackathon.devpost.com/?ref_feature=challenge&ref_medium=discover",
    "location": "AWS",
    "salary": "$45,000",
    "start_date": "Sep 08 ",
    "end_date": "- Oct 20, 2025",
    "type": "Hackathon",
    "description": "DevOps, Enterprise, Machine Learning/AI"
  },
]
```

### 4. Publish Event to Kafka

**Request:**  
```json
POST scrapping-service/api/v1/data/events
Content-Type: application/json

{
  "event_id": 1759850574679243300,
  "title": "Sample Event",
  "image_url": "https://example.com/logo.png",
  "event_link": "https://example.com/event",
  "location": "Remote",
  "salary": "",
  "start_date": "2025-10-07",
  "end_date": "2025-10-15",
  "type": "Job",
  "description": "Sample event description."
}

```

**Response Example:**
```json
Event published successfully.
```


## Event Service API Endpoints
This section provides details and examples for the core data management API in the **Event Service**.

### 1. Create Event
**Request**
```http
Endpoint: event-service/api/v1/events

Method: POST

Role: The core service endpoint used internally (or by authorized clients) to persist standardized event data.
```

#### Request Body (Example):

```json
JSON
{
  "eventId": 101,
  "title": "Spring Boot Developer Job",
  "location": "Remote",
  "salary": "₹ 15,00,000",
  "start_date": "2025-11-01 09:00:00",
  "end_date": "2025-11-30 17:00:00",
  "type": "Job",
  "description": "Full-stack developer opportunity..."
}
```
Success Response (201 CREATED): Returns the saved EventModel object.

### 2. Update Existing Event
**Request**
```http
Endpoint: event-service/api/v1/events/{id}

Method: PUT

Success Response (200 OK): Returns the updated EventModel object.
```

### 3. Get Event by ID
**Request**
```http
Endpoint: event-service/api/v1/events/{id}

Method: GET

Success Response (200 OK): Returns a single EventModel.
```
**Response**
```JSON

{
  "event_id": 101,
  "title": "Spring Boot Developer Job",
  "location": "Remote",
  "salary": "₹ 15,00,000",
  "start_date": "2025-11-01 09:00:00",
  "end_date": "2025-11-30 17:00:00",
  "type": "Job",
  "description": "Full-stack developer opportunity..."
}
```

### 4. Get All Events
**Request**
```http
Endpoint: event-service/api/v1/events

Method: GET

Success Response (200 OK): Returns a list of EventModel objects.
```

### 5. Get Events by List of IDs
**Request**
```http
Endpoint: event-service/api/v1/events/by-ids?eventsIdList=101,102,105

Method: GET

Success Response (200 OK): Returns a list of EventModel objects matching the provided IDs.
```





