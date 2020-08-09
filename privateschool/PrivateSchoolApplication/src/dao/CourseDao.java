package dao;

import static dao.AssignmentsPerCourseDao.insertNewAssignmentPerCourse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Course;

import static utils.DBUtils.closeConnection;
import static utils.DBUtils.createPreparedStatement;
import static utils.DBUtils.getconnection;
import static utils.DBUtils.queryExecute;
import static utils.DBUtils.queryExecuteUpdate;
import static utils.GenericUtils.giveInputFromUser;
import static utils.GenericUtils.giveIntFromUser;
import static utils.GenericUtils.validChoice;

public class CourseDao {

    public static void setCourseParams(PreparedStatement pst, Course Course) {
        try {
            pst.setString(1, Course.getName());
            pst.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public static List<Course> getCourseList(ResultSet rs) {
        List<Course> Courses = new ArrayList();
        try {
            while (rs.next()) {
                Course Course = new Course();
                Course.setId(rs.getInt("id"));
                Course.setName(rs.getString("name"));
                Courses.add(Course);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return Courses;
    }

    public static Course getCourse(ResultSet rs) {
        Course Course = new Course();
        try {
            while (rs.next()) {
                Course.setId(rs.getInt("id"));
                Course.setName(rs.getString("name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Course;
    }

    public static Course courseSelector() {
        boolean check = false;
        Course course = new Course();
        CourseDao cd = new CourseDao();
        do {
            viewAllCourses("Select Course: ");
            int id = giveIntFromUser("Select a Course by ID: ");
            course = cd.getCourseById(id);
            if (course.getName() == null) {
                System.out.println("Give valid option: ");
            } else {
                check = true;
            }
        } while (check == false);
        return course;
    }

    public static int courseSelectorId() {
        Course course = courseSelector();
        return course.getId();
    }

    public void insertCourse(Course Course) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        String sql = "INSERT INTO Courses(name) VALUES(?)";
        PreparedStatement pst = createPreparedStatement(sql, conn);
        setCourseParams(pst, Course);
        closeConnection(conn);
    }

    public List<Course> getAllCourses() {
        List<Course> allCourses = new ArrayList();
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("SELECT*FROM Courses", conn);
        ResultSet rs = queryExecute(pst);
        allCourses.addAll(getCourseList(rs));
        closeConnection(conn);
        return allCourses;
    }

    public Course getCourseById(int id) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("SELECT*FROM courses where id=" + id, conn);
        ResultSet rs = queryExecute(pst);
        Course course = getCourse(rs);
        closeConnection(conn);
        return course;
    }

    public Course getCourseByName(String Name) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("SELECT*FROM Courses where Name='" + Name + "'", conn);
        ResultSet rs = queryExecute(pst);
        Course Course = getCourse(rs);
        closeConnection(conn);
        return Course;
    }

    public void deleteCourse(int id) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("DELETE FROM Courses where id =" + id, conn);
        int ex = queryExecuteUpdate(pst);
        closeConnection(conn);
    }

    public void updateCourse(Course course) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        String sql = "UPDATE Courses SET name=? where id =" + course.getId();
        PreparedStatement pst = createPreparedStatement(sql, conn);
        setCourseParams(pst, course);
        closeConnection(conn);
    }

    public static void updateCourse() {
        CourseDao cd = new CourseDao();
        int id = courseSelector().getId();
        Course course = createCourse();
        course.setId(id);
        cd.updateCourse(course);

    }

    public static void viewAllCourses(String message) {
        List<Course> allCourses = new ArrayList();
        CourseDao cd = new CourseDao();
        allCourses = cd.getAllCourses();
        System.out.println(message);
        for (Course allCourse : allCourses) {
            System.out.println(allCourse);
        }
    }

    public static void viewRestCourses(List<Course> remainingCourses) {
        System.out.println("The Remaining Courses are: ");
        remainingCourses.forEach((remainingCourse) -> {
            System.out.println(remainingCourse);
        });
    }

    public static void deleteCourse() {
        CourseDao cd = new CourseDao();
        Course course = courseSelector();
        cd.deleteCourse(course.getId());
    }

    public static void insertNewCourse(Course course) {
        CourseDao cd = new CourseDao();
        cd.insertCourse(course);
    }

    public static Course createCourse() {
        Course Course = new Course(giveInputFromUser("Give Course name: "));
        return Course;
    }

    public static void createCourseAndAddAssigments() {
        CourseDao cd = new CourseDao();
        Course course = createCourse();
        insertNewCourse(course);
        Course coursereturn = cd.getCourseByName(course.getName());
        do {
            insertNewAssignmentPerCourse(coursereturn);
        } while (validChoice(giveInputFromUser("Give more Assignments? (y/n)")));
    }

}
