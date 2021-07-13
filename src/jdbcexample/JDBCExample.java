package jdbcexample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JDBCExample {

    public static void main(String[] args) {
        Connection con = null;
        Statement stmt = null;
        List<String> lista =  new ArrayList<>();
        try {
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/jdbcrand",
                    "root",
                    "");
            Statement canc = con.createStatement();
            String delSql = "delete from Persons";
            int cancellati = canc.executeUpdate(delSql);
            System.out.println("Rimossi " + cancellati);
            Statement ins = con.createStatement();
            String insSql = "insert into Persons values (1, \"Giuseppe\",\"Garibaldi\",\"Via Quarto, 1000\",\"Palermo\" )";
            boolean bins = ins.execute(insSql);
            Statement ins1 = con.createStatement();
            String insSql1 = "insert into Persons values (1, \"Camillo\",\"Cavour\",\"Via Torino, 1\",\"Torino\" )";
            int numIns = ins.executeUpdate(insSql1);
            System.out.println("Puff! E` andata!" + bins+" aggiunti "+ numIns);
            stmt = con.createStatement();
            String sql = "select * from persons";
            ResultSet res = stmt.executeQuery(sql);
            while (res.next()) {
                System.out.println("Siamo qui");
                long id = res.getLong("PersonId");
                String fn = res.getString("FirstName");
                String ln = res.getString("LastName");
                System.out.println("id = "+ id + "; Nome = "+ fn + "; Cognome = " + ln);
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
