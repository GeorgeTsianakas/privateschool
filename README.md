# Private School — Console Application

A console-based school management application built with Java, JDBC, and MySQL. It models a “Private School” scenario with multiple user roles and provides a simple, menu-driven interface for managing courses, users, and assignments.

Badges: Java • JDBC • MySQL

## Overview
This project was developed as an individual assignment for the AFDEmp Coding Bootcamp. It demonstrates:
- Role-based access (Student, Trainer, Head Master)
- Persistent storage in MySQL via JDBC
- Secure password handling with hashing
- Menu-driven console UI

## Features
- Authentication and Authorization
  - Login with role-based permissions (Student, Trainer, Head Master)
  - Password hashing for secure credential storage
- Head Master
  - Create courses
  - Assign trainers and students to courses
  - Assign assignments to courses
- Trainer
  - View students per course
  - Review and mark assignments
- Student
  - Enroll in available courses
  - View and submit assignments

## Tech Stack
- Language: Java
- Data Access: JDBC
- Database: MySQL
- Build/Project files: Ant (build.xml), NetBeans project metadata

## Repository Structure (high-level)
- privateschool/PrivateSchoolApplication/src
  - dao/ … Data access (JDBC)
  - model/ … Domain models
  - dto/ … Data Transfer Objects
  - utils/ … Utilities (DB connection, password hashing, etc.)
  - view/ … Console menus and flows
  - privateschoolapplication/PrivateSchoolApplication.java … Main entry point
- privateschool/PrivateSchoolApplication/build.xml … Ant build file
- privateschool/PrivateSchoolApplication/dist/ … Packaged JAR and libs
- privateschool/privateschooldb.txt … Database schema/seed notes

## Requirements
- JDK 8 or later
- MySQL 8.x (or compatible)
- MySQL user with permissions to create database, tables, and perform CRUD

## Database Setup
1. Create a database (example: privateschool):
   - You can use the SQL statements and notes inside: privateschool/privateschooldb.txt
2. Create a user or use an existing MySQL account with appropriate privileges.
3. Note your MySQL host, port, database name, username, and password for application configuration.

## Configuration
- JDBC configuration is handled in code via utils/DBUtils.java.
  - Update the JDBC URL, username, and password to match your local MySQL setup.
  - Example format: jdbc:mysql://localhost:3306/privateschool?useSSL=false&serverTimezone=UTC
- The project includes a MySQL Connector/J JAR in dist/lib and privateschool/mysql-connector-java-8.0.15.jar.
  - If you change MySQL versions, ensure the connector matches your server version.

## Build and Run
You have multiple options depending on your IDE/workflow.

Option A — IDE (IntelliJ IDEA / NetBeans)
- Import the project from the root directory.
- Ensure the module uses the included MySQL connector JAR (or add it as a library/dependency).
- Set the main class to: privateschoolapplication.PrivateSchoolApplication
- Run the application from the IDE.

Option B — Ant
- Navigate to privateschool/PrivateSchoolApplication
- Use the provided build.xml to build and run tasks (from your IDE or Ant tooling).

Option C — Run Prebuilt JAR
- After building, a runnable JAR is available at:
  - privateschool/PrivateSchoolApplication/dist/PrivateSchoolApplication.jar
- Ensure the dist/lib folder (with MySQL connector) is present beside the JAR when running.
- Run from the command line, e.g.:
  - java -jar dist/PrivateSchoolApplication.jar

## Usage
1. Start the application.
2. Log in with your credentials.
   - If no users exist, create them via the Head Master role (you may need to insert an initial Head Master user directly in the database for first-time setup).
3. Use the menu to perform role-specific actions:
   - Head Master: create courses, assign trainers/students, assign assignments
   - Trainer: view students, mark assignments
   - Student: enroll and submit assignments

## Security
- Passwords are hashed before storage via utils/Password.
- Do not store plaintext passwords. Use secure, unique credentials in MySQL and within the application.

## Troubleshooting
- Cannot connect to DB:
  - Verify MySQL is running and reachable on the configured host/port.
  - Check JDBC URL, username, password in utils/DBUtils.java.
  - Ensure the MySQL Connector/J JAR is on the classpath.
- SQL errors on startup:
  - Confirm the database and tables exist (see privateschool/privateschooldb.txt).
  - Ensure your user has the required privileges.
- Character encoding/timezone warnings:
  - Add serverTimezone=UTC to the JDBC URL and check collation/charset settings.

## Notes
- This repository contains NetBeans/Ant project files alongside source code for convenience.
- If you prefer Maven/Gradle, you can migrate the project structure and manage the MySQL driver there.

## License
No explicit license has been provided. If you intend to use or distribute this code, please add an appropriate LICENSE file first.
