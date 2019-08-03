package model;

public class StudentsPerCourse {

    private int stid;
    private int crid;

    public StudentsPerCourse() {
    }

    public StudentsPerCourse(int stid, int crid) {
        this.stid = stid;
        this.crid = crid;
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

}
