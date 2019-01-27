package sample;

import java.sql.*;

public class SQLiteConnection {
    Statement smt;

    public static Connection Connector(){
        try{
             Class.forName("org.sqlite.JDBC");
             Connection conn = DriverManager.getConnection("jdbc:sqlite:yakitDB.sqlite;create=true");
           /* Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection("jdbc:derby:yakitDb;create=true");
            String tableName = "yakit_kayitlari";
*/
           String query = "CREATE TABLE IF NOT EXISTS yakit_kayitlari "+
                   "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                   "merkez TEXT, " +
                   "plaka TEXT, " +
                   "tarih TEXT, " +
                   "yakittipi TEXT, " +
                   "yakitmiktari DOUBLE, " +
                   "yakitTutari DOUBLE )";
             Statement smt = conn.createStatement();
             smt.execute(query);
             smt.close();
            return conn;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
