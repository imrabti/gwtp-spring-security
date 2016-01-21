GWTP Spring Security
====================

To run this project make sure that you have at least **JDK 7** and **Maven 3.0.5** installed and configured properly.
This project uses **Spring Boot** so it is pretty easy to build and run, the first step is to build the project : 
```bash
mvn clean install
```
The next step is to use the Spring Boot maven plugin to run the project :
```bash
mvn spring-boot:run
```
When the project is build successfully and is running, application is located at the following URL : **http://localhost:8080/index.html**

Because it is only a demo projet to show how to integrate **GWT/GWTP** with **Spring Security** using REST services, Spring Security is configured to accept only one test user with the login **root** and the password **password**.

For details about the current project, you check the linked article in my blog here : https://crazygui.wordpress.com/2014/08/29/secure-rest-services-using-spring-security/
