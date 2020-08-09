package view;

import dao.AssignmentDao;

import static dao.AssignmentDao.viewAllAssignments;

import dao.AssignmentsPerCourseDao;

import static dao.AssignmentsPerCourseDao.viewAllAssignmentsPerCourse;

import dao.CourseDao;
import dao.RoleDao;
import dao.SchedulePerCourseDao;

import static dao.SchedulePerCourseDao.deleteSchedule;
import static dao.SchedulePerCourseDao.insertNewSchedule;
import static dao.SchedulePerCourseDao.updateSchedule;
import static dao.SchedulePerCourseDao.viewAllSchedule;

import dao.StudentPerCoursePerAssignmentDao;
import dao.StudentsPerCourseDao;

import static dao.StudentsPerCourseDao.viewAllStudentsPerCourse;

import dao.TrainersPerCourseDao;

import static dao.TrainersPerCourseDao.viewAllTrainersPerCourse;

import dao.UserDao;

import static dao.UserDao.viewAllUserByRole;

import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.User;

public class HeadmasterCRUD {

    UserDao u = new UserDao();
    RoleDao r = new RoleDao();
    AssignmentDao a = new AssignmentDao();
    CourseDao course = new CourseDao();
    AssignmentsPerCourseDao apc = new AssignmentsPerCourseDao();
    TrainersPerCourseDao tpc = new TrainersPerCourseDao();
    StudentPerCoursePerAssignmentDao spcpa = new StudentPerCoursePerAssignmentDao();
    SchedulePerCourseDao schedule = new SchedulePerCourseDao();
    StudentsPerCourseDao stc = new StudentsPerCourseDao();

