package dto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Course;

import static utils.DBUtils.closeConnection;
import static utils.DBUtils.createPreparedStatement;
import static utils.DBUtils.getconnection;
import static utils.DBUtils.queryExecute;

public class StudentPerCoursePerAssignmentDTO {

    private String name;
    private String course;
    private String ass;
    private int stid;
    private int crid;
    private int asid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getAss() {
        return ass;
    }

    public void setAss(String ass) {
        this.ass = ass;
    }

    public int getStid() {
        return stid;
    }

    public void setStid(int stid) {
        this.stid = stid;
    }

    public int getCrid() {
        return crid;
    }

    public void setCrid(int crid) {
        this.crid = crid;
    }

    public int getAsid() {
        return asid;
    }

    public void setAsid(int asid) {
        this.asid = asid;
    }

    public List<StudentPerCoursePerAssignmentDTO> getStudentCourseAssigmentByCourse(Course course) {
        List<StudentPerCoursePerAssignmentDTO> spcpah = new ArrayList();
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("Select u.name, u.id as stid, c.name as course, c.id as crid, a.name as assignment,a.id as assid  "
                + "from(((users u INNER JOIN studentpercourseperassignment spcpa ON spcpa.stid=u.id)"
                + "INNER JOIN assignments a ON spcpa.assid=a.id)INNER JOIN courses c ON spcpa.crid=c.id)"
                + "where spcpa.crid=" + course.getId(), conn);
        ResultSet rs = queryExecute(pst);
        spcpah.addAll(getStudentCourseAssigmentList(rs));
        closeConnection(conn);
        return spcpah;
    }

    public static List<StudentPerCoursePerAssignmentDTO> getStudentCourseAssigmentList(ResultSet rs) {
        List<StudentPerCoursePerAssignmentDTO> spcpahList = new ArrayList();
        try {
            while (rs.next()) {
                StudentPerCoursePerAssignmentDTO spcpa = new StudentPerCoursePerAssignmentDTO();
                spcpa.setName(rs.getString("name"));
                spcpa.setCourse(rs.getString("course"));
                spcpa.setAss(rs.getString("assignment"));
                spcpa.setAsid(rs.getInt("assid"));
                spcpa.setCrid(rs.getInt("crid"));
                spcpa.setStid(rs.getInt("stid"));
                spcpahList.add(spcpa);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return spcpahList;
    }

    @Override
    public String toString() {
        return "Student=" + name + ", course=" + course + ", assigment=" + ass;
    }

}
