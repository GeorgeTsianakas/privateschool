package model;

public class StudentPerCoursePerAssignment {

    private int stid;
    private int crid;
    private int assid;
    private int mark;
    private boolean submit;

    public StudentPerCoursePerAssignment() {
    }

    public StudentPerCoursePerAssignment(int stid, int crid, int assid) {
        this.stid = stid;
        this.crid = crid;
        this.assid = assid;
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

    public int getAssid() {
        return assid;
    }

    public void setAssid(int assid) {
        this.assid = assid;
    }

    public boolean isSubmit() {
        return submit;
    }

    public void setSubmit(boolean submit) {
        this.submit = submit;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

}
