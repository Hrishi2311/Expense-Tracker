package com.expensetracker;

import java.time.LocalDate;

public class Transaction {
	
	String category;    
	String type;
	double amount;
    LocalDate date;

    
    public Transaction(String type, String category, double amount, LocalDate date) {
		super();
		this.type = type;
		this.category = category;
		this.amount = amount;
		this.date = date;
	}


	@Override
	public String toString() {
		return "Transaction [type=" + type + ", category=" + category + ", amount=" + amount + ", date=" + date + "]";
	}
    
    
	
	
	
	
	
	
	


}
