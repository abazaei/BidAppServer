package com.example.auctionapplication.search;

import com.example.auctionapplicationIntermed.AuctionItem;

public class Contain implements Predicate<AuctionItem>{

	private String text;
	
	public Contain(String s){
		this.text = s;
	}

	@Override
	public boolean test(AuctionItem auctioni) {
		
		return auctioni.getName().contains(text);
	}
	
}
