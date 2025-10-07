from flask import Flask, request, jsonify
from .service.messageService import MessageService
from kafka import KafkaProducer
import json
import os

app = Flask(__name__)
app.config.from_pyfile('config.py')
kafka_host = os.getenv('KAFKA_HOST', 'localhost:9092')
kafka_port = os.getenv('KAFKA_PORT', '9092')
kafka_bootstrap_servers = f"{kafka_host}:{kafka_port}"
kafka_topic = os.getenv('KAFKA_TOPIC', 'expense_service')
print(f"Kafka Bootstrap Servers: {kafka_bootstrap_servers}")

messageService = MessageService()
producer = KafkaProducer(bootstrap_servers=kafka_bootstrap_servers,value_serializer=lambda v: json.dumps(v).encode('utf-8'))

@app.route('/v1/ds/message', methods=['POST'])
def handle_message():
    message = request.json.get('message')
    result = messageService.process_message(message)
    producer.send(kafka_topic,result)
    return jsonify(result)

@app.route('/', methods=['GET'])
def handle_get():
    return "Hello World"

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8093, debug=False)
