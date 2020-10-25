# mailer

A tool for sending messages over REST API

## Requirements

JRE >= 11

## SMS sending

In this case used devinotele.com. Implement your own 'SmsSender'

## SOAP client generating

Uncomment 'maven-jaxb2-plugin' in pom.xml and run jaxb2 maven plugin
Copy new client classes to src from target/generated-sources/jaxb