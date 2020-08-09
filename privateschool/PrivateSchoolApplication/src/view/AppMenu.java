package view;

import static dao.SchedulePerCourseDao.viewScheduleByStudent;
import static dao.StudentPerCoursePerAssignmentDao.markStudentPerCoursePerAssignment;
import static dao.StudentPerCoursePerAssignmentDao.updateStudentPerCoursePerAssignment;
import static dao.StudentPerCoursePerAssignmentDao.viewStudentPerCoursePerAssignmentViews;
import static dao.TrainersPerCourseDao.viewAllAssigmnetsPerStudentPerCourse2;
import static dao.TrainersPerCourseDao.viewAllCoursesPerTrainer;
import static dao.TrainersPerCourseDao.viewAllStudentsPerCoursePerTrainer;

import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import model.User;

import static view.HeadmasterCRUD.headmasterCRUDMain;
import static view.Login.loginUser;

public class AppMenu {

    public static void selectMenu() throws NoSuchAlgorithmException {
        User user = loginUser();
        String role = user.getRole();
        switch (role) {
            case "student":
                studentMenu(user);
                break;
            case "srainer":
                trainerMenu(user);
                break;
            case "headmaster":
                headmasterCRUDMain(user);
                break;
            default:
                System.out.println("Not valid option!");
                break;
        }
    }

    public static void studentMenu(User student) {
        boolean exit = false;
        do {
            Scanner sc = new Scanner(System.in);
            System.out.println("Student See his / her daily Schedule per Course ");
            System.out.println("Student See the dates of submission of the Assignments per Course");
            System.out.println("Student Submit any Assignments");
            System.out.println("To see your daily schedule press: 1");
            System.out.println("To see dates of submission press: 2");
            System.out.println("To submit a assignment press: 3");
            System.out.println("To exit press: exit");
            System.out.print("Give your selection: ");
            String a = sc.next();
            switch (a) {
                case "1":
                    viewScheduleByStudent(student);
                    break;
                case "2":
                    viewStudentPerCoursePerAssignmentViews(student);
                    break;
                case "3":
                    updateStudentPerCoursePerAssignment(student);
                    break;
                case "exit":
                    exit = true;
                    break;
                default:
                    System.out.println("Not valid option!");
                    break;
            }
        } while (exit == false);
    }

    public static void trainerMenu(User trainer) {

        boolean exit = false;
        do {
            Scanner sc = new Scanner(System.in);
            System.out.println("Trainer View all the Courses he / she is enrolled");
            System.out.println("Trainer View all the Students per Course");
            System.out.println("Trainer View all the Assignments per Student per Course");
            System.out.println("Trainer Mark all the Assignments per Student per Course");
            System.out.println("To see your courses press: 1");
            System.out.println("To see students per course press: 2");
            System.out.println("To assigmnment per student press: 3");
            System.out.println("To mark an assigmnment press: 4");
            System.out.println("To exit press: x");
            System.out.print("Give your selection: ");
            String a = sc.next();
            switch (a) {

                case "1":
                    viewAllCoursesPerTrainer(trainer);
                    break;
                case "2":
                    viewAllStudentsPerCoursePerTrainer(trainer);
                    break;
                case "3":
                    viewAllAssigmnetsPerStudentPerCourse2(trainer);
                    break;
                case "4":
                    markStudentPerCoursePerAssignment(trainer);
                    break;
                case "exit":
                    exit = true;
                    break;
                default:
                    System.out.println("Not valid option!");
                    break;
            }
        } while (exit == false);
    }

}
