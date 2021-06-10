package sample;

import java.util.ArrayList;
import java.util.Objects;

public class Trader {
    private int id , phoneNumber;
    private String name , notes , address ;

    private ArrayList<Item> sellableItems;

    public Trader(int id, int phoneNumber, String name, String notes, String address) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.notes = notes;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Trader{" +
                "id=" + id +
                ", phoneNumber=" + phoneNumber +
                ", name='" + name + '\'' +
                ", notes='" + notes + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trader trader = (Trader) o;
        return id == trader.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
