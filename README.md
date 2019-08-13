Private School Individual Project Console Application
for AFDEmp Coding Bootcamp
## Desciption
1. Design your system to support different user roles, e.g. Student, Trainer, Head Master. The user must be able to login to the system with these different roles
2. Each role should be able to do specific actions as these are designated by the Head Master
	a. Student, enroll to any Course under any Trainer and submit any Assignments that are designated to him / her
	b. Trainer, view the list of the Students and mark any assignments
	c. Head Master, create any courses needed, appoint the trainer(s) and the students under each of the courses, appoint assignments to each of the courses
3. The login system must implement persistence and security along with password hashing
Specifically, each role must be able to do the following:
	a. Student
		i. See his / her daily Schedule per Course
		ii. See the dates of submission of the Assignments per Course
		iii. Submit any Assignments
	b. Trainer
		i. View all the Courses he / she is enrolled
		ii. View all the Students per Course
		iii. View all the Assignments per Student per Course
		iv. Mark all the Assignments per Student per Course
	c. Head Master
		i. CRUD on Courses
		ii. CRUD on Students
		iii. CRUD on Assignments
		iv. CRUD on Trainers
		v. CRUD on Students per Courses
		vi. CRUD on Trainers per Courses
		vii. CRUD on Assignments per Courses
		viii. CRUD on Schedule per Courses
## Technologies used
* JDBC
* MySQL
