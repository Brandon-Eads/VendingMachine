package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VendingMachine {
	
	private List<Slot> slotList = new ArrayList<>();
	
	
	
	public Slot getSlotNumberFromList(String slotToLookFor) {
		for (Slot n : slotList) {
			if (n.getSlotNumber().equalsIgnoreCase(slotToLookFor)) {
				return n;
			}
			
		}
		System.out.println("Slot number not found.");

		return null;
	}
	
	
	
	
	public void restockInventory() throws FileNotFoundException {
		File theFile   = new File("vendingmachine.csv");
		Scanner reader = new Scanner(theFile);
		
		
		while (reader.hasNextLine()) {
		String theLine = reader.nextLine();                    // Variable "theLine"
		String[] theValue = new String[] {} ; 
		theValue = theLine.split("\\|");
		
		if (theValue[3].contains("Chip")) {
			Chip newChip = new Chip(theValue[1], theValue[2]);
			Slot aSlot = new Slot(theValue[0], newChip);
			slotList.add(aSlot);
		}else if (theValue[3].contains("Candy")) {
			Candy newCandy = new Candy(theValue[1], theValue[2]);
			Slot aSlot = new Slot(theValue[0], newCandy);
			slotList.add(aSlot);
		}else if (theValue[3].contains("Drink")) {
			Beverage newBeverage = new Beverage(theValue[1], theValue[2]);
			Slot aSlot = new Slot(theValue[0], newBeverage);
			slotList.add(aSlot);
		}else if (theValue[3].contains("Gum")) {
			Gum newGum = new Gum(theValue[1], theValue[2]);
			Slot aSlot = new Slot(theValue[0], newGum);
			slotList.add(aSlot);
		}
		
		}
		}

        public List<Slot> getSlotList() {
		return slotList;
	}
		
		}
		


