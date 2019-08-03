package dao;

import static dao.CourseDao.courseSelector;
import static dao.CourseDao.getCourseList;
import static dao.StudentPerCoursePerAssignmentDao.viewStudentPerCoursePerAssignmentViews;
import static dao.StudentsPerCourseDao.viewStudentsPerCourse;
import static dao.UserDao.getUserList;
import static dao.UserDao.restUserIdSelector;
import static dao.UserDao.viewRestUsers;
import dto.StudentPerCoursePerAssignmentDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Course;
import model.StudentPerCoursePerAssignment;
import model.TrainersPerCourse;
import model.User;
import static utils.DBUtils.closeConnection;
import static utils.DBUtils.createPreparedStatement;
import static utils.DBUtils.getconnection;
import static utils.DBUtils.queryExecute;
import static utils.DBUtils.queryExecuteUpdate;
import static utils.GenericUtils.giveIntFromUser;

public class TrainersPerCourseDao {

    public static void setTrainersPerCourseParams(PreparedStatement pst, TrainersPerCourse tpc) {
        try {
            pst.setInt(1, tpc.getTrid());
            pst.setInt(2, tpc.getCrid());
            pst.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public static List<TrainersPerCourse> getTrainersPerCourseList(ResultSet rs) {
        List<TrainersPerCourse> trainersPerCourses = new ArrayList();
        try {
            while (rs.next()) {
                TrainersPerCourse trainersPerCourse = new TrainersPerCourse();
                trainersPerCourse.setCrid(rs.getInt("crid"));
                trainersPerCourse.setTrid(rs.getInt("trid"));
                trainersPerCourses.add(trainersPerCourse);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return trainersPerCourses;
    }

    public static TrainersPerCourse getTrainersPerCourse(ResultSet rs) {
        TrainersPerCourse trainersPerCourse = new TrainersPerCourse();
        try {
            while (rs.next()) {
                trainersPerCourse.setCrid(rs.getInt("crid"));
                trainersPerCourse.setTrid(rs.getInt("trid"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TrainersPerCourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return trainersPerCourse;
    }

    public static Set<Integer> getRestTrainersIdsSet(List<User> remainingTrainers) {
        Set<Integer> remainingTrainersIds = new HashSet();
        remainingTrainers.forEach((trainer) -> {
            remainingTrainersIds.add(trainer.getId());
        });
        return remainingTrainersIds;
    }

    public static User trainerSelectorPerCourse(Course course) {
        boolean check = false;
        int trid = 0;
        User trainer = new User();
        UserDao ud = new UserDao();
        List<User> trainers = viewTrainersPerCourse(course);
        do {
            int selection = giveIntFromUser("Select a trainer by number: ");
            if (selection >= 1 && selection <= trainers.size()) {
                trid = trainers.get(selection - 1).getId();
                check = true;
            } else {
                System.out.println("Give valid option!");
            }
        } while (check == false);
        trainer = ud.getUserById(trid);
        return trainer;
    }

    public static User restTrainersSelectorPerCourse(Course course) {
        boolean check = false;
        int trid = 0;
        User trainer = new User();
        TrainersPerCourseDao tpcd = new TrainersPerCourseDao();
        UserDao ud = new UserDao();
        List<User> remainingTrainers = tpcd.restTrainers(course);
        Set<Integer> remainingTrainersIds = new HashSet();
        remainingTrainersIds.addAll(getRestTrainersIdsSet(remainingTrainers));
        if (remainingTrainers.size() >= 1) {
            do {
                System.out.println("The available trainers for this course are: ");
                viewRestUsers(remainingTrainers);
                trid = giveIntFromUser("Select a trainer by ID: ");
                if (remainingTrainersIds.contains(trid)) {
                    check = true;
                } else {
                    System.out.println("Give valid option!");
                }
            } while (check == false);
        } else {
            System.out.println("No more trainers are available for this course");
        }
        trainer = ud.getUserById(trid);
        return trainer;
    }

    public List<User> restTrainers(Course course) {
        List<User> remainingTrainers = new ArrayList();
        int id = course.getId();
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("SELECT*from users u where id not in"
                + "(select trid from TrainersPerCourse crass where crid=" + id + ") and role='trainer';", conn);
        ResultSet rs = queryExecute(pst);
        remainingTrainers.addAll(getUserList(rs));
        closeConnection(conn);
        return remainingTrainers;

    }

    public void insertTrainersPerCourse(TrainersPerCourse tpc) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        String sql = "INSERT INTO TrainersPerCourse(trid,crid) VALUES(?,?)";
        PreparedStatement pst = createPreparedStatement(sql, conn);
        setTrainersPerCourseParams(pst, tpc);
        closeConnection(conn);
    }

    public List<TrainersPerCourse> getAllTrainersPerCourse() {
        List<TrainersPerCourse> allTrainersPerCourse = new ArrayList();
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("SELECT * FROM bootcamp.Trainerspercourse ORDER BY crid;", conn);
        ResultSet rs = queryExecute(pst);
        allTrainersPerCourse.addAll(getTrainersPerCourseList(rs));
        closeConnection(conn);
        return allTrainersPerCourse;
    }

    public List<User> getTrainersPerCourseByCourse(Course course) {
        List<User> trainers = new ArrayList();
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("SELECT u.id,u.name,u.role "
                + "from(users u INNER JOIN trainerspercourse trcr ON "
                + "(trcr.trid=u.id and trcr.crid=" + course.getId() + "));", conn);
        ResultSet rs = queryExecute(pst);
        trainers.addAll(getUserList(rs));
        closeConnection(conn);
        return trainers;
    }

    public List<Course> getTrainersPerCourseByTrainer(User trainer) {
        List<Course> courses = new ArrayList();
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("SELECT c.id, c.name from(courses c INNER JOIN trainerspercourse tpc ) "
                + "where (c.id=tpc.crid and tpc.trid=" + trainer.getId() + ");", conn);
        ResultSet rs = queryExecute(pst);
        courses.addAll(getCourseList(rs));
        closeConnection(conn);
        return courses;
    }

    public TrainersPerCourse getTrainersPerCourseById(int crid, int trid) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("SELECT * FROM bootcamp.trainerspercourse "
                + "where (crid=" + crid + " and trid=" + trid + ");", conn);
        ResultSet rs = queryExecute(pst);
        TrainersPerCourse trainersPerCourse = getTrainersPerCourse(rs);
        closeConnection(conn);
        return trainersPerCourse;
    }

    public void updateTrainersPerCourse(TrainersPerCourse tpc, TrainersPerCourse newtpc) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        String sql = "UPDATE TrainersPerCourse SET crid=?,trid=? where (crid =" + tpc.getCrid() + " and trid=" + tpc.getTrid() + ");";
        PreparedStatement pst = createPreparedStatement(sql, conn);
        setTrainersPerCourseParams(pst, newtpc);
        closeConnection(conn);
    }

    public void deleteTrainersPerCourse(int crid, int trid) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("DELETE FROM TrainersPerCourse where (crid=" + crid + " and trid=" + trid + ");", conn);
        int ex = queryExecuteUpdate(pst);
        closeConnection(conn);
    }

    public static TrainersPerCourse createTrainerPerCourse() {
        TrainersPerCourseDao tpcd = new TrainersPerCourseDao();
        Course course = courseSelector();
        int trid = restUserIdSelector(tpcd.restTrainers(course));
        int crid = course.getId();
        TrainersPerCourse tpc = new TrainersPerCourse(trid, crid);
        tpcd.insertTrainersPerCourse(tpc);
        return tpc;

    }

    public static void insertNewTrainersPerCourse() {
        TrainersPerCourseDao tpcd = new TrainersPerCourseDao();
        tpcd.insertTrainersPerCourse(createTrainerPerCourse());
    }

    public static void viewAllTrainersPerCourse() {
        TrainersPerCourseDao apcd = new TrainersPerCourseDao();
        List<TrainersPerCourse> allTrainersPerCourse = apcd.getAllTrainersPerCourse();
        CourseDao cd = new CourseDao();
        UserDao ud = new UserDao();
        int crid = 0;
        for (TrainersPerCourse trainersPerCourse : allTrainersPerCourse) {
            if (crid != trainersPerCourse.getCrid()) {
                crid = trainersPerCourse.getCrid();
                System.out.println("The Trainers for the Course "
                        + cd.getCourseById(trainersPerCourse.getCrid()).getName() + " are:");
                System.out.println(ud.getUserById(trainersPerCourse.getTrid()));
            } else {
                System.out.println(ud.getUserById(trainersPerCourse.getTrid()));
            }
        }
    }

    public static List<User> viewTrainersPerCourse(Course course) {
        TrainersPerCourseDao tpcd = new TrainersPerCourseDao();
        List<User> trainersPerCourse = tpcd.getTrainersPerCourseByCourse(course);
        int i = 1;
        System.out.println("The Trainers for the Course " + course.getName() + " are:");
        for (User trainer : trainersPerCourse) {
            System.out.println(i + " " + trainer);
            i++;
        }
        return trainersPerCourse;
    }

    public static void viewAllCoursesPerTrainer(User trainer) {
        TrainersPerCourseDao tpcd = new TrainersPerCourseDao();
        List<Course> courses = tpcd.getTrainersPerCourseByTrainer(trainer);
        if (courses.size() != 0) {
            courses.forEach((course) -> {
                System.out.println(course);
            });
        } else {
            System.out.println("No availiable courses!");
        }
    }

    public static void viewAllStudentsPerCoursePerTrainer(User trainer) {
        TrainersPerCourseDao tpcd = new TrainersPerCourseDao();
        Course course = new Course();
        CourseDao cd = new CourseDao();
        List<Course> trainerscourses = tpcd.getTrainersPerCourseByTrainer(trainer);
        trainerscourses.forEach((trainerscourse) -> {
            viewStudentsPerCourse(trainerscourse);
        });
    }

    public static List<StudentPerCoursePerAssignment> viewAllAssigmnetsPerStudentPerCourse(User trainer) {
        List<StudentPerCoursePerAssignment> spcpa = new ArrayList();
        TrainersPerCourseDao tpcd = new TrainersPerCourseDao();
        List<Course> trainerscourses = tpcd.getTrainersPerCourseByTrainer(trainer);
        StudentsPerCourseDao stpcd = new StudentsPerCourseDao();
        trainerscourses.stream().map((trainerscourse) -> stpcd.getStudentsPerCourseByCourse(trainerscourse)).forEachOrdered((studentsPerCourse) -> {
            studentsPerCourse.forEach((student) -> {
                spcpa.addAll(viewStudentPerCoursePerAssignmentViews(student));
            });
        });
        return spcpa;
    }

    public static void viewAllAssigmnetsPerStudentPerCourse2(User trainer) {

        StudentPerCoursePerAssignmentDTO spcah = new StudentPerCoursePerAssignmentDTO();
        List<StudentPerCoursePerAssignmentDTO> spcahList = new ArrayList();
        TrainersPerCourseDao tpcd = new TrainersPerCourseDao();
        List<Course> trainerscourses = tpcd.getTrainersPerCourseByTrainer(trainer);
        trainerscourses.stream().map((trainerscourse) -> {
            spcahList.addAll(spcah.getStudentCourseAssigmentByCourse(trainerscourse));
            return trainerscourse;
        }).forEachOrdered((_item) -> {
            spcahList.forEach((studentCourseAssignment) -> {
                System.out.println(studentCourseAssignment);
            });
        });
    }

    public static void updateTrainersPerCourse() {
        TrainersPerCourseDao tpcd = new TrainersPerCourseDao();
        Course course = courseSelector();
        System.out.println("Select the Trainer that you want to replace");
        User trainer = trainerSelectorPerCourse(course);
        System.out.println("Select the new Trainer");
        TrainersPerCourse tpc = tpcd.getTrainersPerCourseById(course.getId(), trainer.getId());
        TrainersPerCourse newtpc = new TrainersPerCourse(course.getId(), restTrainersSelectorPerCourse(course).getId());
        tpcd.updateTrainersPerCourse(tpc, newtpc);
    }

    public static void deleteTrainerPerCourse() {
        TrainersPerCourseDao tpcd = new TrainersPerCourseDao();
        Course course = courseSelector();
        System.out.println("Select the Trainer that you want to Remove");
        User trainer = trainerSelectorPerCourse(course);
        tpcd.deleteTrainersPerCourse(course.getId(), trainer.getId());
    }

}
