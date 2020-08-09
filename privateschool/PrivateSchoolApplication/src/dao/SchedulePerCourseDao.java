package dao;

import static dao.CourseDao.courseSelectorId;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.SchedulePerCourse;
import model.StudentsPerCourse;
import model.User;

import static utils.DBUtils.closeConnection;
import static utils.DBUtils.createPreparedStatement;
import static utils.DBUtils.getconnection;
import static utils.DBUtils.queryExecute;
import static utils.DBUtils.queryExecuteUpdate;
import static utils.GenericUtils.giveIntFromUser;

public class SchedulePerCourseDao {

    public static void setScheduleParams(PreparedStatement pst, SchedulePerCourse schedule) {
        try {
            String day = schedule.getDay();
            pst.setInt(1, schedule.getCourseid());
            pst.setString(2, day);
            pst.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public static List<SchedulePerCourse> getScheduleList(ResultSet rs) {
        List<SchedulePerCourse> scheduleList = new ArrayList();
        try {
            while (rs.next()) {
                SchedulePerCourse schedule = new SchedulePerCourse();
                schedule.setCourseid(rs.getInt("courseid"));
                schedule.setDay(rs.getString("day"));
                scheduleList.add(schedule);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return scheduleList;
    }

    public static SchedulePerCourse getSchedule(ResultSet rs) {
        SchedulePerCourse schedule = new SchedulePerCourse();
        try {
            while (rs.next()) {
                schedule.setCourseid(rs.getInt("courseid"));
                schedule.setDay(rs.getString("day"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SchedulePerCourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return schedule;
    }

    public static SchedulePerCourse scheduleSelector() {
        boolean check = false;
        SchedulePerCourse schedule = new SchedulePerCourse();
        SchedulePerCourseDao sd = new SchedulePerCourseDao();
        do {
            viewAllSchedule("Select from Schedule: ");
            int id = giveIntFromUser("Select a day and course by ID: ");
            schedule = sd.getScheduleById(id);
            if (schedule.getCourseid() == 0) {
                System.out.println("Give valid option: ");
            } else {
                check = true;
            }
        } while (check == false);
        return schedule;
    }

    public void insertSchedule(SchedulePerCourse schedule) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        String sql = "INSERT INTO SchedulePerCourse(courseid,day) VALUES(?,?)";
        PreparedStatement pst = createPreparedStatement(sql, conn);
        setScheduleParams(pst, schedule);
        closeConnection(conn);
    }

    public void updateSchedule(SchedulePerCourse schedule) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        String sql = "UPDATE SchedulePerCourse SET courseid=?,day=? where courseid =" + schedule.getCourseid();
        PreparedStatement pst = createPreparedStatement(sql, conn);
        setScheduleParams(pst, schedule);
        closeConnection(conn);
    }

    public List<SchedulePerCourse> getAllSchedule() {
        List<SchedulePerCourse> allSchedule = new ArrayList();
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("SELECT*FROM SchedulePerCourse", conn);
        ResultSet rs = queryExecute(pst);
        allSchedule.addAll(getScheduleList(rs));
        closeConnection(conn);
        return allSchedule;
    }

    public SchedulePerCourse getScheduleById(int id) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("SELECT*FROM SchedulePerCourse where courseid=" + id, conn);
        ResultSet rs = queryExecute(pst);
        SchedulePerCourse schedule = getSchedule(rs);
        closeConnection(conn);
        return schedule;
    }

    public void deleteSchedule(int id) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("DELETE FROM SchedulePerCourse where courseid =" + id, conn);
        int ex = queryExecuteUpdate(pst);
        closeConnection(conn);
    }

    public static void insertNewSchedule() {
        SchedulePerCourseDao sd = new SchedulePerCourseDao();
        sd.insertSchedule(createSchedule());
    }

    public static SchedulePerCourse createSchedule() {
        SchedulePerCourse schedule = new SchedulePerCourse(courseSelectorId());
        return schedule;
    }

    public static void deleteSchedule() {
        SchedulePerCourseDao sd = new SchedulePerCourseDao();
        SchedulePerCourse schedule = scheduleSelector();
        sd.deleteSchedule(schedule.getCourseid());
    }

    public static void viewAllSchedule(String message) {
        List<SchedulePerCourse> allSchedule = new ArrayList();
        SchedulePerCourseDao sd = new SchedulePerCourseDao();
        allSchedule = sd.getAllSchedule();
        CourseDao cd = new CourseDao();
        System.out.println(message);
        for (SchedulePerCourse schedule : allSchedule) {
            System.out.println(" Course: " + cd.getCourseById(schedule.getCourseid()).getName()
                    + " Day: " + schedule.getDay());
        }
    }

    public static void viewScheduleByStudent(User user) {
        StudentsPerCourseDao spcd = new StudentsPerCourseDao();
        List<StudentsPerCourse> spclist = spcd.getAllCoursesPerStudent(user);
        SchedulePerCourseDao sd = new SchedulePerCourseDao();
        CourseDao cd = new CourseDao();
        List<SchedulePerCourse> allSchedule = sd.getAllSchedule();
        spclist.forEach((studentsPerCourse) -> {
            allSchedule.stream().filter((schedule) -> (studentsPerCourse.getCrid() == schedule.getCourseid())).forEachOrdered((schedule) -> {
                System.out.println("Course: " + cd.getCourseById(schedule.getCourseid()).getName()
                        + " Day: " + schedule.getDay());
            });
        });
    }

    public static void updateSchedule() {
        SchedulePerCourseDao cd = new SchedulePerCourseDao();
        int id = scheduleSelector().getCourseid();
        SchedulePerCourse schedule = createSchedule();
        schedule.setCourseid(id);
        cd.updateSchedule(schedule);
    }

}
