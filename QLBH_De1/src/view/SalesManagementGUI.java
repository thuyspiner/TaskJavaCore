package view;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SalesManagementGUI extends JFrame {
    private JTextField txtItemCode, txtItemName, txtItemPrice, txtCustomerID, txtCustomerName, txtCustomerAddress, txtCustomerPhone;
    private JComboBox<String> cmbItemGroup;
    private JButton btnAddItem, btnAddCustomer, btnAddToCart, btnPrintInvoice, btnSortByName, btnSortByItem;
    private JTable tblItemList, tblCustomerList, tblCartList;
    private DefaultTableModel itemTableModel, customerTableModel, cartTableModel;
    
    

    private ArrayList<Product> itemList;
    private ArrayList<Customer> customerList;
    private ArrayList<Transaction> cartList;

//    private int itemCodeCounter = 1000;
//    private int customerIDCounter = 10000;
	private Product product;
	private Customer customer;
	private JTextArea textAreaOutput;	

    public SalesManagementGUI() {
        setTitle("Quản Lý Bán Hàng Trong Siêu Thị");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        
        
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        
     // Khởi tạo textAreaOutput và thêm vào giao diện
        textAreaOutput = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textAreaOutput);
        panel.add(scrollPane);

        itemList = new ArrayList<>();
        customerList = new ArrayList<>();
        cartList = new ArrayList<>();

        // Giao diện nhập thông tin mặt hàng
        JPanel itemPanel = new JPanel(new GridLayout(5, 2));
        itemPanel.setBorder(BorderFactory.createTitledBorder("Thông tin Mặt hàng"));

        JLabel lblItemCode = new JLabel("Mã hàng:");
        txtItemCode = new JTextField();
        txtItemCode.setEditable(false);
        itemPanel.add(lblItemCode);
        itemPanel.add(txtItemCode);

        btnAddItem = new JButton("Thêm Mặt hàng");
        itemPanel.add(btnAddItem);
        panel.add(itemPanel);
        
        // Giao diện nhập thông tin khách hàng
        JPanel customerPanel = new JPanel(new GridLayout(5, 2));
        customerPanel.setBorder(BorderFactory.createTitledBorder("Thông tin Khách hàng"));

        JLabel lblCustomerID = new JLabel("Mã KH:");
        txtCustomerID = new JTextField();
        txtCustomerID.setEditable(false);
        customerPanel.add(lblCustomerID);
        customerPanel.add(txtCustomerID);

        btnAddCustomer = new JButton("Thêm Khách hàng");
        customerPanel.add(btnAddCustomer);
        panel.add(customerPanel);

        // Giao diện nhập danh sách mua hàng
        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBorder(BorderFactory.createTitledBorder("Danh sách mua hàng"));

        tblItemList = new JTable(new DefaultTableModel(new Object[]{"Mã hàng", "Tên hàng", "Nhóm hàng", "Giá bán"}, 0));
        JScrollPane itemScrollPane = new JScrollPane(tblItemList);
        cartPanel.add(itemScrollPane, BorderLayout.CENTER);

        btnAddToCart = new JButton("Thêm vào giỏ hàng");
        cartPanel.add(btnAddToCart, BorderLayout.SOUTH);
        panel.add(cartPanel);

        // Giao diện hiển thị danh sách khách hàng
        JPanel customerListPanel = new JPanel(new BorderLayout());
        customerListPanel.setBorder(BorderFactory.createTitledBorder("Danh sách Khách hàng"));

        tblCustomerList = new JTable(new DefaultTableModel(new Object[]{"Mã KH", "Họ tên", "Địa chỉ", "Số ĐT"}, 0));
        JScrollPane customerScrollPane = new JScrollPane(tblCustomerList);
        customerListPanel.add(customerScrollPane, BorderLayout.CENTER);

        panel.add(customerListPanel);

        // Giao diện hiển thị danh sách mua hàng
        JPanel cartListPanel = new JPanel(new BorderLayout());
        cartListPanel.setBorder(BorderFactory.createTitledBorder("Danh sách mua hàng"));

        tblCartList = new JTable(new DefaultTableModel(new Object[]{"Mã KH", "Mã hàng", "Số lượng"}, 0));
        JScrollPane cartScrollPane = new JScrollPane(tblCartList);
        cartListPanel.add(cartScrollPane, BorderLayout.CENTER);

        panel.add(cartListPanel);
        
     // Giao diện sắp xếp danh sách đơn hàng theo tên khách hàng và theo tên mặt hàng
        JPanel sortPanel = new JPanel(new GridLayout(1, 2));
        sortPanel.setBorder(BorderFactory.createTitledBorder("Sắp xếp danh sách đơn hàng"));

        btnSortByName = new JButton("Sắp xếp theo tên KH");
       
        sortPanel.add(btnSortByName);

        btnSortByItem = new JButton("Sắp xếp theo tên mặt hàng");
       
        sortPanel.add(btnSortByItem);

        panel.add(sortPanel);
        

        // Giao diện lập hóa đơn chi tiết cho mỗi khách hàng
        JPanel printInvoicePanel = new JPanel();
        printInvoicePanel.setBorder(BorderFactory.createTitledBorder("Lập hóa đơn chi tiết"));
        
        btnPrintInvoice = new JButton("Lập hóa đơn");
        
        printInvoicePanel.add(btnPrintInvoice);
        panel.add(printInvoicePanel);
        
        getContentPane().add(panel);
        setVisible(true);
        
        // Button và sự kiện xử lý
        btnAddItem.addActionListener(e -> {         
        	addItem();	
        });
        btnAddCustomer.addActionListener(e -> {           
        	addCustomer();
        });
        btnAddToCart.addActionListener(e -> {
        	addToCart();
        });
        btnSortByName.addActionListener(e -> {
        	sortByName();
        	
        });
        btnSortByItem.addActionListener(e -> {
        	sortByItem();
        });
        btnPrintInvoice.addActionListener(e -> {
        	printInvoice();
        	
        });
        
    }

    // Thực thi hàm chính
    private void addItem() {
        String idStr = JOptionPane.showInputDialog("Nhập ID:");
        String name = JOptionPane.showInputDialog("Nhập tên Sản phẩm:");
        String category = JOptionPane.showInputDialog("Nhập loại sản phẩm:");
        String priceStr = JOptionPane.showInputDialog("Nhập giá sản phẩm:");

        try {
            int id = Integer.parseInt(idStr);
            if (id < 1000 || id > 9999) {
                JOptionPane.showMessageDialog(this, "Mã hàng phải là một số nguyên có 4 chữ số");
                return;
            }
            double price = Double.parseDouble(priceStr);
            product = new Product(id, name, category, price);
            addProductToFile();
            itemList.add(product);
            addToItemTable();
            textAreaOutput.append("Đã thêm sản phẩm: " + product.getName() + "\n");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Nhập không hợp lệ!");
        }
    }

    private void addProductToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("MH.TXT", true))) {
            writer.println(product.getId() + "," + product.getName() + "," + product.getCategory() + "," + product.getPrice());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm file: " + e.getMessage());
        }
    }

	// hiển thị thông tin mặt hàng lên bảng
    private void addToItemTable() {
        DefaultTableModel model = (DefaultTableModel) tblItemList.getModel();
        model.addRow(new Object[]{product.getId(), product.getName(), product.getCategory(), product.getPrice()});
    }
    

    // thêm khách hàng vào danh sách và hiển thị lên bảng
    private void addCustomer() {
    	 String idStr = JOptionPane.showInputDialog("Nhập ID khách hàng:");
         String name = JOptionPane.showInputDialog("Nhập tên khách hàng:");
         String address = JOptionPane.showInputDialog("Nhập địa chỉ:");
         String phoneNumber = JOptionPane.showInputDialog("Nhập số điện thoại:");
        
         try {
             int id = Integer.parseInt(idStr);
             if(id<10000 || id >99999) {
            	 JOptionPane.showMessageDialog(this, "Khách hàng phải là một số nguyên có 5 chữ số");
            	 return;
             }
             customer = new Customer(id, name, address, phoneNumber);
             
             customerList.add(customer);
             addCustomerToFile();	
             addToCustomerTable(customer);
             textAreaOutput.append("Đã thêm khách hàng: " + customer.getName() + "\n");
         } catch (NumberFormatException e) {
             JOptionPane.showMessageDialog(this, "Nhập không hợp lệ!");
         }
    }	
    private void addCustomerToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("KH.TXT", true))) {
            writer.println(customer.getId() + "," + customer.getName() + "," + customer.getAddress() + "," + customer.getPhoneNumber());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm file: " + e.getMessage());
        }
    }
   

    
    private void addToCustomerTable(Customer customer) {
        DefaultTableModel model = (DefaultTableModel) tblCustomerList.getModel();
        model.addRow(new Object[]{customer.getId(), customer.getName(), customer.getAddress(), customer.getPhoneNumber()});
    }

   //Thêm vào bảng danh sách mua hàng của từng khách hàng
    private void addToCart() {
    	
    	    int selectedCustomerIndex = tblCustomerList.getSelectedRow();
    	    int selectedItemIndex = tblItemList.getSelectedRow();

    	    if (selectedCustomerIndex == -1 || selectedItemIndex == -1) {
    	        JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng và mặt hàng trước khi thêm vào giỏ hàng!");
    	        return;
    	    }

    	    Customer customer = customerList.get(selectedCustomerIndex);
    	    Product item = itemList.get(selectedItemIndex);

    	    // Kiểm tra số lượng loại mặt hàng đã mua
    	    int count = 0;
    	    for (Transaction transaction : cartList) {
    	        if (transaction.getCustomer().equals(customer)) {
    	            for (Items cartItem : transaction.getItems()) {
    	                if (cartItem.getProduct().getId() != item.getId()) {
    	                    count++;
    	                }
    	            }
    	        }
    	    }
    	    if (count >= 10) {
    	        JOptionPane.showMessageDialog(this, "Mỗi khách hàng chỉ được mua tối đa 10 loại mặt hàng mỗi lần.");
    	        return;
    	    }

    	    // Kiểm tra xem mặt hàng đã có trong giỏ hàng của khách hàng chưa
    	    for (Transaction transaction : cartList) {
    	        if (transaction.getCustomer().equals(customer)) {
    	            for (Items cartItem : transaction.getItems()) {
    	                if (cartItem.getProduct().getId() == item.getId()) {
    	                    JOptionPane.showMessageDialog(this, "Mặt hàng đã có trong giỏ hàng của khách hàng.");
    	                    return;
    	                }
    	            }
    	        }
    	    }

    	    int quantity = Integer.parseInt(JOptionPane.showInputDialog(this, "Nhập số lượng mua:"));

    	    // Tạo một đối tượng Items mới và thêm vào giỏ hàng
    	    Items cartItem = new Items(item, quantity);
    	    Transaction transaction = getTransactionForCustomer(customer);
    	    transaction.addItem(cartItem);

    	    // Hiển thị thông tin mua hàng lên bảng
    	    addToCartTable();
    	    addCartToFile();  
    }
    private Transaction getTransactionForCustomer(Customer customer) {
        // Tìm kiếm trong danh sách giao dịch xem đã có giao dịch cho khách hàng này chưa
        for (Transaction transaction : cartList) {
            if (transaction.getCustomer().equals(customer)) {
                return transaction;
            }
        }
        // Nếu không tìm thấy tạo một giao dịch mới cho khách hàng này
        Transaction newTransaction = new Transaction(customer, null);
        cartList.add(newTransaction);
        return newTransaction;
    }

    // hiển thị thông tin mua hàng lên bảng
    private void addToCartTable() {
        DefaultTableModel model = (DefaultTableModel) tblCartList.getModel();
        model.setRowCount(0); // Xóa các hàng cũ trước khi thêm mới

        for (Transaction transaction : cartList) {
            Customer customer = transaction.getCustomer();
            for (Items cartItem : transaction.getItems()) {
                Product product = cartItem.getProduct();
                int quantity = cartItem.getQuantity();
                model.addRow(new Object[]{customer.getId(), product.getId(), quantity});
            }
        }
    }

    
    private void addCartToFile() {
    	try (PrintWriter writer = new PrintWriter(new FileWriter("QLBH.TXT", true))) {
            for (Transaction transaction : cartList) {
                Customer customer = transaction.getCustomer();
                for (Items cartItem : transaction.getItems()) {
                    Product product = cartItem.getProduct();
                    int quantity = cartItem.getQuantity();
                    writer.println(customer.getId() + "," + product.getId() + "," + quantity);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu file QLBH.TXT: " + e.getMessage());
        }
    }
 // sort theo tên khách hàng
    private void sortByName() {
        Collections.sort(cartList, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction t1, Transaction t2) {
                return t1.getCustomer().getName().compareTo(t2.getCustomer().getName());
            }
        });
        addToCartTable();
    }
 // sort theo tên mặt hàng
    private void sortByItem() {
        Collections.sort(cartList, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction t1, Transaction t2) {
                String item1 = t1.getItems().get(0).getProduct().getName();
                String item2 = t2.getItems().get(0).getProduct().getName();
                return item1.compareTo(item2);
            }
        });
        addToCartTable();
    }
    private void printInvoice() {
        // Lặp qua danh sách giao dịch
        for (Transaction transaction : cartList) {
            Customer customer = transaction.getCustomer();
            textAreaOutput.append("Hóa đơn cho khách hàng: " + customer.getName() + "\n");
            textAreaOutput.append("------------------------------\n");
            textAreaOutput.append("Mã hàng\tTên hàng\tSố lượng\n");
            for (Items cartItem : transaction.getItems()) {
                Product product = cartItem.getProduct();
                int quantity = cartItem.getQuantity();
                textAreaOutput.append(product.getId() + "\t" + product.getName() + "\t" + quantity + "\n");
            }
            textAreaOutput.append("------------------------------\n");
        }
    }
  

    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SalesManagementGUI().setVisible(true);
            }
        });
    }
}
