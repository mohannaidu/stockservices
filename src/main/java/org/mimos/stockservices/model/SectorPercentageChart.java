package org.mimos.stockservices.model;

import java.time.LocalDate;

public class SectorPercentageChart {
	private long publishDate;
	private Double percentage;
	
	public SectorPercentageChart(long publishDate, Double percentage) {
		super();
		this.publishDate = publishDate;
		this.percentage = percentage;
	}
	public long getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(long publishDate) {
		this.publishDate = publishDate;
	}
	public Double getPercentage() {
		return percentage;
	}
	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}
	


}
