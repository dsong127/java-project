# Projects for Advanced Java Programming class at PSU

Java server / client program with RESTFUL web service and an Android app for creating/deleting/modifying
airline data containing information such as flight number, flight time, searching for flights, etc. Used TDD
approach, and thoroughly implemented unit and integration tests.  

Features text file parsing/dumping, XML file parsing/dumping, pretty printing, and Java servlet / client classes, all implemented from scratch.


## Usage:

1. Web service 
* cd into `airline-web`
* `chmod +x mvnw`
* `./mvnw verify` to build and test
* `mvn jetty:run` to start server
* In a new terminal (Currently no web UI support) `java -jar target/airline.jar -README` to start client and view usage
2. Android app
* Install Android Studio, and create a new virtual device (Pixel 2 API 30) in AVD Manager.
* Use the green 'Run' button in Android Studio to start the app
* Or using command lines:
* `./gradlew build`
* In terminal, `$ANDROID_HOME/emulator/emulator @Pixel_2_API_29` to start emulator
* In the main src directory, run `gradlew installDebug` to deploy the app to the emulator


## Android app screen shots

[](imgs/1.png)
[](imgs/2.png)
[](imgs/3.png)
[](imgs/4.png)
[](imgs/5.png)