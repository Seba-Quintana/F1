import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    public static Connection DBConn() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println("Error al registrar el driver de MySQL: " + ex);
        }
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","root");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return conn;
    }

    public static void setup(Connection conn) {
        Statement statement = null;
        try {
            statement = conn.createStatement();
            String sql1 = "CREATE SCHEMA IF NOT EXISTS F1;";
            statement.executeUpdate(sql1);
            String sql2 = "USE F1;";
            statement.executeUpdate(sql2);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
