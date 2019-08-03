package model;

public class AssignmentsPerCourse {

    private int crId;
    private int assId;

    public AssignmentsPerCourse() {
    }

    public AssignmentsPerCourse(int crId, int assId) {
        this.crId = crId;
        this.assId = assId;
    }

    public int getCrId() {
        return crId;
    }

    public void setCrId(int crId) {
        this.crId = crId;
    }

    public int getAssId() {
        return assId;
    }

    public void setAssId(int assId) {
        this.assId = assId;
    }

    @Override
    public String toString() {
        return "AssignmentsPerCourse{" + "crId=" + crId + ", assId=" + assId + '}';
    }

}
