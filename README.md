### HILEHASE

To test run the following command after cloning the project:  
source ./scripts/sourceme.sh  
This sets up the build environment. (JAVA and maven)
Then run maven with the following command:  
mvn test

On Windows there are some issue with testing. You have to remove code-generation part of the pom.xml

Signal registration and start of the simulation:
    Time is registered automatically with id 0
    All signal must be registered with the next id (1, 2 ...)
