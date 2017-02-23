# charParser
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
