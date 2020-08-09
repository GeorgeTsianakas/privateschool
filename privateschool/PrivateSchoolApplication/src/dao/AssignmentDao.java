package dao;

import static dao.AssignmentsPerCourseDao.getRestAssignemntsIdsSet;
import static dao.AssignmentsPerCourseDao.viewAssignmentsPerCourse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Assignment;
import model.Course;

import static utils.DBUtils.closeConnection;
import static utils.DBUtils.createPreparedStatement;
import static utils.DBUtils.getconnection;
import static utils.DBUtils.queryExecute;
import static utils.DBUtils.queryExecuteUpdate;
import static utils.GenericUtils.getDate;
import static utils.GenericUtils.giveInputFromUser;
import static utils.GenericUtils.giveIntFromUser;

public class AssignmentDao {

    public static void setAssignmentParams(PreparedStatement pst, Assignment Assignment) {

        try {
            Date dt = Assignment.getSubmission();
            Calendar c = Calendar.getInstance();
            c.setTime(dt);
            c.add(Calendar.DATE, 1);
            dt = c.getTime();
            java.sql.Date sqlDate = new java.sql.Date(dt.getTime());
            pst.setString(1, Assignment.getName());
            pst.setDate(2, sqlDate);
            pst.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public static List<Assignment> getAssignmentList(ResultSet rs) {
        List<Assignment> assignments = new ArrayList();
        try {
            while (rs.next()) {
                Assignment assignment = new Assignment();
                assignment.setId(rs.getInt("id"));
                assignment.setName(rs.getString("name"));
                assignment.setSubmission(rs.getDate("submission"));
                assignments.add(assignment);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return assignments;
    }

    public static Assignment getAssignment(ResultSet rs) {
        Assignment assignment = new Assignment();
        try {
            while (rs.next()) {
                assignment.setId(rs.getInt("id"));
                assignment.setName(rs.getString("name"));
                assignment.setSubmission(rs.getDate("submission"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AssignmentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return assignment;
    }

    public static Assignment assignmentSelector() {
        boolean check = false;
        Assignment assignment = new Assignment();
        AssignmentDao cd = new AssignmentDao();
        do {
            viewAllAssignments("Select Assignment: ");
            int id = giveIntFromUser("Select a Assignment by ID: ");
            assignment = cd.getAssignmentById(id);
            if (assignment.getName() == null) {
                System.out.println("Give valid option: ");
            } else {
                check = true;
            }
        } while (check == false);
        return assignment;
    }

    public static Assignment restAssignmentSelectorPerCourse(Course course) {
        boolean check = false;
        int assid = 0;
        Assignment assigment = new Assignment();
        AssignmentsPerCourseDao apcd = new AssignmentsPerCourseDao();
        AssignmentDao ad = new AssignmentDao();
        List<Assignment> remainingAssignemnts = apcd.restAssignemnts(course);
        Set<Integer> remainingAssignemntsIds = new HashSet();
        remainingAssignemntsIds.addAll(getRestAssignemntsIdsSet(remainingAssignemnts));
        if (remainingAssignemnts.size() >= 1) {
            do {
                viewRestAssignmets(remainingAssignemnts, "The available Assignments for this Course are: ");
                assid = giveIntFromUser("Select a Assignment by ID: ");
                if (remainingAssignemntsIds.contains(assid)) {
                    check = true;
                } else {
                    System.out.println("Give valid option!");
                }
            } while (check == false);
        } else {
            System.out.println("No more Assignments are available for this Course");
        }
        assigment = ad.getAssignmentById(assid);
        return assigment;
    }

    public static Assignment assignmentSelectorPerCourse(Course course) {
        boolean check = false;
        int assid = 0;
        Assignment assigment = new Assignment();
        AssignmentDao ad = new AssignmentDao();
        List<Assignment> assignments = viewAssignmentsPerCourse(course);
        do {
            int selection = giveIntFromUser("Select a Assignment by Number: ");
            if (selection >= 1 && selection <= assignments.size()) {
                assid = assignments.get(selection - 1).getId();
                check = true;
            } else {
                System.out.println("Give valid option!");
            }
        } while (check == false);
        assigment = ad.getAssignmentById(assid);
        return assigment;
    }

    public void insertAssignment(Assignment Assignment) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        String sql = "INSERT INTO Assignments(name,submission) VALUES(?,?)";
        PreparedStatement pst = createPreparedStatement(sql, conn);
        setAssignmentParams(pst, Assignment);
        closeConnection(conn);
    }

    public List<Assignment> getAllAssignments() {
        List<Assignment> allAssignments = new ArrayList();
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("SELECT*FROM Assignments", conn);
        ResultSet rs = queryExecute(pst);
        allAssignments.addAll(getAssignmentList(rs));
        closeConnection(conn);
        return allAssignments;
    }

    public Assignment getAssignmentById(int id) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("SELECT*FROM Assignments where id=" + id, conn);
        ResultSet rs = queryExecute(pst);
        Assignment assignment = getAssignment(rs);
        closeConnection(conn);
        return assignment;
    }

    public List<Assignment> getAssignmentsByName(String name) {
        List<Assignment> allAssignmentsByName = new ArrayList();
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("SELECT*FROM Assignments where Name='" + name + "'", conn);
        ResultSet rs = queryExecute(pst);
        allAssignmentsByName.addAll(getAssignmentList(rs));
        closeConnection(conn);
        return allAssignmentsByName;
    }

    public void deleteAssignment(int id) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("DELETE FROM Assignments where id =" + id, conn);
        int ex = queryExecuteUpdate(pst);
        closeConnection(conn);
    }

    public void updateAssignment(Assignment assignment) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        String sql = "UPDATE Assignments SET name=?,submission=? where id =" + assignment.getId();
        PreparedStatement pst = createPreparedStatement(sql, conn);
        setAssignmentParams(pst, assignment);
        closeConnection(conn);
    }

    public static void insertNewAssignment() {
        AssignmentDao ad = new AssignmentDao();
        ad.insertAssignment(createAssignment());
    }

    public static Assignment createAssignment() {
        Assignment Assignment = new Assignment(giveInputFromUser("Give Assignments name: "), getDate("Give Submission Date: "));
        return Assignment;
    }

    public static void deleteAssignment() {
        AssignmentDao ad = new AssignmentDao();
        Assignment assignment = assignmentSelector();
        ad.deleteAssignment(assignment.getId());
    }

    public static void viewAllAssignments(String message) {
        List<Assignment> allAssignments = new ArrayList();
        AssignmentDao cd = new AssignmentDao();
        allAssignments = cd.getAllAssignments();
        System.out.println(message);
        for (Assignment allAssignment : allAssignments) {
            System.out.println(allAssignment);
        }
    }

    public static void viewRestAssignmets(List<Assignment> remainingAssignemnts, String message) {
        AssignmentDao ad = new AssignmentDao();
        System.out.println(message);
        remainingAssignemnts.forEach((remainingAssignemnt) -> {
            System.out.println(remainingAssignemnt);
        });
    }

    public static void updateAssignment() {
        AssignmentDao cd = new AssignmentDao();
        int id = assignmentSelector().getId();
        Assignment assignment = createAssignment();
        assignment.setId(id);
        cd.updateAssignment(assignment);
    }

}
