package org.mimos.stockservices.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A StockInfo.
 */
@Entity
@Table(name = "stock_info")
@Document(indexName = "stockinfo")
public class StockInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @NotNull
    @Column(name = "symbol", nullable = false)
    private String symbol;

    @ManyToOne
    private SectorInfo sectorInfo;

    @OneToMany(mappedBy = "stockInfo")
    @JsonIgnore
    private Set<StockPrice> prices = new HashSet<>();

    @ManyToOne
    private MarketInfo marketInfo;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public StockInfo name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public StockInfo code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSymbol() {
        return symbol;
    }

    public StockInfo symbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public SectorInfo getSectorInfo() {
        return sectorInfo;
    }

    public StockInfo sectorInfo(SectorInfo sectorInfo) {
        this.sectorInfo = sectorInfo;
        return this;
    }

    public void setSectorInfo(SectorInfo sectorInfo) {
        this.sectorInfo = sectorInfo;
    }

    public Set<StockPrice> getPrices() {
        return prices;
    }

    public StockInfo prices(Set<StockPrice> stockPrices) {
        this.prices = stockPrices;
        return this;
    }

    public StockInfo addPrices(StockPrice stockPrice) {
        this.prices.add(stockPrice);
        stockPrice.setStockInfo(this);
        return this;
    }

    public StockInfo removePrices(StockPrice stockPrice) {
        this.prices.remove(stockPrice);
        stockPrice.setStockInfo(null);
        return this;
    }

    public void setPrices(Set<StockPrice> stockPrices) {
        this.prices = stockPrices;
    }

    public MarketInfo getMarketInfo() {
        return marketInfo;
    }

    public StockInfo marketInfo(MarketInfo marketInfo) {
        this.marketInfo = marketInfo;
        return this;
    }

    public void setMarketInfo(MarketInfo marketInfo) {
        this.marketInfo = marketInfo;
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
        StockInfo stockInfo = (StockInfo) o;
        if (stockInfo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stockInfo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StockInfo{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", symbol='" + getSymbol() + "'" +
            "}";
    }
}
