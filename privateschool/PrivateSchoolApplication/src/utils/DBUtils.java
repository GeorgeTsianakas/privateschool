package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtils {

    public static Connection getconnection(String USERNAME, String PASSWORD, String dbname) {
        final String CONURL = "jdbc:mysql://localhost:3306/" + dbname + "?zeroDateTimeBehavior=CONVERT_TO_NULL"
                + "&useUnicode=true"
                + "&useJDBCCompliantTimezoneShift=true"
                + "&useLegacyDatetimeCode=false"
                + "&serverTimezone=UTC"
                + "&allowPublicKeyRetrieval=true"
                + "&useSSL=false&allowMultiQueries=true";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(CONURL, USERNAME, PASSWORD);
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return conn;
    }

    public static void closeConnection(Connection conn) {
        try {
            conn.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public static PreparedStatement createPreparedStatement(String sql, Connection conn) {
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return pst;
    }

    public static int queryExecuteUpdate(PreparedStatement pst) {
        int check = 0;
        try {
            check = pst.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return check;
    }

    public static ResultSet queryExecute(PreparedStatement pst) {
        ResultSet rs = null;
        try {
            rs = pst.executeQuery();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return rs;
    }

}
