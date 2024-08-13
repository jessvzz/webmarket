# Java_Example_Newspaper_DAO
> The Newspaper example, showing the full framework developed in the course

This example (also) shows how to implement a simple data model, and includes a model implementation for MySQL. 

## Usage

This is a *sample application* developed during the lectures of the  [**Web Engineering course**](https://webengineering-univaq.github.io). The code is organized to best match the lecture topics and examples. It is not intended for production use and is not optimized in any way. 

*This example code will be shown and described approximately during the 13th lecture of the course, so wait to download it, since it may get updated in the meanwhile.*

## Installation

This is a Maven-based project. Simply download the code and open it in any Maven-enabled IDE such as Netbeans or Eclipse. Additionally, you may need to *configure the deploy settings*: the application is intended to be run on the **JavaEE 7** platform inside **Apache Tomcat version 9**. Refer to your IDE help files to perform this step. For example, in Apache Netbeans, you must enter these settings in Project properties > Run.
Finally, this example uses a MySQL database. Therefore, you need a working instance of **MySQL version 8 or above**. 
**A SQL script to setup the required database is included in the project.**
Such database setup script must be run as root on the DBMS in order to create the database, populate it, and also create the application-specific 
user that connects to the database in the application code.
*The application assumes that the DBMS is accessible on localhost with the default port (3306) and the username/password configured by the
database setup script. Otherwise, update the database connection parameters in the META_INF/context.xml.*


---

![University of L'Aquila](https://www.disim.univaq.it/skins/aqua/img/logo2021-2.png)

 
