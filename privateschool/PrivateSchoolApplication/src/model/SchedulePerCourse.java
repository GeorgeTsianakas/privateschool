package model;

public class SchedulePerCourse {

    private int courseid;
    private String day;

    public SchedulePerCourse() {
    }

    public SchedulePerCourse(int courseid, String day) {
        this.courseid = courseid;
        this.day = day;
    }

    public SchedulePerCourse(int courseSelectorId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getCourseid() {
        return courseid;
    }

    public void setCourseid(int courseid) {
        this.courseid = courseid;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "SchedulePerCourse{" + "courseid=" + courseid + ", day=" + day + '}';
    }

}
