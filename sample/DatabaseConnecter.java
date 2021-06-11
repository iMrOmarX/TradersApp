package sample;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseConnecter {
    private String connectionURL = "jdbc:mysql://localhost:3306/traders" ;
    private String userName = "root";
    private String userPassword = "9yKZpv%PE3GH";

    private Statement stmt;

    public DatabaseConnecter() throws SQLException {
        connectToDatabase();
    }
    public void connectToDatabase() throws SQLException {
        // Step 1: Construct a database 'Connection' object called 'conn'
        Connection conn = DriverManager.getConnection( connectionURL,
                userName, userPassword);   // For MySQL only
        stmt = conn.createStatement();
    }

    public Trader getTraderById(int id)  throws SQLException {
        String strSelect = "SELECT * FROM traders WHERE id = " + id ;
        ResultSet rset =stmt.executeQuery(strSelect);

        while(rset.next()) {
            String name = rset.getString("name");
            String phoneNumber = rset.getString("phone_number");
            String address = rset.getString("address");
            String notes = rset.getString("notes");


            return new Trader(id ,phoneNumber , name ,address ,notes);
        }
        return null;
    }

    public ArrayList<String> getTradersNames() throws SQLException {
        String strSelect = "SELECT name FROM traders ";
        ResultSet rset =stmt.executeQuery(strSelect);

        ArrayList<String> names = new ArrayList<>();

        while(rset.next()) {
            String name = rset.getString("name");
            names.add(name);
        }

        return names;
    }

    public ArrayList<Trader> getTradersByName( String searchedName)  throws SQLException {
        String strSelect = "SELECT * FROM traders WHERE name LIKE " + "'%" +  searchedName + "%'";
        ResultSet rset =stmt.executeQuery(strSelect);

        ArrayList<Trader> traders = new ArrayList<Trader>();
        while(rset.next()) {
            int id = rset.getInt("id");
            String name = rset.getString("name");
            String phoneNumber = rset.getString("phone_number");
            String address = rset.getString("address");
            String notes = rset.getString("notes");


            traders.add(new Trader(id ,phoneNumber , name ,notes ,address)) ;
        }

        return traders;
    }


    public void saveNewTrader(Trader newTrader) throws SQLException{

        String strInsert = "INSERT INTO traders VALUES(" + newTrader.getId()  +',' + "'" +
                ((newTrader.getName() == null) ? "null" : newTrader.getName())+  "'"  + ','  + "'" +
                ((newTrader.getPhoneNumber() == null) ? "null" : newTrader.getPhoneNumber()) +  "'" + ',' + "'" +
                        ((newTrader.getAddress() == null) ? "null" : newTrader.getAddress()) + "'" + ',' + "'" +
                                ((newTrader.getNotes() == null) ? "null" : newTrader.getNotes()) + "'" + ")";

        System.out.println(strInsert);
        stmt.execute(strInsert);
    }

}
