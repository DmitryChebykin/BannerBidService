BannerBidService

This application is based on Spring Boot framework. 

The application provides an REST API for creating ad banners and categories.

The client web application allows you to view banners, create banners for existing categories, and edit banners.


Requirements

JRE: 8 or higher , MAVEN (https://maven.apache.org/), MySQL (https://dev.mysql.com/downloads/mysql/)

How to install:

download the source code of the project, in the root folder run the command - "mvn package"

in folder target you can find compiled program BannerBidService-0.0.1-SNAPSHOT.jar

put this file where you want, along with this file in the same folder, 

you must place the configuration file "mysql.properties" for connecting to the  database

Sample parameter filling:

db.address:localhost
db.port:3306
db.name:banners_app
db.user:root  
db.password:password


Usage

To run the program execute command: java -jar BannerBidService-0.0.1-SNAPSHOT.jar

To run the program in demo mode, run the program with the --init key


java -jar BannerBidService-0.0.1-SNAPSHOT.jar --init

The database will be populated with the data set.

Before starting in demo mode, make sure that there are no data tables in the existing database


The program runs on port 8084

Swagger UI - /swagger-ui.html