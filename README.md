# Notes REST API

## Description
This project is a REST API for managing notes, implemented using Spring Boot. It supports creating, retrieving, updating, and deleting notes. The API can either use an in-memory database or an H2 database that runs in memory.

## Features
- CRUD Operations:
    - `GET /notes`: Fetch all notes.
    - `GET /notes/{id}`: Fetch a note by its ID.
    - `POST /notes`: Create a new note.
    - `PUT /notes/{id}`: Update an existing note.
    - `DELETE /notes/{id}`: Delete a note by ID.

## Requirements
- Java 11 or newer
- Maven

## Setup and Installation
```bash
mvn clean install
```
## Running application
```bash
mvn spring-boot:run
```
## After starting the application, you can access the API at:
http://localhost:8080/

To interact with the API, you can use tools like Postman or cURL. Here are examples of how to use cURL to interact with the API:

## Create a Note:
curl -X POST http://localhost:8080/api/v1/notes -H 'Content-Type: application/json' -d '{"title":"Sample Note", "content":"This is a sample note"}'

## Get All Notes:
curl http://localhost:8080/api/v1/notes

## Update a Note:
curl -X PUT http://localhost:8080/api/v1/notes/1 -H 'Content-Type: application/json' -d '{"title":"Updated Title", "content":"Updated content"}'

## Delete a Note:
curl -X DELETE http://localhost:8080/api/v1/notes/1

