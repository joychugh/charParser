[![Build Status](https://travis-ci.org/joychugh/messageParser.svg?branch=master)](https://travis-ci.org/joychugh/messageParser)

# messageParser
A simple app to parse chat message in a popular app and extract info

Instructions:
* Download and install Java 8
* Download and install Maven 3.3.9
* [Maven](http://maven.apache.org)
* go to root directory of the project and run ```mvn clean compile package```
* Once the compile package phase is complete
* Navigate to target directory created in the root directory, there you will see codescreen.properties
* Set the port to use for the application, default 8090
* run the application by ```java -jar target/atlassian-codescreen-1.0-jar-with-dependencies.jar```
* log file is created under folder where you run the application from ```log.out``` it contains the logs for the application.
* Usage:
* * Request: ```$ curl -H "Content-Type: application/json" -X POST -d '{"message": "this is @sample (emoticon) https://twitter.com/NASA/status/834575390257643521"}' http://localhost:8090/api/v1/parse```
* * Response ```{"emoticons":["emoticon"],"mentions":["sample"],"links":[{"url":"https://twitter.com/NASA/status/834575390257643521","title":"“Notice the new @GoogleDoodles? It\u0027s about the 7 Earth-sized planets we discovered around nearby star! Get the news: https://t.co/G9tW3cJMnV https://t.co/dOHB0bLqXn”"}]}```

Improvements:
* Classes made more Dependency Injection Friendly to get better Unit tests (right now we make real calls to the web to get title of the page for tests)
* SSL Support to be added
* Reduce Spark dependency on request handler. Right now we supply the Spark request object along with the POJO to the handler, in future extract all request information that might be useful into an internal POJO and send that along so its easy to switch frameworks.
* Add more unit tests for edge cases
* Configure the JSOUP to rely on shorter timeouts to load websites to parse
* Add more debug logging to help debug application error
* Add health checks
* Add metrics

