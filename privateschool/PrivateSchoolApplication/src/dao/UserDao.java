package dao;

import static dao.RoleDao.roleSelector;
import java.security.NoSuchAlgorithmException;
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
import model.User;
import static utils.DBUtils.closeConnection;
import static utils.DBUtils.createPreparedStatement;
import static utils.DBUtils.getconnection;
import static utils.DBUtils.queryExecute;
import static utils.DBUtils.queryExecuteUpdate;
import utils.GenericUtils;
import static utils.GenericUtils.giveInputFromUser;
import static utils.GenericUtils.giveIntFromUser;
import static utils.Password.generateHash;

public class UserDao {

    public static void setUserParams(PreparedStatement pst, User user) {
        try {
            pst.setString(1, user.getName());
            pst.setString(2, user.getPassword());
            pst.setString(3, user.getRole());
            pst.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public static User getUser(ResultSet rs) {
        User user = new User();
        try {
            while (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setRole(rs.getString("role"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(GenericUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }

    public static List<User> getUserList(ResultSet rs) {
        List<User> users = new ArrayList();
        try {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setRole(rs.getString("role"));
                users.add(user);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return users;
    }

    public static int userIdSelector(String role) {
        boolean check = false;
        int id = 0;
        List<User> users = new ArrayList();
        Set<Integer> usersids = new HashSet();
        UserDao ud = new UserDao();
        users.addAll(ud.getUsersByRole(role));
        if (users.size() != 0) {
            users.forEach((user) -> {
                usersids.add(user.getId());
            });
            do {
                viewAllUserByRole(role);
                id = giveIntFromUser("Select " + role + " by id:");
                if (usersids.contains(id)) {
                    check = true;
                } else {
                    System.out.println("Give valid option!!");
                }
            } while (check == false);
        } else {
            System.out.println("No users available");
        }
        return id;
    }

    public static int restUserIdSelector(List<User> remainingUsers) {
        boolean check = false;
        int id = 0;
        Set<Integer> remainingusersids = new HashSet();
        if (remainingUsers.size() != 0) {
            String role = remainingUsers.get(0).getRole();
            remainingUsers.forEach((remainingUser) -> {
                remainingusersids.add(remainingUser.getId());
            });
            do {
                viewRestUsers(remainingUsers);
                id = giveIntFromUser("Select " + role + " by id:");
                if (remainingusersids.contains(id)) {
                    check = true;
                } else {
                    System.out.println("Give valid option!!");
                }
            } while (check == false);

        } else {
            System.out.println("Give valid option!!");
        }
        return id;
    }

    public void insertUser(User user) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        String sql = "INSERT INTO users(name,password,role) VALUES(?,?,?)";
        PreparedStatement pst = createPreparedStatement(sql, conn);
        setUserParams(pst, user);
        closeConnection(conn);
    }

    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList();
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("SELECT*FROM users", conn);
        ResultSet rs = queryExecute(pst);
        allUsers.addAll(getUserList(rs));
        closeConnection(conn);
        return allUsers;
    }

    public User getUserById(int id) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("SELECT*FROM users where id=" + id, conn);
        ResultSet rs = queryExecute(pst);
        User user = getUser(rs);
        closeConnection(conn);
        return user;
    }

    public User getUserByNameAndPassword(String name, String password) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("SELECT*FROM users where (name='" + name + "'" + "and password='" + password + "');", conn);
        ResultSet rs = queryExecute(pst);
        User user = getUser(rs);
        closeConnection(conn);
        return user;
    }

    public List<User> getUsersByRole(String role) {
        List<User> allUsersByRole = new ArrayList();
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("SELECT*FROM users where role='" + role + "'", conn);
        ResultSet rs = queryExecute(pst);
        allUsersByRole.addAll(getUserList(rs));
        closeConnection(conn);
        return allUsersByRole;
    }

    public void deleteUser(int id) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("DELETE FROM users where id =" + id, conn);
        int ex = queryExecuteUpdate(pst);
        closeConnection(conn);
    }

    public void updateUser(User user) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        String sql = "UPDATE users SET name=?,password=?,role=? where id =" + user.getId();
        PreparedStatement pst = createPreparedStatement(sql, conn);
        setUserParams(pst, user);
        closeConnection(conn);
    }

    public static void insertNewUser() throws NoSuchAlgorithmException {
        UserDao ud = new UserDao();
        ud.insertUser(createUser());
    }

    public static User createUser() throws NoSuchAlgorithmException {
        User user = new User(giveInputFromUser("Give name: "), generateHash(giveInputFromUser("Set password: "), "MD5"), roleSelector());
        return user;
    }

    public static User createStudent() throws NoSuchAlgorithmException {
        User user = new User(giveInputFromUser("Give name: "), generateHash(giveInputFromUser("Set password: "), "MD5"), "Student");
        return user;
    }

    public static User createTrainer() throws NoSuchAlgorithmException {
        User user = new User(giveInputFromUser("Give name: "), generateHash(giveInputFromUser("Set password: "), "MD5"), "Trainer");
        return user;
    }

    public static void viewAllUserByRole(String role) {
        List<User> allUsersByRole = new ArrayList();
        UserDao ud = new UserDao();
        System.out.println("The " + role + "s are: ");
        allUsersByRole.addAll(ud.getUsersByRole(role));
        allUsersByRole.forEach((user) -> {
            System.out.println(user);
        });
    }

    public static void viewRestUsers(List<User> remainingUsers) {
        String role = remainingUsers.get(0).getRole();
        System.out.println("The Remaining " + role + "s are: ");
        remainingUsers.forEach((remainingUser) -> {
            System.out.println(remainingUser);
        });
    }

    public static void updateUser() throws NoSuchAlgorithmException {
        UserDao ud = new UserDao();
        System.out.println("Select user to Update: ");
        int id = userIdSelector(roleSelector());
        User user = createUser();
        user.setId(id);
        ud.updateUser(user);
    }

    public static void deleteUser() {
        System.out.println("Select User to Delete");
        UserDao ud = new UserDao();
        int id = userIdSelector(roleSelector());
        ud.deleteUser(id);
    }

}
