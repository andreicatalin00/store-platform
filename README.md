# store

## Possible TODO ideas for the project

### Add Payment/Billing service
Event-driven architecture with loosely coupled services
Similar to: Uber, Netflix, Amazon

Flow:
StoreService creates an order and emits an OrderCreated event.

BillingService listens to OrderCreated, attempts payment, emits PaymentSucceeded or PaymentFailed.

StoreService listens to those billing events and updates the order status.

### Add API Gateway

### Start using release notes and working on different branches for new features



## Kafka Topic Management

_Describe a topic:_

`kafka-topics --describe --topic test-topic --bootstrap-server localhost:9092`

_Create a topic:_

`kafka-topics --create --topic test-topic --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1`

_Send a message to the topic:_

`kafka-console-producer --topic test-topic --bootstrap-server localhost:9092`
> Hello, Kafka!

Consume messages from the topic:

`kafka-console-consumer.sh --topic test-topic --from-beginning --bootstrap-server localhost:9092`

#### Example Workflow

Create a Kafka topic:

`kafka-topics.sh --create --topic test-topic --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1`

Produce a message:

`kafka-console-producer.sh --topic test-topic --bootstrap-server localhost:9092`
> Hello, Kafka!

Consume messages:

`kafka-console-consumer --topic test-topic --from-beginning --bootstrap-server localhost:9092`

### Docker Volume Management

List Docker volumes:

`docker volume ls`

Inspect a specific volume:

`docker volume inspect <volume_name>`

Remove a Docker volume:

`docker volume rm <volume_name>`

Clean up unused volumes:

`docker volume prune`

Run bash inside the container:

`docker exec -it <container_name> /bin/bash`

Full cleanup:

`docker compose down -v`
