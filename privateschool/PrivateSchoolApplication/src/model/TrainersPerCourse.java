package model;

public class TrainersPerCourse {

    private int trid;
    private int crid;

    public TrainersPerCourse() {
    }

    public TrainersPerCourse(int trid, int crid) {
        this.trid = trid;
        this.crid = crid;
    }

    public int getTrid() {
        return trid;
    }

    public void setTrid(int trid) {
        this.trid = trid;
    }

    public int getCrid() {
        return crid;
    }

    public void setCrid(int crid) {
        this.crid = crid;
    }

}
