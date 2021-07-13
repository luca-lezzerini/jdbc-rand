package jdbcexample;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class JDBCGenerazioneDati {

    public static void main(String[] args) {
        Connection con = null;
        Statement stmt = null;
        PreparedStatement prep = null;
        try {
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/scarsefourj",
                    "dbuser",
                    "password");
            String sql;

            // inizializzo lo statement
            stmt = con.createStatement();

            // cancellare tutto (5)
            sql = "delete from riga_scontrino";
            int righe = stmt.executeUpdate(sql);
            System.out.println("SQL: " + sql + " righe = " + righe);
            sql = "delete from scontrino";
            righe = stmt.executeUpdate(sql);
            System.out.println("SQL: " + sql + " righe = " + righe);
            sql = "delete from posizione_scaffale";
            righe = stmt.executeUpdate(sql);
            System.out.println("SQL: " + sql + " righe = " + righe);
            sql = "delete from prodotto_sconti";
            righe = stmt.executeUpdate(sql);
            System.out.println("SQL: " + sql + " righe = " + righe);
            sql = "delete from prodotto";
            righe = stmt.executeUpdate(sql);
            System.out.println("SQL: " + sql + " righe = " + righe);

            // creare 10 prodotti (2+1+1 -> 4)
            for (int i = 0; i < 11; i++) {
                sql = "insert into prodotto (id, codice,"
                        + " descrizione, ean, lotto_riordino,"
                        + " prezzo, scorta_min_magazzino_default,"
                        + " scorta_min_scaffale_default)"
                        + " values (?,?,?,?,?,?,?,?)";
                prep = con.prepareStatement(sql);
                // associo i parametri allo statement preparato
                int n = 1;
                prep.setLong(n++, i);
                prep.setString(n++, "codice" + i);
                prep.setString(n++, "descrizione" + i);
                prep.setString(n++, "ean" + i);
                prep.setInt(n++, i);
                prep.setDouble(n++, i * 3.14);
                prep.setInt(n++, i);
                prep.setInt(n++, i);

                // eseguo lo statement preparato
                prep.executeUpdate();
            }

            // rileggo i prodotti (1+1)
            sql = "select * from prodotto";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.println("ID prodotto = " + rs.getLong("id"));
            }

            // creare 5 scontrini (2+1+1 -> 4)
            for (int i = 0; i < 5; i++) {
                sql = "insert into scontrino (id, numero,"
                        + " time_stamp, totale)"
                        + " values (?,?,?,?)";
                prep = con.prepareStatement(sql);
                // associo i parametri allo statement preparato
                int n = 1;
                prep.setLong(n++, i);
                prep.setInt(n++, i);
                prep.setDate(n++, Date.valueOf(LocalDate.now()));
                prep.setDouble(n++, i * 2.7182);

                // eseguo lo statement preparato
                prep.executeUpdate();
            }

            // per ogni scontrino, creare 3 righe con relativo prodotto (6)
            // i -> id scontrino
            // j -> lo uso per calcolare id prodotto (non tutti i prodotti sono usati)
            // l'id del prodotto da associare si calcola come doppio dell'id scontrino + j
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 3; j++) {

                    // inserisco la riga associandola al prodotto e allo scontrino
                    sql = "insert into riga_scontrino (id, quantita, prodotto_id,"
                            + " scontrino_id)"
                            + " values (?,?,?,?)";
                    prep = con.prepareStatement(sql);
                    // associo i parametri allo statement preparato
                    int n = 1;
                    long idProdotto = (i * 2) + j;
                    long idScontrino = i;
                    prep.setLong(n++, i * 10 + j); // chiave primaria univoca della riga
                    prep.setInt(n++, i * 10);
                    prep.setLong(n++, idProdotto); // id Prodotto
                    prep.setLong(n++, idScontrino); // id Scontrino
                    System.out.println("Creo riga per prodotto = " + idProdotto + " e scontrino = " + idScontrino);

                    // eseguo lo statement preparato
                    prep.executeUpdate();
                }
            }

            // stampare gli scontrini, ciascuno con quantità, descrizione e prezzo 
            sql = "select s.numero, r.quantita, p.descrizione, p.prezzo from scontrino as s"
                    + " left join riga_scontrino as r on s.id = r.scontrino_id"
                    + " left join prodotto as p on p.id = r.prodotto_id";

            // uso for invece che il solito while
            for (ResultSet rs2 = stmt.executeQuery(sql); rs2.next();) {
                int numero = rs2.getInt("numero");
                int qta = rs2.getInt("quantita");
                String descrizione = rs2.getString("descrizione");
                Double prezzo = rs2.getDouble("prezzo");
                System.out.println("Scontrino: " + numero + "\t" + qta + "\t" + descrizione + "\t" + prezzo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
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
