package org.mimos.stockservices.domain;


import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A SectorPrice.
 */
@Entity
@Table(name = "sector_price")
@Document(indexName = "sectorprice")
public class SectorPrice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "publish_date")
    private LocalDate publishDate;

    @Column(name = "opening")
    private Double opening;

    @Column(name = "closing")
    private Double closing;

    @Column(name = "high")
    private Double high;

    @Column(name = "low")
    private Double low;

    @Column(name = "jhi_change")
    private Double change;

    @Column(name = "percentage_change")
    private Double percentageChange;

    @ManyToOne
    private SectorInfo sectorInfo;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDate getPublishDate() {
        return publishDate;
    }

    public SectorPrice publishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
        return this;
    }
    
    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }
    
    public Double getOpening() {
        return opening;
    }

    public SectorPrice opening(Double opening) {
        this.opening = opening;
        return this;
    }

    public void setOpening(Double opening) {
        this.opening = opening;
    }

    public Double getClosing() {
        return closing;
    }

    public SectorPrice closing(Double closing) {
        this.closing = closing;
        return this;
    }

    public void setClosing(Double closing) {
        this.closing = closing;
    }

    public Double getHigh() {
        return high;
    }

    public SectorPrice high(Double high) {
        this.high = high;
        return this;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getLow() {
        return low;
    }

    public SectorPrice low(Double low) {
        this.low = low;
        return this;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getChange() {
        return change;
    }

    public SectorPrice change(Double change) {
        this.change = change;
        return this;
    }

    public void setChange(Double change) {
        this.change = change;
    }

    public Double getPercentageChange() {
        return percentageChange;
    }

    public SectorPrice percentageChange(Double percentageChange) {
        this.percentageChange = percentageChange;
        return this;
    }

    public void setPercentageChange(Double percentageChange) {
        this.percentageChange = percentageChange;
    }

    public SectorInfo getSectorInfo() {
        return sectorInfo;
    }

    public SectorPrice sectorInfo(SectorInfo sectorInfo) {
        this.sectorInfo = sectorInfo;
        return this;
    }

    public void setSectorInfo(SectorInfo sectorInfo) {
        this.sectorInfo = sectorInfo;
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
        SectorPrice sectorPrice = (SectorPrice) o;
        if (sectorPrice.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sectorPrice.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SectorPrice{" +
            "id=" + getId() +
            ", opening=" + getOpening() +
            ", closing=" + getClosing() +
            ", high=" + getHigh() +
            ", low=" + getLow() +
            ", change=" + getChange() +
            ", percentageChange=" + getPercentageChange() +
            ", publishDate='" + getPublishDate() + "'" +
            "}";
    }
}
