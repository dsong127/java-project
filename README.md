# Projects for Advanced Java Programming class at PSU

Java server / client program with RESTFUL web service and an Android app for creating/deleting/modifying
airline data containing information such as flight number, flight time, searching for flights, etc. Used TDD
approach, and thoroughly implemented unit and integration tests.  

Features text file parsing/dumping, XML file parsing/dumping, pretty printing, and Java servlet / client classes, all implemented from scratch.


## Usage:

1. Web service
* `chmod +x mvnw`
* `mvnw verify` to build and test
* cd into main src directory (java-project/airline-web/src/main/java/edu/pdx/cs410J/dsong/)
  then run `java edu.pdx.cs410J.dsong.Project4 [options] <args>`
* Run `java edu.pdx.cs410J.dsong.Project4 -README` to see available options and args
2. Android app
* Install Android Studio, and create a new virtual device (Pixel 2 API 30) in AVD Manager.
* `gradlew build`
* In terminal, `$ANDROID_HOME/emulator/emulator @Pixel_2_API_29` to start emulator
* In the main src directory, run `gradlew installDebug` to deploy the app to the emulator
