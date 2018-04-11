package org.mimos.stockservices.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A SectorInfo.
 */
@Entity
@Table(name = "sector_info")
@Document(indexName = "sectorinfo")
public class SectorInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @OneToMany(mappedBy = "sectorInfo")
    @JsonIgnore
    private Set<SectorPrice> sectorPrices = new HashSet<>();

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

    public SectorInfo name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public SectorInfo code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<SectorPrice> getSectorPrices() {
        return sectorPrices;
    }

    public SectorInfo sectorPrices(Set<SectorPrice> sectorPrices) {
        this.sectorPrices = sectorPrices;
        return this;
    }

    public SectorInfo addSectorPrices(SectorPrice sectorPrice) {
        this.sectorPrices.add(sectorPrice);
        sectorPrice.setSectorInfo(this);
        return this;
    }

    public SectorInfo removeSectorPrices(SectorPrice sectorPrice) {
        this.sectorPrices.remove(sectorPrice);
        sectorPrice.setSectorInfo(null);
        return this;
    }

    public void setSectorPrices(Set<SectorPrice> sectorPrices) {
        this.sectorPrices = sectorPrices;
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
        SectorInfo sectorInfo = (SectorInfo) o;
        if (sectorInfo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sectorInfo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SectorInfo{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}
