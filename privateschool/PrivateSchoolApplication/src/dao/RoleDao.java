package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Role;

import static utils.DBUtils.closeConnection;
import static utils.DBUtils.createPreparedStatement;
import static utils.DBUtils.getconnection;
import static utils.DBUtils.queryExecute;
import static utils.DBUtils.queryExecuteUpdate;
import static utils.GenericUtils.giveInputFromUser;
import static utils.GenericUtils.giveIntFromUser;

public class RoleDao {

    public static void setRoleParams(PreparedStatement pst, Role role) {
        try {
            pst.setString(1, role.getRole());
            pst.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public static List<Role> getRolesList(ResultSet rs) {
        List<Role> Roles = new ArrayList();
        try {
            while (rs.next()) {
                Role role = new Role();
                role.setRole(rs.getString("role"));
                Roles.add(role);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return Roles;
    }

    public static String roleSelector() {
        boolean check = false;
        int selection = 0;
        String role = "";
        List<Role> roles = new ArrayList();
        roles.addAll(viewAllRoles());
        do {
            selection = giveIntFromUser("Select role by number: ");
            if (selection > 0 && selection <= roles.size()) {
                role = roles.get(selection - 1).getRole();
                check = true;
            } else {
                System.out.println("Give valid option!!");
            }
        } while (check == false);
        return role;
    }

    public Role createRole() {
        Role role = new Role(giveInputFromUser("Give new role: "));
        return role;
    }

    public void insertRole(Role role) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        String sql = "INSERT INTO Roles(role) VALUES(?)";
        PreparedStatement pst = createPreparedStatement(sql, conn);
        setRoleParams(pst, role);
        closeConnection(conn);
    }

    public List<Role> getAllRoles() {
        List<Role> allRoless = new ArrayList();
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("SELECT*FROM roles", conn);
        ResultSet rs = queryExecute(pst);
        allRoless.addAll(getRolesList(rs));
        closeConnection(conn);
        return allRoless;
    }

    public void deleteRole(String role) {
        Connection conn = getconnection("root", "123456789", "bootcamp");
        PreparedStatement pst = createPreparedStatement("DELETE FROM Roles where id ='" + role + "'", conn);
        int ex = queryExecuteUpdate(pst);
        closeConnection(conn);
    }

    public static List<Role> viewAllRoles() {
        int i = 1;
        List<Role> roles = new ArrayList();
        RoleDao rd = new RoleDao();
        roles.addAll(rd.getAllRoles());
        for (Role role : roles) {
            System.out.println(i + " " + role);
            i++;
        }
        return roles;
    }

}
