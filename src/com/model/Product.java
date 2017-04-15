package com.model;



public class Product {
	private int store_id;
	private int receipt_id;
	private int customer_id;
	private Item[] items;
	
	public Product(){
		
	}
	
	public Product(int store_id, int receipt_id, int cust_id,Item[] item){
		this.setStore_id(store_id);
		this.setReceipt_id(receipt_id);
		this.setCustomer_id(cust_id);
		this.setItems(item);
	}
	


	public Item[] getItems(){
		return items;
	}
	
	public int getStore_id() {
		return store_id;
	}

	public int getReceipt_id() {
		return receipt_id;
	}

	public int getCustomer_id() {
		return customer_id;
	}

	public void setStore_id(int store_id) {
		this.store_id = store_id;
	}

	public void setReceipt_id(int receipt_id) {
		this.receipt_id = receipt_id;
	}

	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}

	public void setItems(Item[] item) {
		this.items = item;
	}
}
