# Database Project for CSI-345

## Requirements

1. Java 8
2. [MySql](https://www.mysql.com/downloads/)
2. [NPM and Nodejs](https://nodejs.org/en/)

## Setup MySql for MacOS/Linux

1. Launch interative terminal
  
  * Follow inital setup instructions for Node and MySQL

2. Create a database

   * `create database regal;`

3. Exit terminal 

   * `Ctrl+d`

4. Clone or download project.

   * From moodle submission
   * [Github](https://github.com/Aaron-G-9/cse-345)
   * [Github](https://github.com/Aaron-G-9/regal-bank-frontend)


## Run project

* On macOS/Linux 
  * In cse345 folder: `./gradlew clean bootRun`
  * In regal-front-end folder: `npm start`

* On windows
  * In cse345 folder: `./gradlew.bat clean bootRun`
  * In regal-front-end folder: `npm start`
  * Go to http://localhost:8080
    * (API runs on 8090)

## **NOTE**

* Once you have ran the project once do the following:
  * open `src/main/resources/application.properties`
  * Change `spring.datasource.initialize=true` to
    `spring.datasource.initialize=false`
* This will prevent the build process from attempting to create the schema and populate the database.

## Tested on

* Macbook, macOS High Sierra
* RazerBlade, Windows 10
* Dell Optiplex, Arch Linux

## Questions?
* Email amgoodfellow@oakland.edu

## Project Structure (Required)  


