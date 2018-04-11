package org.mimos.stockservices.service.dto;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Objects;

import javax.persistence.Column;

import org.mimos.stockservices.domain.IndicePrice;

/**
 * A IndicePrice.
 */
public class IndicePriceDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Double open;

    private Double high;

    private Double low;

    private Double change;

    private Double percentageChange;

    private LocalDate publishDate;
    
    private Double price;

    private Integer volume;
    
    public IndicePriceDto(IndicePrice indicePrice) {
		this.id = indicePrice.getId();
		this.open = indicePrice.getOpen();
		this.high = indicePrice.getHigh();
		this.low = indicePrice.getChange();
		this.percentageChange = indicePrice.getPercentageChange();
		this.publishDate = indicePrice.getPublishDate();
		this.price = indicePrice.getPrice();
		this.volume = indicePrice.getVolume();
	}
    
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getOpen() {
        return open;
    }

    public IndicePriceDto open(Double open) {
        this.open = open;
        return this;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getHigh() {
        return high;
    }

    public IndicePriceDto high(Double high) {
        this.high = high;
        return this;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getLow() {
        return low;
    }

    public IndicePriceDto low(Double low) {
        this.low = low;
        return this;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getChange() {
        return change;
    }

    public IndicePriceDto change(Double change) {
        this.change = change;
        return this;
    }

    public void setChange(Double change) {
        this.change = change;
    }

    public Double getPercentageChange() {
        return percentageChange;
    }

    public IndicePriceDto percentageChange(Double percentageChange) {
        this.percentageChange = percentageChange;
        return this;
    }

    public void setPercentageChange(Double percentageChange) {
        this.percentageChange = percentageChange;
    }

    public long getPublishDate() {
        return java.sql.Date.valueOf(this.publishDate).getTime();
    }

    public IndicePriceDto publishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
        return this;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }
    
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getVolume() {
		return volume;
	}

	public void setVolume(Integer volume) {
		this.volume = volume;
	}


	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IndicePriceDto indicePrice = (IndicePriceDto) o;
        if (indicePrice.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), indicePrice.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IndicePrice{" +
            "id=" + getId() +
            ", open=" + getOpen() +
            ", high=" + getHigh() +
            ", low=" + getLow() +
            ", change=" + getChange() +
            ", percentage_change=" + getPercentageChange() +
            ", publishDate='" + getPublishDate() + "'" +
            "}";
    }
}
