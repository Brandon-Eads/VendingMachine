package com.techelevator;

public class Slot {

	
	private String slotNumber;
	private int slotStock;
	private Item itemName;                                                 // Line ".itemName = itemName" required this
	
	
	public Slot(String slotNumber, Item itemName) {
		this.slotNumber = slotNumber;
		this.itemName = itemName;
		slotStock = 5;
		
		
	}
	
	public Item getItemName() {
		return itemName;
	}

	public String getSlotNumber() {
		return slotNumber;
	}
	
	public void removeStock() {
		slotStock -= 1;
	}

	public int getSlotStock() {
		return slotStock;
	}
}
