# Reviook

Reviook is a Java application that implements a social network for books. Users can register as administrators, book authors, or regular users. Once logged in, users can search for books and read/leave reviews on a book. Additionally, they can search for other users and start following them.

## Technologies Used

The project has been implemented using the following technologies:

* Maven
* Neo4j
* MongoDB
* JavaFX

Most of the information is stored in MongoDB, while Neo4j is used to implement the social part of the application that provides book recommendations based on followed users and read books. Furthermore, a local cache is implemented to speed up searches.

## How to Use the Application

To use the application, follow these steps:

* Clone the repository from GitHub
* Open the project in an IDE (IntelliJ IDEA recommended)
* Import the Maven dependencies
* Configure the MongoDB and Neo4J servers
* Run the application

## Features

Reviook offers the following features:

* Registration as administrators, book authors, or regular users
* Login
* Book search
* Reading and writing book reviews
* Searching for other users and following them
* Book recommendations based on followed users and read books

## Screenshot

![Screenshot of Reviook](https://github.com/mgiorgi13/Reviook/blob/main/Screenshot%20of%20application.png)
