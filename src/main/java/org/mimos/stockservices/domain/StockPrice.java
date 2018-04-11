package org.mimos.stockservices.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A StockPrice.
 */
@Entity
@Table(name = "stock_price", uniqueConstraints=@UniqueConstraint(columnNames={"publish_date","stock_info_id"}))
@Document(indexName = "stockprice")
public class StockPrice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "opening")
    private Double opening;

    @Column(name = "closing")
    private Double closing;

    @Column(name = "volume")
    private Double volume;

    @Column(name = "high")
    private Double high;

    @Column(name = "low")
    private Double low;

    @Column(name = "publish_date")
    private LocalDate publishDate;

    @Column(name = "jhi_change")
    private Double change;

    @Column(name = "percentage_change")
    private Double percentageChange;

    @ManyToOne(optional = false)
    @NotNull
    private StockInfo stockInfo;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getOpening() {
        return opening;
    }

    public StockPrice opening(Double opening) {
        this.opening = opening;
        return this;
    }

    public void setOpening(Double opening) {
        this.opening = opening;
    }

    public Double getClosing() {
        return closing;
    }

    public StockPrice closing(Double closing) {
        this.closing = closing;
        return this;
    }

    public void setClosing(Double closing) {
        this.closing = closing;
    }

    public Double getVolume() {
        return volume;
    }

    public StockPrice volume(Double volume) {
        this.volume = volume;
        return this;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getHigh() {
        return high;
    }

    public StockPrice high(Double high) {
        this.high = high;
        return this;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getLow() {
        return low;
    }

    public StockPrice low(Double low) {
        this.low = low;
        return this;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public StockPrice publishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
        return this;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public Double getChange() {
        return change;
    }

    public StockPrice change(Double change) {
        this.change = change;
        return this;
    }

    public void setChange(Double change) {
        this.change = change;
    }

    public Double getPercentageChange() {
        return percentageChange;
    }

    public StockPrice percentageChange(Double percentageChange) {
        this.percentageChange = percentageChange;
        return this;
    }

    public void setPercentageChange(Double percentageChange) {
        this.percentageChange = percentageChange;
    }

    public StockInfo getStockInfo() {
        return stockInfo;
    }

    public StockPrice stockInfo(StockInfo stockInfo) {
        this.stockInfo = stockInfo;
        return this;
    }

    public void setStockInfo(StockInfo stockInfo) {
        this.stockInfo = stockInfo;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StockPrice stockPrice = (StockPrice) o;
        if (stockPrice.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stockPrice.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StockPrice{" +
            "id=" + getId() +
            ", opening=" + getOpening() +
            ", closing=" + getClosing() +
            ", volume=" + getVolume() +
            ", high=" + getHigh() +
            ", low=" + getLow() +
            ", publishDate='" + getPublishDate() + "'" +
            ", change=" + getChange() +
            ", percentageChange=" + getPercentageChange() +
            "}";
    }
}
