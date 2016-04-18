Service for logging incoming messages.

To run the application:
    Run the following commands from project root dir
        1)  `mvn clean install` from project root dir
        2) Run `mvn -pl server spring-boot:run`
        
To test message logging funtionality, make the following HTTP request:

`POST http://localhost:8080/message-service/audit`
    Request body example:
        `{
              "type": "Usual type",
              "date": "04/18/2016",
              "operation": "Insert"
        }`
