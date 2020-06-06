# pstn-bridge
Matrix PSTN Bridge

Bridges PSTN to matrix.


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
(Hopefully more automated in the future)
### Matrix:
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

### PSTN Bridge Database Setup:
Create a database called sms_bridge (with appropriate users/roles if required)
E.g., in postgres:
```
CREATE DATABASE sms_bridge;
CREATE USER youruser WITH ENCRYPTED PASSWORD 'yourpass';
GRANT ALL PRIVILEGES ON DATABASE yourdbname TO sms_bridge;
```

Register the phone number you want to send/receive messages for with your matrix user:
```
INSERT INTO contact(number) VALUES('+1234567890'); # Can add optional location
INSERT INTO chat_user(owner_id, user_type, contact) VALUES('@username:server.ca', 'USER', <id-of-above>);
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

### Example docker-compose:
```
matrix:
  // matrix config

sms-bridge:
  image: levimiller/sms-bridge:latest
  links:
    - database:db
    - matrix
  expose:
    - "8421"
  environment:
    VIRTUAL_HOST: sms.domain.com
    VIRTUAL_PORT: 8421
    BRIDGE_DB_USERNAME: sms_bridge
    BRIDGE_DB_PASSWORD: <password for user>
    BRIDGE_MATRIX_URL: https://matrix.hosted.url
    BRIDGE_MATRIX_AS_TOKEN: <application service token>
    BRIDGE_MATRIX_HS_TOKEN: <home server token>
    BRIDGE_TWILIO_SID: <twilio app sid>
    BRIDGE_TWILIO_TOKEN: <twilio app token>
    
database:
  image: postgres:9.6.2
  // postgres config
```

# Testing:
- Set up [Ngrok](https://ngrok.com/) to allow Twilio to communicate with a local build.
- Run/Debug the sms-bridge spring app
- Run `ngrok http <server.port>` and open the link that appears in the terminal.
- Copy the **http** url to the twilio Dashboard <br>
(otherwise the signature validation will fail due to Twilio hashing with https, 
but sms-bridge hashing with http)
