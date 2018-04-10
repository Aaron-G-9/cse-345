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

#### Backend:  
.
├── build.gradle
├── dump.rdb
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── LICENSE
├── README.md
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── databases
    │   │           └── project
    │   │               ├── controllers
    │   │               │   ├── Api.java
    │   │               │   └── HomeController.java
    │   │               ├── models
    │   │               │   ├── Account.java
    │   │               │   ├── AccountType.java
    │   │               │   ├── CreditCard.java
    │   │               │   ├── CreditHistory.java
    │   │               │   ├── Customer.java
    │   │               │   ├── Employee.java
    │   │               │   ├── Gender.java
    │   │               │   ├── Loan.java
    │   │               │   ├── LoginForm.java
    │   │               │   ├── Timesheet.java
    │   │               │   └── Transaction.java
    │   │               ├── ProjectApplication.java
    │   │               └── services
    │   │                   ├── Authentication.java
    │   │                   ├── BankService.java
    │   │                   ├── IAuthentication.java
    │   │                   ├── IBankService.java
    │   │                   └── ScheduledTasks.java
    │   └── resources
    │       ├── application.properties
    │       ├── drop.sql
    │       ├── schema.sql
    │       ├── static
    │       └── templates
    │           └── index.html

#### Frontend:  
.
├── build
│   ├── build.js
│   ├── check-versions.js
│   ├── logo.png
│   ├── utils.js
│   ├── vue-loader.conf.js
│   ├── webpack.base.conf.js
│   ├── webpack.dev.conf.js
│   └── webpack.prod.conf.js
├── config
│   ├── dev.env.js
│   ├── index.js
│   └── prod.env.js
├── index.html
├── package.json
├── package-lock.json
├── src
│   ├── App.vue
│   ├── assets
│   │   ├── backpack-dude.jpg
│   │   ├── coffe-table.jpeg
│   │   ├── edc.jpg
│   │   ├── logo.png
│   │   ├── security
│   │   │   ├── computer.jpg
│   │   │   ├── france.jpeg
│   │   │   ├── moon.jpg
│   │   │   └── mountain.jpg
│   │   └── tree-girl.jpg
│   ├── components
│   │   ├── About.vue
│   │   ├── AccountSummary.vue
│   │   ├── AccountTypes.vue
│   │   ├── AddAccount.vue
│   │   ├── AddTransaction.vue
│   │   ├── Admin.vue
│   │   ├── ApplyForAccount.vue
│   │   ├── CreditCards.vue
│   │   ├── HelloWorld.vue
│   │   ├── Help.vue
│   │   ├── Join.vue
│   │   ├── Login.vue
│   │   ├── Navbar.vue
│   │   ├── OnlineBanking.vue
│   │   └── TransactionHistory.vue
│   ├── main.js
│   └── router
│       └── index.js
└── static

## License  
 
MIT License

Copyright (c) 2018 Aaron Goodfellow Conner Lee

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
