package view;

import java.util.ArrayList;

class Transaction {
    Customer customer;
    ArrayList<Items> items;

    public Transaction(Customer customer, ArrayList<Items> items) {
        this.customer = customer;
        this.items = items != null ? items : new ArrayList<>();
    }

    public void addItem(Items item) {
        items.add(item);
    }

    public void removeItem(Items item) {
        items.remove(item);
    }

    public ArrayList<Items> getItems() {
        return items;
    }

    public Customer getCustomer() {
        return customer;
    }
}

	


