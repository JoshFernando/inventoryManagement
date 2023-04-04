import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class InventoryManagementSystem {

	public abstract class Item {
		String name, brand;
		double price = 0.00;
		int quantity = 0;

		public Item (String name, double price, int quantity) {
			this.name = name;
			this.price = price;
			this.quantity = quantity;
		}

		public String getItemName() {
			return name;
		}

		public double getItemPrice() {
			return price;
		}

		public int getItemQuantity() {
			return quantity;
		}

		public void reduceItemQuantity(int quantity) {
			this.quantity = this.quantity - quantity;
		}

		public void increaseItemQuantity(int quantity) {
			this.quantity = this.quantity + quantity;
		}
	}
	public class Product extends Item {
		private String brand;

		public Product(String name, double price, int quantity, String brand) {
			super(name, price, quantity);
			this.brand = brand;
		}
		public String getProductBrand() {
			return brand;
		}
	}

	public class InventoryManager {

		// create arraylist of objects
		ArrayList<Product > itemArray = new ArrayList<Product >();

		// fucntion to add object to arraylist
		public void addItem(Product  item) {
			itemArray.add(item);
		}

		// function to check if object exists in array
		public double doesProductExists(String n) {
			String itemName;
			double allowedPrice = 0.00;
			for (int i = 0; i < itemArray.size(); i++) {
				itemName = itemArray.get(i).getItemName();
				if (itemName.toLowerCase().trim().equals(n.toLowerCase().trim())) {
					allowedPrice = itemArray.get(i).getItemPrice();
					break;
				} else
					allowedPrice = 0.00;
			}
			return allowedPrice;
		}

		// deletes object in arraylist
		public void deleteProduct(String name) {
			String itemName;
			for (int i = 0; i < itemArray.size(); i++) {
				itemName = itemArray.get(i).getItemName();
				if (itemName.toLowerCase().trim().equals(name.toLowerCase().trim())) {
					itemArray.remove(i);
				}
			}
		}

		public void modifyProduct(String modify, String name, double price, int quantity, String brand) {
			Product  item;
			String itemName;
			for (int i = 0; i < itemArray.size(); i++) {
				itemName = itemArray.get(i).getItemName();
				if (itemName.toLowerCase().trim().equals(modify.toLowerCase().trim())) {
					item = new Product (name, price, quantity, brand);
					itemArray.remove(i);
					addItem(item);
				}
			}
		}

		public void subtractProductQuantity(String name, int quantity) {
			String itemName;
			for (int i = 0; i < itemArray.size(); i++) {
				itemName = itemArray.get(i).getItemName();
				if (itemName.toLowerCase().trim().equals(name.toLowerCase().trim())) {// always use equals function to
																						// check strings
					if (itemArray.get(i).getItemQuantity() >= quantity) {
						itemArray.get(i).reduceItemQuantity(quantity);
					} else
						System.out.println("Negetive quantity");
				}
			}
		}

		public void addProductQuantity(String n, int quantity) {
			String itemName;
			for (int i = 0; i < itemArray.size(); i++) {
				itemName = itemArray.get(i).getItemName();
				if (itemName.equals(n)) {
					itemArray.get(i).increaseItemQuantity(quantity);
				}
			}
		}

		public String displayInventory() {
			StringBuilder sb = new StringBuilder();
			int counter = 0;
			for (Product  item : itemArray) {
				counter++;
				sb.append("ID: " + counter + " [PRODUCT: " + item.getItemName() + ", BRAND: " + item.getProductBrand()
					+", PRICE: " + item.getItemPrice()
						+ ", QUANTITY: " + item.getItemQuantity() + "]\n");
			}
			return sb.toString();
		}
	}

	public class Admin {
		private final String USERNAME = "a";
		private final String PASSWORD = "1234";
		Product product;
		String order, selectedProduct;
		int quantity = 0;
		double payment, allowedPrice, totalSales;
		InventoryManager manager = new InventoryManager();

		private String getAdminAction() {
			String userOption = JOptionPane
					.showInputDialog("(1) Manage Inventory\n(2) Start Selling\n(3) View Inventory\n(4) Logout");
			return userOption;
		}

		private void addProduct() {
			String exit = "1";
			do {
				try {
					String name = JOptionPane.showInputDialog("Enter product's name: ");
					double price = Double.parseDouble(JOptionPane.showInputDialog("Enter the product's's price: "));
					int productQuantity = Integer.parseInt(JOptionPane.showInputDialog("Enter the product's's quantity: "));
					String brand = JOptionPane.showInputDialog("Enter product's brand: ");
					product = new Product (name.trim(), price, productQuantity, brand);
					manager.addItem(product);
					exit = JOptionPane.showInputDialog(name.trim() + " added successfully.\n(1) Continue\n(2) Exit");
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(null, "Invalid input. Try again.");
				}
			} while (exit.trim().equals("1"));
		}

		private void deleteProduct() {
			String deletedItem, exit = "1";
			double checkedItem = 0.00;
			do {
				do {
					deletedItem = JOptionPane
							.showInputDialog(manager.displayInventory() + "\nEnter the name of item to be removed: ");
					checkedItem = manager.doesProductExists(deletedItem);
					if (deletedItem == null) {
						break;
					}
					if (checkedItem == 0.00) {
						JOptionPane.showMessageDialog(null, deletedItem + " is not in the inventory.");
					}
				} while (checkedItem == 0.00);
				if (deletedItem == null) {
						break;
				}
				manager.deleteProduct(deletedItem);
				exit = JOptionPane.showInputDialog(manager.displayInventory() + "\n" + deletedItem.trim()
						+ " deleted successfully.\n1 = Continue\n2 = Exit");
			} while (exit.trim().equals("1"));
		}

		private void editProduct() {
			String modifiedItem, exit = "1";
			double checkedItem = 0.00;
			do {
				do {

					modifiedItem = JOptionPane.showInputDialog(
							manager.displayInventory() + "\nEnter the name of the item to be modified: ");
					checkedItem = manager.doesProductExists(modifiedItem);
					if (modifiedItem == null) {
						break;
					}
					if (checkedItem == 0.00) {
						JOptionPane.showMessageDialog(null, modifiedItem + " is not in the inventory.");
					}
				} while (checkedItem == 0);
				if (modifiedItem == null) {
						break;
				}
				String name = JOptionPane
						.showInputDialog("Selected: " + modifiedItem + "\n" + "Enter modifications. \nNew name: ");

				double price = Double.parseDouble(JOptionPane.showInputDialog("New price"));

				int q = Integer.parseInt(JOptionPane.showInputDialog("New quantity: "));
				String brand = JOptionPane.showInputDialog("New brand: ");
				manager.modifyProduct(modifiedItem, name, price, q, brand);

				exit = JOptionPane
						.showInputDialog(modifiedItem.trim() + " updated successfully.\n1 = Continue\n2 = Exit");
			} while (exit.trim().equals("1"));
		}

		private Double getUserSelection() {
			double calulatedPrice = 0.00;
			do {
				selectedProduct = JOptionPane
						.showInputDialog(manager.displayInventory() + "\nEnter the name of item you want to buy: ");
				calulatedPrice = manager.doesProductExists(selectedProduct);
				if (calulatedPrice == 0.00) {
					JOptionPane.showMessageDialog(null, selectedProduct + " entered is not available.");
				}
			} while (calulatedPrice == 0.00);
			return calulatedPrice;
		}

		private boolean isAdminCredentialsValid(String username, String password) {
			boolean valid = false;
			if (username.trim().equals(USERNAME) && password.trim().equals(PASSWORD)) {
				valid = true;
			} else {
				JOptionPane.showMessageDialog(null, "Invalid username/password. Try again.");
			}
			return valid;
		}

		private void letAdminLogin() {
			String username = null;
			String password = null;
			do {
				username = JOptionPane.showInputDialog("Enter username: ");
				password = JOptionPane.showInputDialog("Enter password: ");
			} while (!isAdminCredentialsValid(username, password));
		}

		private void sellProduct() {
			allowedPrice = getUserSelection();
			try {
				quantity = Integer.parseInt(JOptionPane.showInputDialog("Enter quantity: "));
				allowedPrice = allowedPrice * quantity;
				JOptionPane.showMessageDialog(null, "AllowedPrice: " + allowedPrice);
				do {
					payment = Double.parseDouble(JOptionPane.showInputDialog("Enter payment: "));
					if (payment < allowedPrice)
						JOptionPane.showMessageDialog(null, "Insufficient payment!");
				} while (payment < allowedPrice);
				payment -= allowedPrice;
				totalSales += allowedPrice;
				manager.subtractProductQuantity(selectedProduct, quantity);
				if (payment > 0) {
					JOptionPane.showMessageDialog(null, "\nHere is your change: " + payment);
					payment = 0;
				}
			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(null, "Invalid input. Try again.");
			}
		}

		private void showAdminOptions() {
			boolean exit = false;
			do {
				String manageOption = JOptionPane
						.showInputDialog("(1) Add Item\n(2) Delete Item\n(3) Modifyitem\n(4) Exit");
				if (manageOption.toLowerCase().trim().equals("1")) {
					addProduct();
				} else if (manageOption.toLowerCase().trim().equals("2")) {
					deleteProduct();
				} else if (manageOption.toLowerCase().trim().equals("3")) {
					editProduct();
				} else if (manageOption.toLowerCase().trim().equals("4")) {
					exit = true;
				} else {
					JOptionPane.showMessageDialog(null, "Invalid input. Try again.");
				}
			} while (!exit);
		}

		public void operation() {
			boolean closeApplication = false, logout = false;
			while (!closeApplication) {
				String login = JOptionPane.showInputDialog("(1) Login as Admin.\n(2) Exit application");
				logout = false;
				if (login.trim().equals("1")) {
					letAdminLogin();
					do {
						String adminOption = getAdminAction();
						if (adminOption.trim().equals("1")) {
							showAdminOptions();
						}

						else if (adminOption.trim().equals("2")) {
							sellProduct();
						}

						else if (adminOption.trim().equals("3")) {
							JOptionPane.showMessageDialog(null,
									"Total Sales: " + totalSales + "\n" + manager.displayInventory());
						}

						else if (adminOption.trim().equals("4")) {
							logout = true;
						}

						else {
							JOptionPane.showMessageDialog(null, "Invalid input. Try again.");
						}
					} while (!logout);
				}

				else if (login.trim().equals("2")) {
					closeApplication = true;
					System.exit(0);
				}

				else {
					JOptionPane.showMessageDialog(null, "Invalid input.");
				}
			}
		}
	}

	public void operation() {
		Admin admin = new Admin();
		admin.operation();
	}

	public static void main(String[] args) {
		InventoryManagementSystem inventoryManagementSystem = new InventoryManagementSystem();
		inventoryManagementSystem.operation();
	}
}