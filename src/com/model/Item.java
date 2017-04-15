package com.model;

public class Item {
	private int item_id;
	private int quantity;
	private float total_price_paid;
	
	public Item(){
		
	}
	
	public Item(int id, int quantity, int price){
		this.setItem_id(id);
		this.setQuantity(quantity);
		this.setTotal_price_paid(price);
	}

	public int getItem_id() {
		return item_id;
	}

	public int getQuantity() {
		return quantity;
	}

	public float getTotal_price_paid() {
		return total_price_paid;
	}

	public void setItem_id(int item_id) {
		this.item_id = item_id;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setTotal_price_paid(float total_price_paid) {
		this.total_price_paid = total_price_paid;
	}
}
