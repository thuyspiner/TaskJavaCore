package view;

class Items {
     Product product;
     int quantity;

    public Items(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    
    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }


}

