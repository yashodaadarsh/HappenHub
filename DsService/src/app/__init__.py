from flask import Flask
from service.eventService import EventService
from kafka import KafkaConsumer, KafkaProducer
import json
import os
import threading

app = Flask(__name__)
app.config.from_pyfile('config.py')

# Kafka configuration
kafka_host = os.getenv('KAFKA_HOST', 'localhost')
kafka_port = os.getenv('KAFKA_PORT', '9092')
kafka_bootstrap_servers = f"{kafka_host}:{kafka_port}"

# Topics
consume_topic = os.getenv('KAFKA_CONSUME_TOPIC', 'scrap_data')
produce_topic = os.getenv('KAFKA_PRODUCE_TOPIC', 'event_data')

print(f"Kafka Bootstrap Servers: {kafka_bootstrap_servers}")
print(f"Consuming from: {consume_topic}, Producing to: {produce_topic}")

# Initialize EventService
eventService = EventService()

# Kafka Producer
producer = KafkaProducer(
    bootstrap_servers=kafka_bootstrap_servers,
    value_serializer=lambda v: json.dumps(v).encode('utf-8')
)

# Function to consume, process, and produce
def consume_and_produce():
    consumer = KafkaConsumer(
        consume_topic,
        bootstrap_servers=kafka_bootstrap_servers,
        auto_offset_reset='earliest',
        group_id='event-service-group',
        value_deserializer=lambda m: json.loads(m.decode('utf-8'))
    )
    print(f"Listening to Kafka topic: {consume_topic} ...")
    for message in consumer:
        event = message.value
        print(f"Received event: {event} \n\n")
        try:
            # Process the event
            result = eventService.process_event(event)
            print(f"Processed event: {result}\n\n")

            # Produce to another topic
            producer.send(produce_topic, result)
            producer.flush()  # ensure it's sent
            print(f"Sent processed event to topic {produce_topic}\n\n")
        except Exception as e:
            print(f"Error processing event: {e}")

# Start consumer-producer in background thread
threading.Thread(target=consume_and_produce, daemon=True).start()

@app.route('/', methods=['GET'])
def handle_get():
    return "Kafka Consumer-Producer Service Running"

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8091, debug=False)
