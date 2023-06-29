import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.sql.Connection;
import java.sql.SQLException;


public class Main {

    public static void main(String[] args) {
        Connection conn = DBConnection.DBConn();
        DBConnection.setup(conn);
        final File folder = new File("src\\archivos");
        File[] files = new File[14];
        RWFiles.readFolder(folder, files);
        try {
            RWFiles.createTables(conn);
            for (File file : files) {
                RWFiles.insertValues(conn, file);
            }
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
