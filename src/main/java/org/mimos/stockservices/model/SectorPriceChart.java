package org.mimos.stockservices.model;

import java.time.LocalDate;

public class SectorPriceChart {
	private long publishDate;
	private Double price;
	
	public SectorPriceChart(long publishDate, Double price) {
		super();
		this.publishDate = publishDate;
		this.price = price;
	}
	
	public long getPublishDate() {
		return publishDate;
	}
	
	public void setPublishDate(long publishDate) {
		this.publishDate = publishDate;
	}
	
	public Double getPrice() {
		return price;
	}
	
	public void setPrice(Double price) {
		this.price = price;
	}
	
	
}
