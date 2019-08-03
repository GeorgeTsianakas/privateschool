package dao;

import static dao.AssignmentDao.assignmentSelectorPerCourse;
import static dao.AssignmentDao.getAssignmentList;
import static dao.AssignmentDao.restAssignmentSelectorPerCourse;
import static dao.CourseDao.courseSelector;
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
import model.AssignmentsPerCourse;
import model.Course;
import static utils.DBUtils.closeConnection;
import static utils.DBUtils.createPreparedStatement;
import static utils.DBUtils.getconnection;
import static utils.DBUtils.queryExecute;
import static utils.DBUtils.queryExecuteUpdate;

public class AssignmentsPerCourseDao {

    public static void setAssignmentsPerCourseParams(PreparedStatement pst, AssignmentsPerCourse apc) {

        try {
            pst.setInt(1, apc.getCrId());
            pst.setInt(2, apc.getAssId());
            pst.executeUpdate();
        } catch (SQLException ex) {
            //System.out.println("No action made");
        }
    }

    public static Set<Integer> getRestAssignemntsIdsSet(List<Assignment> remainingAssignemnts) {
        Set<Integer> remainingAssignemntsIds = new HashSet();
        remainingAssignemnts.forEach((assignment) -> {
            remainingAssignemntsIds.add(assignment.getId());
        });
        return remainingAssignemntsIds;
    }

