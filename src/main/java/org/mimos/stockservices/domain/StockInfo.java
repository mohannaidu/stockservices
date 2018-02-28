package org.mimos.stockservices.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

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

    @OneToMany(mappedBy = "sector")
    @JsonIgnore
    private Set<UserPortfolio> portfolios = new HashSet<>();

    @OneToOne(mappedBy = "info")
    @JsonIgnore
    private StockPrice price;

    @ManyToOne
    private Sector sector;

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

    public Set<UserPortfolio> getPortfolios() {
        return portfolios;
    }

    public StockInfo portfolios(Set<UserPortfolio> userPortfolios) {
        this.portfolios = userPortfolios;
        return this;
    }

    public StockInfo addPortfolio(UserPortfolio userPortfolio) {
        this.portfolios.add(userPortfolio);
        userPortfolio.setSector(this);
        return this;
    }

    public StockInfo removePortfolio(UserPortfolio userPortfolio) {
        this.portfolios.remove(userPortfolio);
        userPortfolio.setSector(null);
        return this;
    }

    public void setPortfolios(Set<UserPortfolio> userPortfolios) {
        this.portfolios = userPortfolios;
    }

    public StockPrice getPrice() {
        return price;
    }

    public StockInfo price(StockPrice stockPrice) {
        this.price = stockPrice;
        return this;
    }

    public void setPrice(StockPrice stockPrice) {
        this.price = stockPrice;
    }

    public Sector getSector() {
        return sector;
    }

    public StockInfo sector(Sector sector) {
        this.sector = sector;
        return this;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
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
            "}";
    }
}
