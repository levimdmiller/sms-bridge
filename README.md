# sms-bridge
Matrix Sms Bridge

Bridges the sms service Twilio to matrix, but is easily extendible to other services/chat servers


Run App
- Copy main/resources/application-example.properties to main/resources/application.properties and fill in values
- mvn spring-boot:run


Run Unit Tests
mvn install

Run Checkstyle
mvn checkstyle:check

Swagger Docs
http://localhost:8421/swagger-ui.html
Get the docs in json format: http://localhost:8421/v2/api-docs

