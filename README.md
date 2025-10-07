# HappenHub: Event Aggregator Platform

HappenHub is a modern, scalable event aggregation platform built on a **microservices architecture**. It uses an **event-driven design** with **Apache Kafka** as the central nervous system to ensure high availability, decoupling, and real-time data processing.

## 🚀 Architecture Overview

The system is composed of several independent services that communicate primarily through asynchronous messaging (Kafka topics). This design principle ensures that failure in one service does not propagate to others, enhancing overall system resilience.

A visual representation of the design can be found [here - *Insert Link to Excalidraw or diagram image*].

## 🧱 Core Components and Data Flow

The architecture is divided into specialized services, each responsible for a single business capability and typically managing its own data store (Database per Service pattern).

| Service | Primary Function | Kafka Role (Topics) | Database |
| :--- | :--- | :--- | :--- |
| **Scraping Service** | Ingests raw event data from external sources. | **Produces** `scrap-data` | N/A |
| **DS Service (Data Standardization)** | Cleans, normalizes, and validates raw event data. | **Consumes** `scrap-data`, **Produces** `event-data` | N/A |
| **Event Service** | Core business logic and persistence for standardized events. | **Consumes** `event-data` | **eventsdb** (Main Event Data Store) |
| **Auth Service** | Manages user registration, login, and authorization. | **Produces** `user-data` | **usersdb** |
| **Search Service** | Indexes event data to provide fast, full-text search capabilities. | **Consumes** `event-data` | **searchdb** (Search Engine) |
| **Recommendation Service** | Generates personalized event recommendations for users. | **Consumes** `event-data` & `user-data` | **recomandationdb** |
| **Wishlist Service** | Manages user-specific lists of saved/favorited events. | **Produces** `mail-data` (for event alerts) | **wishlistdb** |
| **Mail Service** | Handles all asynchronous communication with users (e.g., alerts, digests). | **Consumes** `mail-data` | N/A |

---

## 🔗 Data Communication Summary

### Asynchronous Flow (Kafka)

The bulk of the system communication is handled by Kafka, enabling decoupling and high-throughput data processing.

| Producer | Topic Name | Consumer(s) |
| :--- | :--- | :--- |
| **Scraping Service** | `scrap-data` | DS Service |
| **DS Service** | `event-data` | Event Service, Search Service, Recommendation Service |
| **Auth Service** | `user-data` | Recommendation Service |
| **Wishlist Service** | `mail-data` | Mail Service |

### Synchronous Flow (HTTP)

Services that require real-time client interaction (like user-facing APIs) use synchronous HTTP/REST communication.

* **Client (via API Gateway)** ↔ **Wishlist Service**: For adding, removing, and viewing events on a user's wishlist.
* **Wishlist Service** ↔ **Event Service**: (Likely) To retrieve real-time event details based on Wishlist IDs.

---

## 🛠️ Technology Stack (Suggested)

| Category | Technology | Rationale |
| :--- | :--- | :--- |
| **Messaging** | **Apache Kafka** | Event-driven architecture, decoupling, and fault tolerance. |
| **Service Framework** | Python (FastAPI/Flask) / Go / Java (Spring Boot) | Flexibility and performance for microservices. |
| **Primary Database** | PostgreSQL/MySQL | Reliability and transactional integrity for core event data (`eventsdb`). |
| **Search Database** | Elasticsearch/Solr | Optimized for full-text and complex search queries (`searchdb`). |
| **User/Auth DB** | MongoDB/PostgreSQL | Scalable and secure user data management (`usersdb`). |

## 💡 Key Design Decisions

1.  **Event-Driven Core:** All major data changes (scraped data, new events, user updates) are published as immutable events to Kafka. This allows services to react autonomously.
2.  **Separate Search Engine:** Indexing event data in a dedicated `searchdb` (e.g., Elasticsearch) offloads complex search queries from the primary `eventsdb`, ensuring the Event Service remains responsive.
3.  **Specialized Recommendation Engine:** The Recommendation Service uses a stream of `event-data` and `user-data` to continuously update recommendations, storing complex models or pre-calculated results in its dedicated database.

---

## 🏁 Getting Started

To run the HappenHub platform locally:

1.  **Prerequisites:** Docker, Docker Compose, Python (or chosen language runtime).
2.  **Clone the Repository:**
    ```bash
    git clone [Your Repository URL]
    cd happenhub
    ```
3.  **Start Infrastructure (Kafka, Databases):**
    ```bash
    docker-compose up -d kafka zookeeper eventsdb usersdb ...
    ```
4.  **Run Services:** Navigate to each service directory and follow its local run instructions (e.g., `python main.py` or similar).

***