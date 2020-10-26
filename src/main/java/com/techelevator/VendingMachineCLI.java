package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**************************************************************************************************************************
*  This is your Vending Machine Command Line Interface (CLI) class
*
*  It is the main process for the Vending Machine
*
*  THIS is where most, if not all, of your Vending Machine interactions should be coded
*  
*  It is instantiated and invoked from the VendingMachineApp (main() application)
*  
*  Your code vending machine related code should be placed in here
***************************************************************************************************************************/
import com.techelevator.view.Menu; // Gain access to Menu class provided for the Capstone

public class VendingMachineCLI {

	// Main menu options defined as constants

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String MAIN_MENU_OPTION_HIDDEN = "sUPER sECRET oPTION dON'T cLICK";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE,
									MAIN_MENU_OPTION_EXIT, MAIN_MENU_OPTION_HIDDEN };
	private static final String PURCHASE_MENU_OPTION_ADD_CREDIT = "Add Credit";
	private static final String PURCHASE_MENU_OPTION_SELECT_ITEM = "Select Item";
	private static final String PURCHASE_MENU_OPTION_FINISH = "Finish";
	private static final String[] PURCHASE_MENU_OPTIONS = { PURCHASE_MENU_OPTION_ADD_CREDIT,
									PURCHASE_MENU_OPTION_SELECT_ITEM, PURCHASE_MENU_OPTION_FINISH };
	private double creditAdded = 0.0;
	private double totalProfit = 0.0;
	private File logFile;
	private PrintWriter transWriter;
	private VendingMachine vendOMatic = new VendingMachine();

	private Menu vendingMenu; // Menu object to be used by an instance of this class

	public VendingMachineCLI(Menu menu) { // Constructor - user will pas a menu for this class to use
		this.vendingMenu = menu; // Make the Menu the user object passed, our Menu
	}

	/**************************************************************************************************************************
	 * VendingMachineCLI main processing loop
	 * 
	 * Display the main menu and process option chosen
	 *
	 * It is invoked from the VendingMachineApp program
	 *
	 * THIS is where most, if not all, of your Vending Machine objects and
	 * interactions should be coded
	 *
	 * Methods should be defined following run() method and invoked from it
	 * 
	 * @throws FileNotFoundException
	 *
	 ***************************************************************************************************************************/

	public void run() throws FileNotFoundException {
		
		logFile = new File("log.txt");
		transWriter = new PrintWriter(logFile);

		vendOMatic.restockInventory();

		System.out.println("Welcome to the \n\n--- Vend-o-matic 8000 ---" + "\n\nWhat would you like to do today?\n");

		boolean shouldProcess = true; // Loop control variable

		while (shouldProcess) { // Loop until user indicates they want to exit

			String choice = (String) vendingMenu.getChoiceFromOptions(MAIN_MENU_OPTIONS); // Display menu and get choice

			switch (choice) { // Process based on user menu choice

			case MAIN_MENU_OPTION_DISPLAY_ITEMS:
				displayItems(); // invoke method to display items in Vending Machine
				break; // Exit switch statement

			case MAIN_MENU_OPTION_PURCHASE:
				purchaseMenu(); // invoke method to purchase items from Vending Machine
				break; // Exit switch statement

			case MAIN_MENU_OPTION_EXIT:
				endMethodProcessing(); // Invoke method to perform end of method processing
				shouldProcess = false; // Set variable to end loop
				break; // Exit switch statement
			
			case MAIN_MENU_OPTION_HIDDEN:
				writeToSalesReport();
				break;
			}
		}
		return; // End method and return to caller
	}

	/********************************************************************************************************
	 * Methods used to perform processing
	 * 
	 * @throws FileNotFoundException
	 ********************************************************************************************************/
	public void displayItems() throws FileNotFoundException { // static attribute used as method is not associated with
																// specific object instance
		File vendingItemMenu = new File("vendingmachine.csv");
		Scanner itemDisplay = new Scanner(vendingItemMenu);
		String theLine = "";
		while (itemDisplay.hasNextLine()) {
			theLine = itemDisplay.nextLine();
			//System.out.println(theLine.substring(0,2));
			System.out.println(theLine + vendOMatic.getSlotNumberFromList(theLine.substring(0,2)).getSlotStock());

			
		}
		itemDisplay.close();
	}

	public void purchaseMenu() throws FileNotFoundException { // static attribute used as method is not associated with
																// specific object instance
		boolean isShopping = true;

		while (isShopping) {
			
			
				String doubleFormatted = String.format("%.2f", creditAdded);
				creditAdded = Double.parseDouble(doubleFormatted);
			
			

			System.out.println("Current Credit: " + creditAdded + "\n");
			System.out.println("What would you like to do?\n");

			String choice = (String) vendingMenu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS);

			switch (choice) { // Process based on user menu choice

			case PURCHASE_MENU_OPTION_ADD_CREDIT:
				addCredit(); // invoke method to display items in Vending Machine
				break; // Exit switch statement

			case PURCHASE_MENU_OPTION_SELECT_ITEM:
				purchaseItems(); // invoke method to purchase items from Vending Machine
				break; // Exit switch statement

			case PURCHASE_MENU_OPTION_FINISH:
				returnChange();
				endMethodProcessing();// Invoke method to perform end of method processing
				isShopping = false; // Set variable to end loop
				break; // Exit switch statement
			}
		}
		return;

	}

	public double addCredit() {
		Scanner input = new Scanner(System.in);
		String inputCredit = "";
		double beforeInflux = creditAdded;
		System.out.println("How much would you like to deposit?"
				+ "\n(Note: This machine only accepts $1's, $2's, $5's, and $10's)");
		inputCredit = input.nextLine();
		if (inputCredit.equals("1") || inputCredit.equals("2") || inputCredit.equals("5") || inputCredit.equals("10")) {
			creditAdded += Double.parseDouble(inputCredit);
			writeToTransLog("FEED MONEY $" + beforeInflux + " $" + creditAdded);
		} else {
			System.out.println("This machine ONLY accepts $1's, $2's, $5's, and $10's. Try again.\n");
		}
		return creditAdded;
	}

	public void purchaseItems() {
		Scanner input = new Scanner(System.in);
		String inputSlot = "";
		System.out.println("What would you like?");
		inputSlot = input.nextLine();

		for (Slot slot : vendOMatic.getSlotList()) {
			if (slot.getSlotNumber().equalsIgnoreCase(inputSlot)) {
				String selectedItemPrice = slot.getItemName().getItemPrice();
				
				if (creditAdded - Double.parseDouble(selectedItemPrice) >= 0) {
					totalProfit += Double.parseDouble(selectedItemPrice);
					double beforeDeflux = creditAdded;
					creditAdded -= Double.parseDouble(selectedItemPrice); // subtracts selected item's price from any credit added
					slot.removeStock();
					
					if (inputSlot.contains("A") || inputSlot.contains("a")) {
						System.out.println("Crunch Crunch, Yum!");
					}if (inputSlot.contains("B") || inputSlot.contains("b")) {
						System.out.println("Munch Munch, Yum!");
					}if (inputSlot.contains("C") || inputSlot.contains("c")) {
						System.out.println("Glug Glug, Yum!");
					}if (inputSlot.contains("D") || inputSlot.contains("d")) {
					   System.out.println("Chew Chew, Yum!"); 
				   }
					writeToTransLog(slot.getItemName().getItemName() + " " + slot.getSlotNumber() + " $" + beforeDeflux + " $" + creditAdded);

				}else {
					System.out.println("Not enough credit to make purchase. Try again.\n");
				}
											

			}
		}

		}
	
	public void returnChange() {
		int quarterAmt = 0;
		int dimeAmt = 0;
		int nickelAmt = 0;
		Double changeReturned = creditAdded;
		String stringCredit = String.format("%.0f", (creditAdded*100));
		int bigCredit = Integer.parseInt(stringCredit);
		while (bigCredit >= 5) {
			if (bigCredit >= 25) {
				bigCredit -= 25;
				quarterAmt++;
			}else if (bigCredit >= 10 && bigCredit < 25) {
				bigCredit -= 10;
				dimeAmt++;
			}else if (bigCredit >= 5 && bigCredit < 10) {
				bigCredit -= 5;
				nickelAmt++;
			}
		}
		System.out.println("\nChange due: " + changeReturned
				+ "\n\nTing ting ting!\n"
				+ "\nYou received " + quarterAmt + " quarter(s), " + dimeAmt + " dime(s), and " + nickelAmt + " nickel(s)!");
		creditAdded = 0.0;
		writeToTransLog("GIVE CHANGE $" + changeReturned + " $" + creditAdded);


		
	}
	
	public void writeToTransLog(String actionToWrite) {
		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
		String dateTimeString = dateTime.format(dateTimeFormatter);
		
		transWriter.println(dateTimeString + " " + actionToWrite);
		
		
	}
	
	public void writeToSalesReport() throws FileNotFoundException {
		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM_dd_yyyy_hh_mm_ss_a");
		String dateTimeString = dateTime.format(dateTimeFormatter);
		File salesReport = new File("sales_report_" + dateTimeString + ".txt");
		PrintWriter salesWriter = new PrintWriter(salesReport);
		
		for (Slot n : vendOMatic.getSlotList()) {
			int amtSold = 5 - n.getSlotStock();
			salesWriter.println(n.getItemName().getItemName() + " | " + amtSold);
		}
		String formattedProfit = String.format("%.2f", totalProfit);
		salesWriter.println("Total Sales: $" + formattedProfit);
		salesWriter.close();
	}

	public void endMethodProcessing() { // static attribute used as method is not associated with specific object
										// instance
										// Any processing that needs to be done before method ends
		
		transWriter.close();
		
	}
}
