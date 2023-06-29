import javax.swing.plaf.nimbus.State;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class RWFiles {

	/*
	 * leo y almaceno los archivos dentro de una carpeta, y
	 * reordeno los archivos segun el orden de creacion
	 * de las tablas en la base de datos
	 */
    public static void readFolder(final File folder, File[] files) {
        File[] unorderedFiles = folder.listFiles();
        files[0] = unorderedFiles[13];
        files[1] = unorderedFiles[11];
        files[2] = unorderedFiles[0];
        files[3] = unorderedFiles[9];
        files[4] = unorderedFiles[1];
        files[5] = unorderedFiles[2];
        files[6] = unorderedFiles[3];
        files[7] = unorderedFiles[4];
        files[8] = unorderedFiles[5];
        files[9] = unorderedFiles[6];
        files[10] = unorderedFiles[7];
        files[11] = unorderedFiles[8];
        files[12] = unorderedFiles[10];
        files[13] = unorderedFiles[12];
    }


    public static void createTables(Connection conn) throws SQLException {
        Statement statement = conn.createStatement();
        String sql1 = "CREATE SCHEMA IF NOT EXISTS F1;";
        String sql2 = "USE F1;";
        statement.executeUpdate(sql1);
        statement.executeUpdate(sql2);

        String status = "CREATE TABLE IF NOT EXISTS status(statusId int PRIMARY KEY,status varchar(50));";
        String seasons = "CREATE TABLE IF NOT EXISTS seasons(year int PRIMARY KEY, url varchar(200));";
        String circuits = "CREATE TABLE IF NOT EXISTS circuits(circuitId int PRIMARY KEY,circuitRef varchar(50), name varchar(50), location varchar(50), country varchar(50), lat int, lng int, alt int, url varchar(200));";
        String races = "CREATE TABLE IF NOT EXISTS races(raceId int PRIMARY KEY, year int, FOREIGN KEY (year) REFERENCES seasons (year), round int, circuitId int, FOREIGN KEY (circuitId) REFERENCES circuits (circuitId), name varchar(50), date date, time time(3), url varchar(200), fp1_date date, fp1_time time(3), fp2_date date, fp2_time time(3), fp3_date date, fp3_time time(3), quali_date date, quali_time time(3), sprint_date date, sprint_time time(3));";
        String constructors = "CREATE TABLE IF NOT EXISTS constructors(constructorId int PRIMARY KEY, constructorRef varchar(50), name varchar(50), nationality varchar(50), url varchar(200));";
        String constructor_results = "CREATE TABLE IF NOT EXISTS constructor_results(constructorResultsId int PRIMARY KEY, raceId int, FOREIGN KEY (raceId) REFERENCES races (raceId), constructorId int, FOREIGN KEY (constructorId) REFERENCES constructors (constructorId), points int, status varchar(5));";
        String constructor_standings = "CREATE TABLE IF NOT EXISTS constructor_standings(constructorStandingsId int PRIMARY KEY, raceId int, FOREIGN KEY (raceId) REFERENCES races (raceId), constructorId int, FOREIGN KEY (constructorId) REFERENCES constructors (constructorId), points int, position int, positionText varchar(4), wins int);";
        String drivers = "CREATE TABLE IF NOT EXISTS drivers(driverId int PRIMARY KEY, driverRef varchar(50), number int, code varchar(4), forename varchar(50), surname varchar(50), dob date, nationality varchar(50), url varchar(200));";
        String driver_standings = "CREATE TABLE IF NOT EXISTS driver_standings(driverStandingsId int PRIMARY KEY, raceId int, FOREIGN KEY (raceId) REFERENCES races (raceId), driverId int, FOREIGN KEY (driverId) REFERENCES drivers (driverId), points int, position int, positionText varchar(50), wins int);";
        String lap_times = "CREATE TABLE IF NOT EXISTS lap_times(lap_timesId int PRIMARY KEY AUTO_INCREMENT, raceId int, FOREIGN KEY (raceId) REFERENCES races (raceId), driverId int, FOREIGN KEY (driverId) REFERENCES drivers (driverId), lap int, position int, time time(3), milliseconds int);";
        String pit_stops = "CREATE TABLE IF NOT EXISTS pit_stops(pit_stopsId int PRIMARY KEY AUTO_INCREMENT, raceId int, FOREIGN KEY (raceId) REFERENCES races (raceId), driverId int, FOREIGN KEY (driverId) REFERENCES drivers (driverId), stop int, lap int, FOREIGN KEY (lap) REFERENCES lap_times (lap_timesId), time time(3), duration time(3), milliseconds int);";
        String qualifying = "CREATE TABLE IF NOT EXISTS qualifying(qualifyId int PRIMARY KEY, raceId int, FOREIGN KEY (raceId) REFERENCES races (raceId), driverId int, FOREIGN KEY (driverId) REFERENCES drivers (driverId), constructorId int, FOREIGN KEY (constructorId) REFERENCES constructors (constructorId), number int, position int, q1 time(3), q2 time(3), q3 time(3));";
        String results = "CREATE TABLE IF NOT EXISTS results(resultId int PRIMARY KEY, raceId int, FOREIGN KEY (raceId) REFERENCES races (raceId), driverId int, FOREIGN KEY (driverId) REFERENCES drivers (driverId), constructorId int, FOREIGN KEY (constructorId) REFERENCES constructors (constructorId), number int, grid int, position int, positionText varchar(50), positionOrder int, points int, laps int, time varchar(15), milliseconds int, fastestLap int, `rank` int, FOREIGN KEY (fastestLap) REFERENCES lap_times (lap_timesId), fastestLapTime time(3), fastestLapSpeed varchar(50), statusId int, FOREIGN KEY (statusId) REFERENCES status (statusId));";
        String sprint_results = "CREATE TABLE IF NOT EXISTS sprint_results(sprint_resultsId int PRIMARY KEY AUTO_INCREMENT, resultId int, FOREIGN KEY (resultId) REFERENCES results (resultId), raceId int, FOREIGN KEY (raceId) REFERENCES races (raceId), driverId int, FOREIGN KEY (driverId) REFERENCES drivers (driverId), constructorId int, FOREIGN KEY (constructorId) REFERENCES constructors (constructorId), number int, grid int, position int, positionText varchar(50), positionOrder int, points int, laps int, time varchar(15), milliseconds int, fastestLap int, FOREIGN KEY (fastestLap) REFERENCES lap_times (lap_timesId), fastestLapTime time(3), statusId int, FOREIGN KEY (statusId) REFERENCES status (statusId));";

        statement.executeUpdate(status);
        statement.executeUpdate(seasons);
        statement.executeUpdate(circuits);
        statement.executeUpdate(races);
        statement.executeUpdate(constructors);
        statement.executeUpdate(constructor_results);
        statement.executeUpdate(constructor_standings);
        statement.executeUpdate(drivers);
        statement.executeUpdate(driver_standings);
        statement.executeUpdate(lap_times);
        statement.executeUpdate(pit_stops);
        statement.executeUpdate(qualifying);
        statement.executeUpdate(results);
        statement.executeUpdate(sprint_results);
    }


    public static void insertValues(Connection conn, File file) throws SQLException, FileNotFoundException {
        Statement statement = conn.createStatement();
        statement.executeUpdate("USE F1;");
        String columns = "";
        String tableName = file.getName().replace(".csv", "");
        Scanner scanner = new Scanner(file);
        if (scanner.hasNext()) {
            columns = scanner.next();
        }
        int i = 0;
        while (scanner.hasNext()) {
            if (i == 0) {
                scanner.nextLine();
                i++;
            }
            StringBuilder insert = new StringBuilder();
            String field = scanner.nextLine().replace("'", "´").replace("\"", "'").replace("\\N", "NULL");
            insert.append("INSERT INTO " + tableName + " (" + columns.replace("rank", "`rank`") + ") VALUES (" + field + ");");
            System.out.println(insert);
            statement.executeUpdate(insert.toString());
        }
        scanner.close();
    }
}
