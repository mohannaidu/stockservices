package org.mimos.stockservices.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Sector.
 */
@Entity
@Table(name = "sector")
@Document(indexName = "sector")
public class Sector implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "sector")
    @JsonIgnore
    private Set<StockInfo> infos = new HashSet<>();

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

    public Sector name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<StockInfo> getInfos() {
        return infos;
    }

    public Sector infos(Set<StockInfo> stockInfos) {
        this.infos = stockInfos;
        return this;
    }

    public Sector addInfo(StockInfo stockInfo) {
        this.infos.add(stockInfo);
        stockInfo.setSector(this);
        return this;
    }

    public Sector removeInfo(StockInfo stockInfo) {
        this.infos.remove(stockInfo);
        stockInfo.setSector(null);
        return this;
    }

    public void setInfos(Set<StockInfo> stockInfos) {
        this.infos = stockInfos;
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
        Sector sector = (Sector) o;
        if (sector.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sector.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Sector{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
