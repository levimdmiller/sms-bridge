# sms-bridge
Matrix Sms Bridge

Bridges the sms service Twilio to matrix, but is easily extendible to other services/chat servers


Run App
- Copy main/resources/application-example.properties to main/resources/application.properties and fill in values
- mvn spring-boot:run


Run Unit Tests
- mvn install

Run Checkstyle
- mvn checkstyle:check

Swagger Docs

http://localhost:8421/swagger-ui.html

Get the docs in json format: http://localhost:8421/v2/api-docs

# Installation:
https://matrix.org/docs/guides/application-services/#what-application-services-can-do-for-you

Create an sms-registration.yaml file (see above link):
```
id: "some-unique-id"
url: "https://my-hosted-sms-bridge.ca"

as_token: <generate-random-strings-here>
hs_token: <generate-random-strings-here>

# this is the local part of the desired user ID for this AS (in this case @logging:localhost)
sender_localpart: sms
namespaces:
  users: 
    - exclusive: false
      regex: "@sms-.*"
  rooms: []
  aliases:
    - exclusive: false
      regex: "#sms-.*"
```

Add the sms-registration file to homeserver.yml and restart:
```
app_service_config_files:
  - "/path/to/appservice/registration.yaml"
```

Create a user in the `security_user` table for twilio to auth with 
([Online BCrypt Hasher](https://bcrypt-generator.com/)):
```
insert into security_user(username, password, role)
values('whatever-username', 'bcrypt-hashed-password', 'TWILIO_SERVER');
```

Configure Twilio to make POST requests to 
`https://username:password@<hosted-domain>/twilio/sms`

It's really important to use **https** here, as the server only uses Basic Authentication,
and will send the username/password unencrypted if you don't use https.

# Testing:
- Set up [Ngrok](https://ngrok.com/) to allow Twilio to communicate with a local build.
- Run/Debug the sms-bridge spring app
- Run `ngrok http <server.port>` and open the link that appears in the terminal.
- Copy the **http** url to the twilio Dashboard <br>
(otherwise the signature validation will fail due to Twilio hashing with https, 
but sms-bridge hashing with http)
