package org.mimos.stockservices.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A UserPortfolio.
 */
@Entity
@Table(name = "user_portfolio")
@Document(indexName = "userportfolio")
public class UserPortfolio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "username", nullable = false)
    private String username;

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

    public String getUsername() {
        return username;
    }

    public UserPortfolio username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public StockInfo getStockInfo() {
        return stockInfo;
    }

    public UserPortfolio stockInfo(StockInfo stockInfo) {
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
        UserPortfolio userPortfolio = (UserPortfolio) o;
        if (userPortfolio.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userPortfolio.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserPortfolio{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            "}";
    }
}
