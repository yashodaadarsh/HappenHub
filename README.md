## Event Discovery Platform 🚀

This document outlines the design and architecture of the **Event Discovery Platform**, a microservices-based system built to aggregate, process, and deliver personalized event (jobs, internships, hackathons) information to users.

---

---

## 🏗 System Architecture Diagram (Visual Overview)

![Event Discovery Platform Microservices Architecture Diagram](design.svg) 

---

## 🏗 System Architecture: Microservices with Event Streaming

The system employs a **Microservices Architecture** where each business function is isolated into an independent service. **Apache Kafka** is central to this design, acting as the high-throughput, fault-tolerant message broker for asynchronous, decoupled communication between services.

### Key Architectural Principles

* **Decoupling:** Services communicate primarily via asynchronous **event streams (Kafka)**, preventing cascading failures and allowing independent scaling.
* **Domain Isolation:** Each service owns its data store, ensuring autonomy and clear data boundaries (e.g., Auth Service owns `usersdb`, Event Service owns `eventsdb`).
* **Asynchronous Processing:** Long-running tasks, like data processing (DS Service) and search indexing (Search Service), are handled asynchronously via Kafka events.
* **Mixed Communication:** While Kafka is the backbone, direct **HTTP API calls** are used for synchronous operations, such as front-end requests to the Event or Auth services, and specific inter-service triggers (e.g., from Wishlist to Mail Service).

---

## 🌐 Data Flow and Communication

The platform's operation is defined by the lifecycle of two main entities: **Events** and **Users**.

### 1. Event Data Flow (Scrape $\rightarrow$ Publish $\rightarrow$ Consume)

1.  **Ingestion:** The **Scraping Service** polls external sources and **produces** raw data to the `scrap-data` Kafka topic.
2.  **Transformation:** The **DS Service** (Data Science/Processing) **consumes** `scrap-data`, cleans it, standardizes dates, and shortens descriptions. It then **produces** the cleaned data to the `event-data` topic.
3.  **Distribution/Indexing:** Multiple services **consume** the final `event-data` for their specific functions:
    * **Event Service** stores the official record in `eventsdb`.
    * **Search Service** indexes the data in `searchdb` for fast search capabilities.
    * **Recommendation Service** uses it to train models and generate user feeds.

### 2. User/Authentication Data Flow

1.  **Authentication:** The **Auth Service** handles sign-up and login, managing user credentials in `usersdb`.
2.  **Profile Events:** Upon significant changes (e.g., new user, preference update), the **Auth Service produces** a `user-data` event.
3.  **Consumption:** The **Recommendation Service consumes** `user-data` to update user profiles and personalize recommendations.

---

## 💻 Microservices and Responsibilities

The system is composed of the following services, detailing their role and interaction with the Kafka event stream:

| Service | Primary Function | Kafka Interaction | Data Store / Communication |
| :--- | :--- | :--- | :--- |
| **Scraping Service** | External data extraction. | **Produces to:** `scrap-data` | N/A |
| **DS Service** | Data cleaning and enrichment (description shortening, date fixing). | **Consumes from:** `scrap-data` **Produces to:** `event-data` | N/A |
| **Event Service** | Core event data management (CRUD operations). | **Consumes from:** `event-data` | `eventsdb` (Primary DB) |
| **Auth Service** | User authentication and identity management. | **Produces to:** `user-data` | `usersdb` |
| **Search Service** | Fast search and filtering on all events. | **Consumes from:** `event-data` | `searchdb` (Search Index) |
| **Recommendation Service** | Personalized event feed generation based on user preferences. | **Consumes from:** `event-data`, `user-data` | `recommendationdb` |
| **Wishlist Service** | Manages user-event favorites and reminder logic. | **Produces to:** `mail-data` (Inferred) | `wishlistdb`, HTTP call to Mail Service |
| **Mail Service** | Sends transactional emails (e.g., expiring event reminders). | **Consumes from:** `mail-data` (Inferred) | N/A |

---

## 🔗 Kafka Topics (Asynchronous Contracts)

| Topic Name | Producer Service(s) | Consumer Service(s) | Event Type |
| :--- | :--- | :--- | :--- |
| **`scrap-data`** | Scraping Service | DS Service | Raw, uncleaned event JSON. |
| **`event-data`** | DS Service | Event, Search, Recommendation Services | Finalized, standardized event object. |
| **`user-data`** | Auth Service | Recommendation Service | User profile or preference changes. |
| **`mail-data`** | Wishlist Service (Inferred) | Mail Service (Inferred) | Payload to trigger an email send. |

---

## 🛠 Technology Stack (Placeholder)

* **Message Broker:** Apache Kafka
* **Containerization:** Docker, Docker Compose
* **Databases:**
    * **Transactional:** PostgreSQL/MySQL (for `eventsdb`, `usersdb`, etc.)
    * **Search:** ElasticSearch/Solr (for `searchdb`)
* **Microservice Frameworks:** (Details to be provided per service, e.g., Spring Boot, GoLang, Python/Flask)