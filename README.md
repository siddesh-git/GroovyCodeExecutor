# GroovyCodeExecutor

Is a E2E Spring boot microservice solution which can be run on either local or on any cloud. 

It is a Java service which can run Groovy code successfully. Currently it is supported for single Groovy file. But, can be extended for an entire Groovy project submitted as zip file. It used Springboot thymeleaf for UI interface.

*Sample Groovy file is under uploads folder which can be used for testing*


Clone the repo

Two ways to run the program

1.Run below commands

mvn clean install

java -jar target/GroovyCodeExecutor-0.0.1-SNAPSHOT.jar 



2. Run below command
   mvn spring-boot:run


Launch any browser with below url

http://localhost:8080/index

Only *.groovy file uploads are supported

Sample Groovy file is under uploads folder which can be used for testing

There are three api's available

1. To upload .groovy script. Apis response contains jobId which can be used to check status and download logs

2. To check the status of uploaded groovy file using jobId

3. Using jobId from previous step logs can be downloaded
