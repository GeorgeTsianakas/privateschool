package dao;

import dto.StudentPerCoursePerAssignmentDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Course;
import model.StudentPerCoursePerAssignment;
import model.User;
import static utils.DBUtils.closeConnection;
import static utils.DBUtils.createPreparedStatement;
import static utils.DBUtils.getconnection;
import static utils.DBUtils.queryExecute;
import static utils.GenericUtils.giveIntFromUser;

public class StudentPerCoursePerAssignmentDao {

    public static void setStudentPerCoursePerAssignment(PreparedStatement pst, StudentPerCoursePerAssignment stpcpa) {
        try {
            pst.setInt(1, stpcpa.getStid());
            pst.setInt(2, stpcpa.getCrid());
            pst.setInt(3, stpcpa.getAssid());
            pst.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public static List<StudentPerCoursePerAssignment> getStudentPerCoursePerAssignmentList(ResultSet rs) {
        List<StudentPerCoursePerAssignment> spcpaList = new ArrayList();
        try {
            while (rs.next()) {
                StudentPerCoursePerAssignment spcpa = new StudentPerCoursePerAssignment();
                spcpa.setCrid(rs.getInt("crid"));
                spcpa.setStid(rs.getInt("stid"));
                spcpa.setAssid(rs.getInt("assid"));
                spcpa.setSubmit(rs.getBoolean("submit"));
                spcpaList.add(spcpa);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return spcpaList;
    }

    public static StudentPerCoursePerAssignment getStudentPerCoursePerAssignment(ResultSet rs) {
        StudentPerCoursePerAssignment spcpa = new StudentPerCoursePerAssignment();
        try {
            while (rs.next()) {
                spcpa.setCrid(rs.getInt("crid"));
                spcpa.setStid(rs.getInt("stid"));
                spcpa.setAssid(rs.getInt("assid"));
                spcpa.setSubmit(rs.getBoolean("submit"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(StudentPerCoursePerAssignmentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return spcpa;
    }

    public static void setMarkStudentPerCoursePerAssignment(PreparedStatement pst, StudentPerCoursePerAssignment stpcpa) {
        try {
            pst.setInt(1, stpcpa.getMark());
            pst.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public static StudentPerCoursePerAssignment selectStudentPerCoursePerAssignment(User student) {
        boolean check = false;
        int stid = 0;
        int crid = 0;
        int assid = 0;
        StudentPerCoursePerAssignmentDao spcpad = new StudentPerCoursePerAssignmentDao();
        StudentPerCoursePerAssignment spcpa = new StudentPerCoursePerAssignment();
        do {
            List<StudentPerCoursePerAssignment> assigmentsPerStudent = viewStudentPerCoursePerAssignmentViews(student);
            int selection = giveIntFromUser("Select a Assignment to Submit by Number: ");
            if (selection >= 1 && selection <= assigmentsPerStudent.size()) {
                stid = assigmentsPerStudent.get(selection - 1).getStid();
                crid = assigmentsPerStudent.get(selection - 1).getCrid();
                assid = assigmentsPerStudent.get(selection - 1).getAssid();
                check = true;
            } else {
                System.out.println("Give valid option!");
            }
        } while (check == false);
        spcpa = spcpad.getStudentPerCoursePerAssignmentById(crid, stid, assid);
        return spcpa;
    }

    public static StudentPerCoursePerAssignment selectStudentPerCoursePerAssignmentByTrainer(User trainer) {
        int i = 1;
        boolean check = false;
        int stid = 0;
        int crid = 0;
        int assid = 0;
        StudentPerCoursePerAssignmentDao spcpad = new StudentPerCoursePerAssignmentDao();
        StudentPerCoursePerAssignment spcpa = new StudentPerCoursePerAssignment();
        StudentPerCoursePerAssignmentDTO spcah = new StudentPerCoursePerAssignmentDTO();
        List<StudentPerCoursePerAssignmentDTO> spcahList = new ArrayList();
        TrainersPerCourseDao tpcd = new TrainersPerCourseDao();
        List<Course> trainerscourses = tpcd.getTrainersPerCourseByTrainer(trainer);
        trainerscourses.forEach((trainerscourse) -> {
            spcahList.addAll(spcah.getStudentCourseAssigmentByCourse(trainerscourse));
        });
        do {
            for (StudentPerCoursePerAssignmentDTO studentCourseAssigmentHelpClass : spcahList) {

                System.out.println(i + " " + studentCourseAssigmentHelpClass);
                i++;
            }
            int selection = giveIntFromUser("Select a Assignment to Mark by Number: ");
            if (selection >= 1 && selection <= spcahList.size()) {
                stid = spcahList.get(selection - 1).getStid();
                crid = spcahList.get(selection - 1).getCrid();
                assid = spcahList.get(selection - 1).getAsid();
                check = true;
            } else {
                System.out.println("Give valid option!");
            }
        } while (check == false);
        spcpa = spcpad.getStudentPerCoursePerAssignmentById(crid, stid, assid);
        return spcpa;
    }

    public void insertStudentPerCoursePerAssignment(StudentPerCoursePerAssignment stpcpa) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        String sql = "INSERT INTO StudentPerCoursePerAssignment(stid,crid,assid) VALUES(?,?,?)";
        PreparedStatement pst = createPreparedStatement(sql, conn);
        setStudentPerCoursePerAssignment(pst, stpcpa);
        closeConnection(conn);
    }

    public List<StudentPerCoursePerAssignment> getStudentPerCoursePerAssignmentByCourse(Course course) {
        List<StudentPerCoursePerAssignment> spcpa = new ArrayList();
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("SELECT*FROM StudentPerCoursePerAssignment where crid=" + course.getId(), conn);
        ResultSet rs = queryExecute(pst);
        spcpa.addAll(getStudentPerCoursePerAssignmentList(rs));
        closeConnection(conn);
        return spcpa;
    }

    public List<StudentPerCoursePerAssignment> getStudentPerCoursePerAssignmentByStudent(User student) {
        List<StudentPerCoursePerAssignment> spcpa = new ArrayList();
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("SELECT*FROM StudentPerCoursePerAssignment where stid=" + student.getId(), conn);
        ResultSet rs = queryExecute(pst);
        spcpa.addAll(getStudentPerCoursePerAssignmentList(rs));
        closeConnection(conn);
        return spcpa;
    }

    public void updateSubmit(StudentPerCoursePerAssignment spcpa) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        String sql = "UPDATE StudentPerCoursePerAssignment SET submit='1',stid=?,crid=?,assid=? where (stid =" + spcpa.getStid()
                + " and crid=" + spcpa.getCrid() + " and assid=" + spcpa.getAssid() + ");";
        PreparedStatement pst = createPreparedStatement(sql, conn);
        setStudentPerCoursePerAssignment(pst, spcpa);
        closeConnection(conn);
    }

    public StudentPerCoursePerAssignment getStudentPerCoursePerAssignmentById(int crid, int stid, int assid) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("SELECT * FROM bootcamp.StudentPerCoursePerAssignment "
                + "where (crid=" + crid + " and stid=" + stid + " and assid=" + assid + ");", conn);
        ResultSet rs = queryExecute(pst);
        StudentPerCoursePerAssignment spcpa = getStudentPerCoursePerAssignment(rs);
        closeConnection(conn);
        return spcpa;
    }

    public void markStudentPerCoursePerAssignment(StudentPerCoursePerAssignment spcpa) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        String sql = "UPDATE StudentPerCoursePerAssignment SET mark=? where (stid =" + spcpa.getStid()
                + " and crid=" + spcpa.getCrid() + " and assid=" + spcpa.getAssid() + ");";
        PreparedStatement pst = createPreparedStatement(sql, conn);
        setMarkStudentPerCoursePerAssignment(pst, spcpa);

    }

    public static List<StudentPerCoursePerAssignment> viewStudentPerCoursePerAssignmentViews(User student) {
        int i = 1;
        String submit = "no";
        AssignmentDao ad = new AssignmentDao();
        CourseDao cd = new CourseDao();
        StudentPerCoursePerAssignmentDao spcpad = new StudentPerCoursePerAssignmentDao();
        List<StudentPerCoursePerAssignment> spcpaList = spcpad.getStudentPerCoursePerAssignmentByStudent(student);
        List<StudentPerCoursePerAssignment> assigmentsPerStudent = new ArrayList();
        if (spcpaList.size() != 0) {
            for (StudentPerCoursePerAssignment studentPerCoursePerAssignment : spcpaList) {
                int assid = studentPerCoursePerAssignment.getAssid();
                int crid = studentPerCoursePerAssignment.getCrid();
                if (studentPerCoursePerAssignment.getStid() == student.getId()) {
                    if (studentPerCoursePerAssignment.isSubmit()) {
                        submit = "yes";
                    }
                }
                System.out.println(i + " Assignment: " + ad.getAssignmentById(assid) + " for the Course: "
                        + cd.getCourseById(crid) + "Sumbited: " + submit);
                i++;
            }
        } else {
            System.out.println("No assignment available");
        }
        return spcpaList;
    }

    public static void updateStudentPerCoursePerAssignment(User student) {
        StudentPerCoursePerAssignmentDao spcpad = new StudentPerCoursePerAssignmentDao();
        StudentPerCoursePerAssignment spcpa = selectStudentPerCoursePerAssignment(student);
        spcpad.updateSubmit(spcpa);
    }

    public static void markStudentPerCoursePerAssignment(User trainer) {
        StudentPerCoursePerAssignmentDao spcpad = new StudentPerCoursePerAssignmentDao();
        StudentPerCoursePerAssignment spcpa = selectStudentPerCoursePerAssignmentByTrainer(trainer);
        int mark = giveIntFromUser("Give mark (0 to 100): ", 0, 100);
        spcpa.setMark(mark);
        spcpad.markStudentPerCoursePerAssignment(spcpa);
    }

}
