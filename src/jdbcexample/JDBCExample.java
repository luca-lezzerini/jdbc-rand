package jdbcexample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JDBCExample {

    public static void main(String[] args) {
        Connection con = null;
        Statement stmt = null;
        try {
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/jdbcrand",
                    "root",
                    "");
            stmt = con.createStatement();
            String sql = "select * from cassa";
            ResultSet res = stmt.executeQuery(sql);
            while (res.next()) {
                long id = res.getLong("id");
                String codice = res.getString("codice");
                long fk = res.getLong("scontrino_id");
                System.out.println("id = "+ id + "; codice = "+ codice + "; fk = " + fk);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt != null){
                try {
                    stmt.close();
                } catch (Exception e) {
                    // lasciato bianco intenzionalmente
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    // lasciata in bianco intenzionalmente
                }
            }
        }

    }

}
