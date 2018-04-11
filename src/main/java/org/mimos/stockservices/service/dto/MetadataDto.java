package org.mimos.stockservices.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

/**
 * A Metadata.
 */
//@Document(indexName = "md", type = "metadata")
public class MetadataDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private String docid;

    private String content;

    private String url;

    private String title;

    private Integer status;

    private String author;

    private LocalDateTime publishdate;

    private String description;
    
    private String type;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocid() {
        return docid;
    }

    public MetadataDto docid(String docid) {
        this.docid = docid;
        return this;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getContent() {
        return content;
    }

    public MetadataDto content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public MetadataDto url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public MetadataDto title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getStatus() {
        return status;
    }

    public MetadataDto status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAuthor() {
        return author;
    }

    public MetadataDto author(String author) {
        this.author = author;
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getPublishdate() {
        return publishdate;
    }

    public MetadataDto publishdate(LocalDateTime publishdate) {
        this.publishdate = publishdate;
        return this;
    }

    public void setPublishdate(LocalDateTime publishdate) {
        this.publishdate = publishdate;
    }

    public String getDescription() {
        return description;
    }

    public MetadataDto description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
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
        MetadataDto metadata = (MetadataDto) o;
        if (metadata.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), metadata.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Metadata{" +
            "id=" + getId() +
            ", docid='" + getDocid() + "'" +
            ", content='" + getContent() + "'" +
            ", url='" + getUrl() + "'" +
            ", title='" + getTitle() + "'" +
            ", status=" + getStatus() +
            ", author='" + getAuthor() + "'" +
            ", publishdate='" + getPublishdate() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
