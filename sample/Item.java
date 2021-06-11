package sample;

public class Item {
    private int id = 0 , traderID = 0;
    private float price  = 0 ;
    private String name= "" , notes= "";

    public Item(int id, float price, int traderID, String name, String notes) {
        this.id = id;
        this.price = price;
        this.traderID = traderID;
        this.name = name;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getTraderID() {
        return traderID;
    }

    public void setTraderID(int traderID) {
        this.traderID = traderID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
