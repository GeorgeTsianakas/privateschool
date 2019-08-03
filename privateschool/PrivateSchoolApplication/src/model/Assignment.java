package model;

import java.util.Date;
import static utils.GenericUtils.dateFix;

public class Assignment {

    private int id;
    private String name;
    private Date submission;

    public Assignment() {
    }

    public Assignment(String name, Date submission) {
        this.name = name;
        this.submission = submission;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getSubmission() {
        return submission;
    }

    public void setSubmission(Date submission) {
        this.submission = submission;
    }

    @Override
    public String toString() {
        return "id= " + id + ", name= " + name + ", submission= " + dateFix(submission);
    }

}
