**Service for logging incoming messages.**

To run the application:

    Run the following commands from the project root dir
    
        1) mvn clean install
        2) mvn -pl server spring-boot:run

Then add desired Parser interface implementations (JSON, XML, CSV implementations are already present) to lib/parser directory to be able to process desired type messages.
To test message logging funtionality, make the following HTTP request:

    POST http://localhost:8080/message-service/audit

        Request body example:
        {
            "type": "Usual type",
            "date": "04/18/2016",
            "operation": "Insert"
        }
