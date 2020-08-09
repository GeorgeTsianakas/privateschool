package dao;

import static dao.CourseDao.courseSelector;
import static dao.CourseDao.getCourseList;
import static dao.CourseDao.viewRestCourses;
import static dao.UserDao.getUserList;
import static dao.UserDao.userIdSelector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Assignment;
import model.Course;
import model.StudentPerCoursePerAssignment;
import model.StudentsPerCourse;
import model.User;

import static utils.DBUtils.closeConnection;
import static utils.DBUtils.createPreparedStatement;
import static utils.DBUtils.getconnection;
import static utils.DBUtils.queryExecute;
import static utils.DBUtils.queryExecuteUpdate;
import static utils.GenericUtils.giveIntFromUser;

public class StudentsPerCourseDao {

    public static Set<Integer> getRestCoursesIdsSet(List<Course> remainingCourses) {
        Set<Integer> remainingCoursesIds = new HashSet();
        remainingCourses.forEach((course) -> {
            remainingCoursesIds.add(course.getId());
        });
        return remainingCoursesIds;
    }

    public static void setStudentsPerCourseParams(PreparedStatement pst, StudentsPerCourse stpc) {
        try {
            pst.setInt(1, stpc.getStid());
            pst.setInt(2, stpc.getCrid());
            pst.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public static List<StudentsPerCourse> getStudentsPerCourseList(ResultSet rs) {
        List<StudentsPerCourse> studentsPerCourses = new ArrayList();
        try {
            while (rs.next()) {
                StudentsPerCourse studentsPerCourse = new StudentsPerCourse();
                studentsPerCourse.setCrid(rs.getInt("crid"));
                studentsPerCourse.setStid(rs.getInt("stid"));
                studentsPerCourses.add(studentsPerCourse);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return studentsPerCourses;
    }

    public static StudentsPerCourse getStudentsPerCourse(ResultSet rs) {
        StudentsPerCourse studentsPerCourse = new StudentsPerCourse();
        try {
            while (rs.next()) {
                studentsPerCourse.setCrid(rs.getInt("crid"));
                studentsPerCourse.setStid(rs.getInt("stid"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TrainersPerCourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return studentsPerCourse;
    }

    public static Set<Integer> getRestStudentsIdsSet(List<User> remainingStudents) {
        Set<Integer> remainingStudentsIds = new HashSet();
        remainingStudents.forEach((student) -> {
            remainingStudentsIds.add(student.getId());
        });
        return remainingStudentsIds;
    }

    public static Course restCoursesSelectorPerStudent(User student) {
        boolean check = false;
        int crid = 0;
        Course course = new Course();
        StudentsPerCourseDao stpcd = new StudentsPerCourseDao();
        CourseDao cd = new CourseDao();
        List<Course> remainingCourses = stpcd.restCourses(student);
        Set<Integer> remainingCoursesIds = new HashSet();
        remainingCoursesIds.addAll(getRestCoursesIdsSet(remainingCourses));
        if (remainingCourses.size() >= 1) {
            do {
                System.out.println("The available Courses are: ");
                viewRestCourses(remainingCourses);
                crid = giveIntFromUser("Select a Course by ID: ");
                if (remainingCoursesIds.contains(crid)) {
                    check = true;
                } else {
                    System.out.println("Give valid option!");
                }
            } while (check == false);
        } else {
            System.out.println("No more Courses are available for this Student");
        }
        course = cd.getCourseById(crid);
        return course;
    }

    public static User studentSelectorPerCourse(Course course) {
        boolean check = false;
        int strid = 0;
        User trainer = new User();
        UserDao ud = new UserDao();
        List<User> trainers = viewStudentsPerCourse(course);
        do {
            int selection = giveIntFromUser("Select a Student by Number: ");
            if (selection >= 1 && selection <= trainers.size()) {
                strid = trainers.get(selection - 1).getId();
                check = true;
            } else {
                System.out.println("Give valid option!");
            }
        } while (check == false);
        trainer = ud.getUserById(strid);

        return trainer;
    }

    public List<Course> restCourses(User student) {
        List<Course> remainingCourses = new ArrayList();
        int id = student.getId();
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("SELECT*from Courses c where id not in"
                + "(select crid from StudentsPerCourse where stid=" + id + ");", conn);
        ResultSet rs = queryExecute(pst);
        remainingCourses.addAll(getCourseList(rs));
        closeConnection(conn);
        return remainingCourses;
    }

    public void insertStudentsPerCourse(StudentsPerCourse stpc) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        String sql = "INSERT INTO StudentsPerCourse(stid,crid) VALUES(?,?)";
        PreparedStatement pst = createPreparedStatement(sql, conn);
        setStudentsPerCourseParams(pst, stpc);
        closeConnection(conn);
    }

    public List<StudentsPerCourse> getAllStudentsPerCourse() {
        List<StudentsPerCourse> allStudentsPerCourse = new ArrayList();
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("SELECT * FROM bootcamp.StudentsPerCourse ORDER BY crid;", conn);
        ResultSet rs = queryExecute(pst);
        allStudentsPerCourse.addAll(getStudentsPerCourseList(rs));
        closeConnection(conn);
        return allStudentsPerCourse;
    }

    public List<StudentsPerCourse> getAllCoursesPerStudent(User student) {
        List<StudentsPerCourse> allStudentsPerCourse = new ArrayList();
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("SELECT * FROM bootcamp.StudentsPerCourse where stid=" + student.getId() + ";", conn);
        ResultSet rs = queryExecute(pst);
        allStudentsPerCourse.addAll(getStudentsPerCourseList(rs));
        closeConnection(conn);
        return allStudentsPerCourse;
    }

    public List<User> getStudentsPerCourseByCourse(Course course) {
        List<User> students = new ArrayList();
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("SELECT u.id,u.name,u.role "
                + "from(users u INNER JOIN StudentsPerCourse strcr ON "
                + "(strcr.stid=u.id and strcr.crid=" + course.getId() + "));", conn);
        ResultSet rs = queryExecute(pst);
        students.addAll(getUserList(rs));
        closeConnection(conn);
        return students;
    }

    public void updateStudentsPerCourse(StudentsPerCourse stpc, StudentsPerCourse newstpc) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        String sql = "UPDATE StudentsPerCourse SET crid=?,strid=? where (crid =" + stpc.getCrid() + " and strid=" + stpc.getStid() + ");";
        PreparedStatement pst = createPreparedStatement(sql, conn);
        setStudentsPerCourseParams(pst, newstpc);
        closeConnection(conn);
    }

    public StudentsPerCourse getStudentsPerCourseById(int crid, int stid) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("SELECT * FROM bootcamp.StudentsPerCourse "
                + "where (crid=" + crid + " and stid=" + stid + ");", conn);
        ResultSet rs = queryExecute(pst);
        StudentsPerCourse studentsPerCourse = getStudentsPerCourse(rs);
        closeConnection(conn);
        return studentsPerCourse;
    }

    public static StudentsPerCourse createStudentsPerCourse() {
        UserDao ud = new UserDao();
        int stid = userIdSelector("Student");
        User student = ud.getUserById(stid);
        Course course = restCoursesSelectorPerStudent(student);
        int crid = course.getId();
        StudentsPerCourse stpcr = new StudentsPerCourse(stid, crid);
        return stpcr;
    }

    public static StudentsPerCourse insertNewStudentsPerCourse() {
        StudentsPerCourseDao stpcd = new StudentsPerCourseDao();
        StudentsPerCourse stpc = createStudentsPerCourse();
        stpcd.insertStudentsPerCourse(stpc);
        return stpc;
    }

    public static void insertNewStudentsPerCourseAndUpdateDb() {
        StudentsPerCourse stpc = insertNewStudentsPerCourse();
        StudentPerCoursePerAssignmentDao spcpad = new StudentPerCoursePerAssignmentDao();
        int stid = stpc.getStid();
        int crid = stpc.getCrid();
        CourseDao cd = new CourseDao();
        Course course = cd.getCourseById(crid);
        AssignmentsPerCourseDao apcd = new AssignmentsPerCourseDao();
        List<Assignment> assignmentsPerCourse = apcd.getAssignmentsPerCourseByCourse(course);
        for (Assignment assignment : assignmentsPerCourse) {
            StudentPerCoursePerAssignment spapc = new StudentPerCoursePerAssignment(stid, crid, assignment.getId());
            spcpad.insertStudentPerCoursePerAssignment(spapc);
        }
    }

    public void deleteStudentsPerCourse(int crid, int stid) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("DELETE FROM StudentsPerCourse where (crid=" + crid + " and stid=" + stid + ");", conn);
        int ex = queryExecuteUpdate(pst);
        closeConnection(conn);
    }

    public static void updateStudentsPerCourse() {
        StudentsPerCourseDao stpcd = new StudentsPerCourseDao();
        Course course = courseSelector();
        System.out.println("Select the Student that you want to replace");
        User student = studentSelectorPerCourse(course);
        System.out.println("Select the new Trainer");
        StudentsPerCourse stpc = stpcd.getStudentsPerCourseById(course.getId(), student.getId());
        StudentsPerCourse newstpc = new StudentsPerCourse(student.getId(), restCoursesSelectorPerStudent(student).getId());
        stpcd.updateStudentsPerCourse(stpc, newstpc);
    }

    public static void viewAllStudentsPerCourse() {
        StudentsPerCourseDao stpcd = new StudentsPerCourseDao();
        List<StudentsPerCourse> allStudentsPerCourse = stpcd.getAllStudentsPerCourse();
        CourseDao cd = new CourseDao();
        UserDao ud = new UserDao();
        int crid = 0;
        for (StudentsPerCourse studentsPerCourse : allStudentsPerCourse) {
            if (crid != studentsPerCourse.getCrid()) {
                crid = studentsPerCourse.getCrid();
                System.out.println("The students for the course "
                        + cd.getCourseById(studentsPerCourse.getCrid()).getName() + " are:");
                System.out.println(ud.getUserById(studentsPerCourse.getStid()));
            } else {
                System.out.println(ud.getUserById(studentsPerCourse.getStid()));
            }
        }
    }

    public static List<User> viewStudentsPerCourse(Course course) {
        StudentsPerCourseDao stpcd = new StudentsPerCourseDao();
        List<User> studentsPerCourse = stpcd.getStudentsPerCourseByCourse(course);
        int i = 1;
        System.out.println("The students for the course " + course.getName() + " are:");
        for (User student : studentsPerCourse) {
            System.out.println(i + " " + student);
            i++;
        }
        return studentsPerCourse;
    }

    public static void deleteStudentsPerCourse() {
        StudentsPerCourseDao stpcd = new StudentsPerCourseDao();
        Course course = courseSelector();
        System.out.println("Select the student that you want to remove");
        User student = studentSelectorPerCourse(course);
        stpcd.deleteStudentsPerCourse(course.getId(), student.getId());
    }

}