    public static void headmasterCRUDMain(User headmaster) throws NoSuchAlgorithmException {
        boolean exit = false;
        do {
            Scanner sc = new Scanner(System.in);
            System.out.println("Headmaster has the following options :");
            System.out.println("1.CRUD on Courses");
            System.out.println("2.CRUD on Students");
            System.out.println("3.CRUD on Assignments");
            System.out.println("4.CRUD on Trainers");
            System.out.println("5.CRUD on Students per Courses");
            System.out.println("6.CRUD on Trainers per Courses");
            System.out.println("7.CRUD on Assignments per Courses");
            System.out.println("8.CRUD on Schedule per Courses");
            System.out.println("For courses press: 1");
            System.out.println("For students press: 2");
            System.out.println("For assigments press: 3");
            System.out.println("For trainers press: 4");
            System.out.println("For students per course press 5: ");
            System.out.println("For trainers per course press 6: ");
            System.out.println("For assignments per course press 7: ");
            System.out.println("For schedule press 8: ");
            String a = sc.next();
            switch (a) {
                case "1":
                    coursesMenu(headmaster);
                    break;
                case "2":
                    studentsMenu(headmaster);
                    break;
                case "3":
                    assignmentMenu(headmaster);
                    break;
                case "4":
                    trainersMenu(headmaster);
                    break;
                case "5":
                    studentsPerCourseMenu(headmaster);
                    break;
                case "6":
                    trainersPerCourseMenu(headmaster);
                    break;
                case "7":
                    assigmentsPerCourseMenu(headmaster);
                    break;
                case "8":
                    scheduleMenu(headmaster);
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

    public static void coursesMenu(User headmaster) {
        boolean exit = false;
        do {
            Scanner sc = new Scanner(System.in);
            System.out.println("To create course press: 1");
            System.out.println("To update course press: 2");
            System.out.println("To delete course press: 3");
            System.out.println("To view all courses press: 4");
            System.out.println("To return to Main Menu press: back");
            System.out.print("Select: ");
            String a = sc.next();
            switch (a) {

                case "1":
                    CourseDao.createCourse();
                    break;
                case "2":
                    CourseDao.updateCourse();
                    break;
                case "3":
                    CourseDao.deleteCourse();
                    break;
                case "4":
                    CourseDao.viewAllCourses("Courses are: ");
                    break;
                case "back": {
                    try {
                        headmasterCRUDMain(headmaster);
                    } catch (NoSuchAlgorithmException ex) {
                        Logger.getLogger(HeadmasterCRUD.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                exit = true;
                break;
                default:
                    System.out.println("Not valid option!");
                    break;
            }
        } while (exit == false);
    }

    public static void assignmentMenu(User headmaster) {
        boolean exit = false;
        do {
            Scanner sc = new Scanner(System.in);
            System.out.println("To create assignment press: 1");
            System.out.println("To update assignment press: 2");
            System.out.println("To delete assignment press: 3");
            System.out.println("To view all assignments press: 4");
            System.out.println("To return to Main Menu press: back");
            System.out.print("Select: ");
            String a = sc.next();
            switch (a) {
                case "1":
                    AssignmentDao.insertNewAssignment();
                    break;
                case "2":
                    AssignmentDao.updateAssignment();
                    break;
                case "3":
                    AssignmentDao.deleteAssignment();
                    break;
                case "4":
                    viewAllAssignments("The Assignments are: ");
                    break;
                case "back": {
                    try {
                        headmasterCRUDMain(headmaster);
                    } catch (NoSuchAlgorithmException ex) {
                        Logger.getLogger(HeadmasterCRUD.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                exit = true;
                break;
                default:
                    System.out.println("Not valid option!");
                    break;
            }
        } while (exit == false);
    }

    public static void studentsMenu(User headmaster) throws NoSuchAlgorithmException {
        boolean exit = false;
        do {
            Scanner sc = new Scanner(System.in);
            System.out.println("To create student press: 1");
            System.out.println("To update student press: 2");
            System.out.println("To delete student press: 3");
            System.out.println("To view all students press: 4");
            System.out.println("To return to Main Menu press: back");
            System.out.print("Select: ");
            String a = sc.next();
            switch (a) {

                case "1":
                    UserDao.createStudent();
                    break;
                case "2":
                    UserDao.updateUser();
                    break;
                case "3":
                    UserDao.deleteUser();
                    break;
                case "4":
                    viewAllUserByRole("student");
                    break;
                case "back":
                    headmasterCRUDMain(headmaster);
                    exit = true;
                    break;
                default:
                    System.out.println("Not valid option!");
                    break;
            }
        } while (exit == false);
    }

    public static void trainersMenu(User headmaster) {
        boolean exit = false;
        do {
            Scanner sc = new Scanner(System.in);
            System.out.println("To create trainer press: 1");
            System.out.println("To update trainer press: 2");
            System.out.println("To delete trainer press: 3");
            System.out.println("To view all trainers press: 4");
            System.out.println("To return to Main Menu press: back");
            System.out.print("Select: ");
            String a = sc.next();
            switch (a) {

                case "1": {
                    try {
                        UserDao.createTrainer();
                    } catch (NoSuchAlgorithmException ex) {
                        Logger.getLogger(HeadmasterCRUD.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "2": {
                    try {
                        UserDao.updateUser();
                    } catch (NoSuchAlgorithmException ex) {
                        Logger.getLogger(HeadmasterCRUD.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case "3":
                    UserDao.deleteUser();
                    break;
                case "4":
                    viewAllUserByRole("trainer");
                    break;
                case "back": {
                    try {
                        headmasterCRUDMain(headmaster);
                    } catch (NoSuchAlgorithmException ex) {
                        Logger.getLogger(HeadmasterCRUD.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                exit = true;
                break;
                default:
                    System.out.println("Not valid option!");
                    break;
            }
        } while (exit == false);
    }

    public static void assigmentsPerCourseMenu(User headmaster) {
        boolean exit = false;
        do {
            Scanner sc = new Scanner(System.in);
            System.out.println("To create assigments per course press: 1");
            System.out.println("To update assigments per course press: 2");
            System.out.println("To delete assigments per course press: 3");
            System.out.println("To view all assigments per course press: 4");
            System.out.println("To return to Main Menu press: back");
            System.out.print("Select: ");
            String a = sc.next();
            switch (a) {

                case "1":
                    AssignmentsPerCourseDao.createAssignmentsPerCourse();
                    break;
                case "2":
                    AssignmentsPerCourseDao.updateAssignmentsPerCourse();
                    break;
                case "3":
                    AssignmentsPerCourseDao.deleteAssigmnentPerCourse();
                    break;
                case "4":
                    viewAllAssignmentsPerCourse();
                    break;
                case "back": {
                    try {
                        headmasterCRUDMain(headmaster);
                    } catch (NoSuchAlgorithmException ex) {
                        Logger.getLogger(HeadmasterCRUD.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                exit = true;
                break;
                default:
                    System.out.println("Not valid option!");
                    break;
            }
        } while (exit == false);
    }

    public static void studentsPerCourseMenu(User headmaster) {
        boolean exit = false;
        do {
            Scanner sc = new Scanner(System.in);
            System.out.println("To create student per course press: 1");
            System.out.println("To update student per course press: 2");
            System.out.println("To delete student per course press: 3");
            System.out.println("To view all students per course press: 4");
            System.out.println("To return to Main Menu press: back");
            System.out.print("Select:  ");
            String a = sc.next();
            switch (a) {

                case "1":
                    StudentsPerCourseDao.createStudentsPerCourse();
                    break;
                case "2":
                    StudentsPerCourseDao.updateStudentsPerCourse();
                    break;
                case "3":
                    StudentsPerCourseDao.deleteStudentsPerCourse();
                    break;
                case "4":
                    viewAllStudentsPerCourse();
                    break;
                case "back": {
                    try {
                        headmasterCRUDMain(headmaster);
                    } catch (NoSuchAlgorithmException ex) {
                        Logger.getLogger(HeadmasterCRUD.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                exit = true;
                break;
                default:
                    System.out.println("Not valid option!");
                    break;
            }
        } while (exit == false);
    }

    public static void trainersPerCourseMenu(User headmaster) {
        boolean exit = false;
        do {
            Scanner sc = new Scanner(System.in);
            System.out.println("To create trainers per course press: 1");
            System.out.println("To update trainers per course press: 2");
            System.out.println("To delete trainers per course press: 3");
            System.out.println("To view all trainers per course press: 4");
            System.out.println("To return to Main Menu press: back");
            System.out.print("Select:  ");
            String a = sc.next();
            switch (a) {

                case "1":
                    TrainersPerCourseDao.createTrainerPerCourse();
                    break;
                case "2":
                    TrainersPerCourseDao.updateTrainersPerCourse();
                    break;
                case "3":
                    TrainersPerCourseDao.deleteTrainerPerCourse();
                    break;
                case "4":
                    viewAllTrainersPerCourse();
                    break;
                case "back": {
                    try {
                        headmasterCRUDMain(headmaster);
                    } catch (NoSuchAlgorithmException ex) {
                        Logger.getLogger(HeadmasterCRUD.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                exit = true;
                break;
                default:
                    System.out.println("Not valid option!");
                    break;
            }
        } while (exit == false);
    }

    public static void scheduleMenu(User headmaster) {
        boolean exit = false;
        do {
            Scanner sc = new Scanner(System.in);
            System.out.println("To create schedule press: 1");
            System.out.println("To update schedule press: 2");
            System.out.println("To delete schedule press: 3");
            System.out.println("To print all schedule press: 4");
            System.out.println("To return to Main Menu press: back");
            System.out.print("Select: ");
            String a = sc.next();
            switch (a) {

                case "1":
                    insertNewSchedule();
                    break;
                case "2":
                    updateSchedule();
                    break;
                case "3":
                    deleteSchedule();
                    break;
                case "4":
                    viewAllSchedule("The schedule is: ");
                    break;
                case "back": {
                    try {
                        headmasterCRUDMain(headmaster);
                    } catch (NoSuchAlgorithmException ex) {
                        Logger.getLogger(HeadmasterCRUD.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                exit = true;
                break;
                default:
                    System.out.println("Not valid option!");
                    break;
            }
        } while (exit == false);
    }

}
