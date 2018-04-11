package org.mimos.stockservices.model;

public class StockInfoChart {
	
	
	private Double volume;	
	private Double price;
	private Double percentage;
	private long publishDate;
	
	public StockInfoChart(Double volume, Double price, Double percentage, long publishDate) {
		super();
		this.volume = volume;
		this.price = price;
		this.percentage = percentage;
		this.publishDate = publishDate;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	public long getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(long publishDate) {
		this.publishDate = publishDate;
	}
	
	
}
