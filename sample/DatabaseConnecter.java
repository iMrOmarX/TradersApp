package sample;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseConnecter {
    private String connectionURL = "jdbc:mysql://localhost:3306/traders" ;
    private String userName = "root";
    private String userPassword = "9yKZpv%PE3GH";
    private Connection conn;
    private Statement stmt;

    public DatabaseConnecter() throws SQLException {
        connectToDatabase();
    }
    public void connectToDatabase() throws SQLException {
        // Step 1: Construct a database 'Connection' object called 'conn'
        conn = DriverManager.getConnection( connectionURL,
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

    public int getNumberOfItems() throws SQLException{
        String strCount = "SELECT COUNT(id) AS NumberOfItems FROM item";

        ResultSet rset = stmt.executeQuery(strCount);

        while(rset.next()) {
            return rset.getInt(1);
        }
        return  0;
    }

    public void saveNewItem(Item newItem) throws SQLException{
        String strInsert = "INSERT INTO item(id ,name , price , notes,trader_id) VALUES (?, ? , ? ,?,?)";

        PreparedStatement preparedStmt = conn.prepareStatement(strInsert);
        preparedStmt.setInt(1, newItem.getId());
        preparedStmt.setString(2 , newItem.getName());
        preparedStmt.setFloat(3,newItem.getPrice());
        preparedStmt.setString(4, newItem.getNotes());
        preparedStmt.setInt(5,newItem.getTraderID());

        preparedStmt.execute();
    }

    public ArrayList<Item> getItemsOfTrader(Trader trader ) throws SQLException{

        String strSelect ="SELECT * FROM item WHERE trader_id = " + trader.getId();

        ResultSet rset = stmt.executeQuery(strSelect);

        ArrayList<Item> items = new ArrayList<>();

        int trader_id = trader.getId();
        while(rset.next()) {
            int id = rset.getInt("id");
            float price = rset.getFloat("price");
            String name = rset.getString("name");
            String notes = rset.getString("notes");

            items.add(new Item(id,  price , trader_id , name , notes));
        }

        return items;
    }

    public ArrayList<Item> getItemsOfTrader(Trader trader , String itemName) throws SQLException{

        String strSelect ="SELECT * FROM item WHERE trader_id = " + trader.getId() +
                ((itemName.isBlank())? "" : " AND name LIKE " +  "'%" + itemName + "%'");

        ResultSet rset = stmt.executeQuery(strSelect);

        ArrayList<Item> items = new ArrayList<>();

        int trader_id = trader.getId();
        while(rset.next()) {
            int id = rset.getInt("id");
            float price = rset.getFloat("price");
            String name = rset.getString("name");
            String notes = rset.getString("notes");

            items.add(new Item(id,  price , trader_id , name , notes));
        }

        return items;
    }

    public ArrayList<Item> getItems (String itemName ) throws SQLException{

        //String strSelect ="SELECT id(item) , name(item), price(item) , notes(item) , name(traders) FROM item  "+
         //       ((itemName.isBlank())? "" : "WHERE name LIKE " +  "'%" + itemName + "%'");


        String strSelect = "select item.* , traders.name as trader_name from item INNER JOIN traders on item.trader_id = traders.id " +
                ((itemName.isBlank())? "" : "WHERE item.name LIKE " +  "'%" + itemName + "%'");

        ResultSet rset = stmt.executeQuery(strSelect);

        ArrayList<Item> items = new ArrayList<>();


        ArrayList<String> traders_names = new ArrayList<>();

        while(rset.next()) {
            int id = rset.getInt("id");
            float price = rset.getFloat("price");
            String name = rset.getString("name");
            String notes = rset.getString("notes");

            int trader_id = rset.getInt("trader_id");


            Item newItem = new Item(id,  price , trader_id , name , notes) ;
            String trader_name = rset.getString("trader_name");

            newItem.setTraderName(trader_name);
            items.add(newItem);

        }
        return items;
    }

}