    public static List<AssignmentsPerCourse> getAssignmentsPerCourseList(ResultSet rs) {
        List<AssignmentsPerCourse> assignmentsPerCourses = new ArrayList();
        try {
            while (rs.next()) {
                AssignmentsPerCourse assignmentsPerCourse = new AssignmentsPerCourse();
                assignmentsPerCourse.setCrId(rs.getInt("crid"));
                assignmentsPerCourse.setAssId(rs.getInt("assid"));
                assignmentsPerCourses.add(assignmentsPerCourse);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return assignmentsPerCourses;
    }

    public static AssignmentsPerCourse getAssignmentsPerCourse(ResultSet rs) {
        AssignmentsPerCourse assignmentsPerCourse = new AssignmentsPerCourse();
        try {
            while (rs.next()) {
                assignmentsPerCourse.setCrId(rs.getInt("crid"));
                assignmentsPerCourse.setAssId(rs.getInt("assid"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AssignmentsPerCourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return assignmentsPerCourse;
    }

    public List<Assignment> restAssignemnts(Course course) {
        List<Assignment> remainingAssignemnts = new ArrayList();
        int id = course.getId();
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("SELECT*from assignments ass where id not in"
                + "(select assid from AssignmentsPerCourse crass where crid=" + id + ");", conn);
        ResultSet rs = queryExecute(pst);
        remainingAssignemnts.addAll(getAssignmentList(rs));
        closeConnection(conn);
        return remainingAssignemnts;
    }

    public List<Assignment> getAssignmentsPerCourseByCourse(Course course) {
        List<Assignment> assignemnts = new ArrayList();
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("SELECT ass.id,ass.name,ass.submission "
                + "from(assignments ass INNER JOIN assignmentspercourse crass ON "
                + "(crass.assid=ass.id and crass.crid=" + course.getId() + "));", conn);
        ResultSet rs = queryExecute(pst);
        assignemnts.addAll(getAssignmentList(rs));
        closeConnection(conn);
        return assignemnts;
    }

    public void insertAssignmentsPerCourse(AssignmentsPerCourse apc) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        String sql = "INSERT INTO AssignmentsPerCourse(crid,assid) VALUES(?,?)";
        PreparedStatement pst = createPreparedStatement(sql, conn);
        setAssignmentsPerCourseParams(pst, apc);
        closeConnection(conn);
    }

    public List<AssignmentsPerCourse> getAllAssignmentsPerCourse() {
        List<AssignmentsPerCourse> allAssignmentsPerCourse = new ArrayList();
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("SELECT * FROM bootcamp.assignmentspercourse ORDER BY crid;", conn);
        ResultSet rs = queryExecute(pst);
        allAssignmentsPerCourse.addAll(getAssignmentsPerCourseList(rs));
        closeConnection(conn);
        return allAssignmentsPerCourse;
    }

    public AssignmentsPerCourse getAssignmentsPerCourseById(int crid, int assid) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("SELECT * FROM bootcamp.assignmentspercourse "
                + "where (crid=" + crid + " and assid=" + assid + ");", conn);
        ResultSet rs = queryExecute(pst);
        AssignmentsPerCourse assignmentsPerCourse = getAssignmentsPerCourse(rs);
        closeConnection(conn);
        return assignmentsPerCourse;
    }

    public void deleteAssignmentsPerCourse(int crid, int assid) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("DELETE FROM AssignmentsPerCourse where (crid=" + crid + " and assid=" + assid + ");", conn);
        int ex = queryExecuteUpdate(pst);
        closeConnection(conn);
    }

    public void updateAssignmentsPerCourse(AssignmentsPerCourse apc, AssignmentsPerCourse newapc) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        String sql = "UPDATE AssignmentsPerCourse SET crid=?,assid=? where (crid =" + apc.getCrId() + " and assid=" + apc.getAssId() + ");";
        PreparedStatement pst = createPreparedStatement(sql, conn);
        setAssignmentsPerCourseParams(pst, newapc);
        closeConnection(conn);
    }

    public static void updateAssignmentsPerCourse() {
        AssignmentsPerCourseDao apcd = new AssignmentsPerCourseDao();
        Course course = courseSelector();
        System.out.println("Select the Assignment that you want to replace");
        Assignment assignment = assignmentSelectorPerCourse(course);
        System.out.println("Select the new Assignment");
        AssignmentsPerCourse apc = apcd.getAssignmentsPerCourseById(course.getId(), assignment.getId());
        AssignmentsPerCourse newapc = new AssignmentsPerCourse(course.getId(), restAssignmentSelectorPerCourse(course).getId());
        apcd.updateAssignmentsPerCourse(apc, newapc);
    }

    public static void viewAllAssignmentsPerCourse() {
        AssignmentsPerCourseDao apcd = new AssignmentsPerCourseDao();
        List<AssignmentsPerCourse> allAssignmentsPerCourse = apcd.getAllAssignmentsPerCourse();
        CourseDao cd = new CourseDao();
        AssignmentDao ad = new AssignmentDao();
        int crid = 0;
        for (AssignmentsPerCourse assignmentsPerCourse : allAssignmentsPerCourse) {
            if (crid != assignmentsPerCourse.getCrId()) {
                crid = assignmentsPerCourse.getCrId();
                System.out.println("\nThe Assignments for the Course "
                        + cd.getCourseById(assignmentsPerCourse.getCrId()).getName() + " are:");
                System.out.println(ad.getAssignmentById(assignmentsPerCourse.getAssId()));
            } else {
                System.out.println(ad.getAssignmentById(assignmentsPerCourse.getAssId()));
            }
        }
    }

    public static List<Assignment> viewAssignmentsPerCourse(Course course) {
        AssignmentsPerCourseDao apcd = new AssignmentsPerCourseDao();
        List<Assignment> assignmentsPerCourse = apcd.getAssignmentsPerCourseByCourse(course);
        int i = 1;
        System.out.println("The Assigmnets for the Course " + course.getName() + " are:");
        for (Assignment assignment : assignmentsPerCourse) {
            System.out.println(i + " " + assignment);
            i++;
        }
        return assignmentsPerCourse;
    }

    public static void deleteAssigmnentPerCourse() {
        AssignmentsPerCourseDao apcd = new AssignmentsPerCourseDao();
        Course course = courseSelector();
        System.out.println("Select the Assignment that you want to Delete");
        Assignment assignment = assignmentSelectorPerCourse(course);
        apcd.deleteAssignmentsPerCourse(course.getId(), assignment.getId());

    }

    public static void insertNewAssignmentPerCourse() {
        AssignmentsPerCourseDao apcd = new AssignmentsPerCourseDao();
        AssignmentsPerCourse apc = createAssignmentsPerCourse();
        apcd.insertAssignmentsPerCourse(apc);
    }

    public static AssignmentsPerCourse createAssignmentsPerCourse() {
        Course course = courseSelector();
        Assignment assignment = restAssignmentSelectorPerCourse(course);
        AssignmentsPerCourse apc = new AssignmentsPerCourse(course.getId(), assignment.getId());
        return apc;
    }

    public static AssignmentsPerCourse createAssignmentsPerCourse(Course course) {
        Assignment assignment = restAssignmentSelectorPerCourse(course);
        AssignmentsPerCourse apc = new AssignmentsPerCourse(course.getId(), assignment.getId());
        return apc;
    }

    public static void insertNewAssignmentPerCourse(Course course) {
        AssignmentsPerCourseDao apcd = new AssignmentsPerCourseDao();
        AssignmentsPerCourse apc = createAssignmentsPerCourse(course);
        apcd.insertAssignmentsPerCourse(apc);
    }

}
