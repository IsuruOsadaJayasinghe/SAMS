Student Attendance Management System (SAMS)

1\. Project Overview



The Student Attendance Management System (SAMS) is a Java-based desktop application designed to manage student information, courses, lecturers, class schedules, attendance records, and attendance reports.



The application supports role-based access for administrators and lecturers.



2\. Technologies Used

Java

JavaFX

Apache Maven

MySQL

JDBC

Git and GitHub

NetBeans IDE

3\. System Architecture



The application follows a layered architecture:



Presentation Layer: JavaFX user interface

Service Layer: Application business logic

Data Access Layer: DAO classes for database operations

Data Layer: MySQL database

4\. Features

User login and role-based access control

Course management

Student management

Lecturer management

Subject management and lecturer assignments

Class scheduling

Attendance marking and updating

Attendance reports

5\. Setup Instructions

Install the Java Development Kit (JDK).

Install NetBeans IDE.

Install MySQL Server and start the database service.

Create the required database using the supplied SQL script.

Open the project in NetBeans as a Maven project.

Update the database connection settings in the DBConnection class with your MySQL username, password, and database name.

Allow Maven to download the required dependencies.

Clean and build the project.

Run the application from NetBeans.

6\. Database



Database: sams\_db



The database SQL script will contain the table definitions and sample records required to set up the application.



7\. Login Credentials



Use the administrator and lecturer accounts configured in the sample database.



Admin 
username - admin
password - Admin123!

Lecturer
username - lecturer1
password - Lecturer123!


8\. Screenshots



Screenshots of the running application will be added here.



9\. Repository



GitHub: https://github.com/IsuruOsadaJayasinghe/SAMS

