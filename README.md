# mailer

A tool for sending messages over REST API

## Requirements

JRE >= 11
RabbitMQ
PostgreSQL >= 9.5

## Docker commands for RabbitMQ and PostgreSQL

### Just for development and testing purposes

docker run -d --hostname my-rabbit --name some-rabbit -p 5672:5672 -p 15672:15672 rabbitmq:3-management
docker run -d -e POSTGRES_PASSWORD=11 -p 5432:5432 postgres
