package org.mimos.stockservices.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A IndicePrice.
 */
@Entity
@Table(name = "indice_price")
@Document(indexName = "indiceprice")
public class IndicePrice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_open")
    private Double open;

    @Column(name = "high")
    private Double high;

    @Column(name = "low")
    private Double low;

    @Column(name = "jhi_change")
    private Double change;

    @Column(name = "publish_date")
    private LocalDate publishDate;

    @Column(name = "percentage_change")
    private Double percentageChange;

    @Column(name = "price")
    private Double price;

    @Column(name = "volume")
    private Integer volume;

    @ManyToOne(optional = false)
    @NotNull
    private IndiceInfo indiceInfo;

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

    public IndicePrice open(Double open) {
        this.open = open;
        return this;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getHigh() {
        return high;
    }

    public IndicePrice high(Double high) {
        this.high = high;
        return this;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getLow() {
        return low;
    }

    public IndicePrice low(Double low) {
        this.low = low;
        return this;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getChange() {
        return change;
    }

    public IndicePrice change(Double change) {
        this.change = change;
        return this;
    }

    public void setChange(Double change) {
        this.change = change;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public IndicePrice publishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
        return this;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public Double getPercentageChange() {
        return percentageChange;
    }

    public IndicePrice percentageChange(Double percentageChange) {
        this.percentageChange = percentageChange;
        return this;
    }

    public void setPercentageChange(Double percentageChange) {
        this.percentageChange = percentageChange;
    }

    public Double getPrice() {
        return price;
    }

    public IndicePrice price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getVolume() {
        return volume;
    }

    public IndicePrice volume(Integer volume) {
        this.volume = volume;
        return this;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public IndiceInfo getIndiceInfo() {
        return indiceInfo;
    }

    public IndicePrice indiceInfo(IndiceInfo indiceInfo) {
        this.indiceInfo = indiceInfo;
        return this;
    }

    public void setIndiceInfo(IndiceInfo indiceInfo) {
        this.indiceInfo = indiceInfo;
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
        IndicePrice indicePrice = (IndicePrice) o;
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
            ", publishDate='" + getPublishDate() + "'" +
            ", percentageChange=" + getPercentageChange() +
            ", price=" + getPrice() +
            ", volume=" + getVolume() +
            "}";
    }
}
